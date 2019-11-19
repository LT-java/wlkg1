package com.wlkg.service;

import com.wlkg.api.BrandClient;
import com.wlkg.api.CategoryClient;
import com.wlkg.api.GoodsClient;
import com.wlkg.api.SpecificationClient;
import com.wlkg.pojo.*;
import com.wlkg.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecificationClient specificationClient;

    @Value("${wlkg.thymeleaf.destPath}")
    private String destPath;

    @Autowired
    private TemplateEngine templateEngine;


    private static final Logger logger = LoggerFactory.getLogger(PageService.class);

    public Map<String,Object> loadModel(Long id){
        //模型数据
        Map<String,Object> modelMap = new HashMap<>();

        //查询spu
        Spu spu = goodsClient.querySpuById(id);



        //查询spuDetail
        SpuDetail detail = spu.getSpuDetail();

        //查询sku
        List<Sku> skus = spu.getSkus();

        //准备品牌数据
        Brand brand = brandClient.queryBrandById(spu.getBrandId());


        //准备商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));


        //查询规格参数
        List<SpecGroup> specs = specificationClient.querySpecsByCid(spu.getCid3());
        System.out.println(specs);



        //装填模型数据
        modelMap.put("spu",spu);
        modelMap.put("title",spu.getTitle());
        modelMap.put("subTitle",spu.getSubTitle());
        modelMap.put("detail",detail);
        modelMap.put("skus",skus);
        modelMap.put("brand",brand);
        modelMap.put("categories",categories);
        modelMap.put("specs",specs);
       /* modelMap.put("paramMap",detail.getSpecialSpec());*/
        return modelMap;



    }

    //创建html页面
    public void createHtml(Long spuId) throws Exception{

        //创建上下文
        Context context = new Context();
        //把数据加入上下文
        context.setVariables(loadModel(spuId));


        //创建输出流，关联到一个临时文件
        File temp = new File(spuId+".html");

        //目标页面文件
        File dest = createPath(spuId);

        //备份原页面文件
        File bak = new File(spuId + "_bak.html");

        try(PrintWriter writer = new PrintWriter(temp,"UTF-8")){
            //利用thymeleaf模板引擎生成静态页面
            templateEngine.process("item",context,writer);
            if(dest.exists()){
                //如果目标文件已经存在，先备份
                dest.renameTo(bak);
            }
            //将新页面覆盖旧页面
            FileCopyUtils.copy(temp,dest);

            //成功后及那个备份页面删除
            bak.delete();
        }catch (IOException e){
            //失败后，将备份页面恢复
            bak.renameTo(dest);
            throw new Exception(e);
        }finally {
            //删除临时页面
            if(temp.exists()){
                temp.delete();
            }
        }

    }


    //删除html页面
    public void deleteHtml(Long id){
        File file = new File(destPath,id + ".html");
        file.deleteOnExit();
    }


    private File createPath(Long id){
        if(id == null){
            return null;
        }

        File dest = new File(this.destPath);
        if(!dest.exists()){
            dest.mkdirs();
        }
        return new File(dest,id+".html");
    }


    //判断某个商品的页面是否存在
    public boolean exists(Long id){
        return createPath(id).exists();
    }



    //异步创建html页面
    public void syncCreateHtml(Long id){
        ThreadUtils.execute(()->{
            try{

                createHtml(id);

            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }

}
