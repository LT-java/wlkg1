package com.wlkg.client;

import com.wlkg.WlkgSearchService;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.pojo.Goods;
import com.wlkg.pojo.Spu;
import com.wlkg.repository.GoodsRepository;
import com.wlkg.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WlkgSearchService.class)
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsService goodsService;


    @Test
    public void test(){
        //创建索引
        template.createIndex(Goods.class);
        //配置映射
        template.putMapping(Goods.class);
    }



    //导入数据
    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;

        do{

            //查询分页数据
             PageResult<Spu> result = goodsClient.getSpuByPage(page,rows,true,true,null);
            List<Spu> spus = result.getItems();

            size = spus.size();
            //创建Goods集合
            List<Goods> goodsList = new ArrayList<>();

            for (Spu spu : spus) {

                    Goods goods = goodsService.buildGoods(spu);
                    goodsList.add(goods);

            }

            goodsRepository.saveAll(goodsList);
            page++;
        }while(size == 100);
    }




}
