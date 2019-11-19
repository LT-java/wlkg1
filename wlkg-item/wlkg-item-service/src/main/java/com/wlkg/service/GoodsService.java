package com.wlkg.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.mapper.SkuMapper;
import com.wlkg.mapper.SpuDetailMapper;
import com.wlkg.mapper.SpuMapper;
import com.wlkg.mapper.StockMapper;
import com.wlkg.pojo.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    //保存跟spu相关的数据
    @Transactional
    public void save(Spu spu){
        //保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);


        //保存spu详情
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);


        //保存sku和库存信息
        saveSkuAndStock(spu.getSkus(),spu.getId());

        //发送消息
        sendMessage(spu.getId(),"insert");

    }

    //保存sku和库存的信息
    private void saveSkuAndStock(List<Sku> skus,Long spuId){
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku: skus) {
            //保存sku,先将字段赋值
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insert(sku);


            //保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);

        }

        stockMapper.insertList(stocks);

    }

    //分页查询商品列表
    public PageResult<Spu> querySpuByPageAndSort(Integer page, Integer rows, Boolean saleable, String key,Boolean valid) {

        //分页查询，最多允许查询100条
        PageHelper.startPage(page,Math.min(rows,100));
        //创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //考虑是否上架
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        //是否被逻辑删除
        if(valid != null){
            criteria.andEqualTo("valid",valid);
        }

        //是否模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%" + key + "%");
        }

        //默认排序
        example.setOrderByClause("last_update_time desc");


        //查询
        List<Spu> spus = spuMapper.selectByExample(example);


        if(CollectionUtils.isEmpty(spus)){
            throw new WlkgException(ExceptionEnums.GOODS_NOT_FOUNT);
        }
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        for (Spu spu : spus) {
            //查询spu的商品分类，要查三级分类
            List<String> names = categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            //将分类名称拼接后存入cname
            spu.setCname(StringUtils.join(names,"/"));


            //查询spu的品牌名称
            Brand brand = brandService.queryBrandByPrimaryKey(spu.getBrandId());
            spu.setBname(brand.getName());

        }

        return new PageResult<>(pageInfo.getTotal(),spus);
    }

    //通过spu_id查询商品详情
    public SpuDetail querySpuDetailById(Long id) {

        return spuDetailMapper.selectByPrimaryKey(id);
    }


    //通过spu_id查询sku集合
    public List<Sku> querySkusById(Long id) {
        Sku sku  = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        for (Sku sk : skus) {
            sk.setStock(stockMapper.selectByPrimaryKey(sk.getId()).getStock());
        }

        return skus;
    }



    //更新spu以及相关的表
    @Transactional
    public void update(Spu spu) {

        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skus = skuMapper.select(sku);
        if(!CollectionUtils.isEmpty(skus)){
            //若根据spu_id能查到sku，则删除所有此spu_id的sku
            skuMapper.delete(sku);
            //删除stork
            List<Long> ids = skus.stream().map(s->s.getId()).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }

        //修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        int row = spuMapper.updateByPrimaryKeySelective(spu);
        if(row == 0){
            throw new WlkgException(ExceptionEnums.SPU_UPDATE_ERROR);
        }

        //更新spu详情
        row = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(row == 0){
            throw new WlkgException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
        }
        //发送消息
        sendMessage(spu.getId(),"update");



        saveSkuAndStock(spu.getSkus(),spu.getId());
    }

    //按照spu_id将关联的所有记录都删除
    public void delete(Long id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setValid(false);
        //spuMapper.updateValid(id);
        spuMapper.updateByPrimaryKeySelective(spu);

        //删除时发送消息
        sendMessage(id,"delete");

        /*spuMapper.deleteByPrimaryKey(id);
        spuDetailMapper.deleteByPrimaryKey(id);
        List<Long> ids = skuMapper.selectOneSkuBySpuId(id);//根据spuId查询出所有的skuid
        skuMapper.deleteBySpuId(id);//根据spuId删除sku表中的数据
        stockMapper.deleteByIdList(ids);//根据skuId删除库存表中的记录*/
    }


    //修改下架
    public void editSale(Long id) {

        Spu spu = spuMapper.selectByPrimaryKey(id);//通过spu_id查出spu对象
        spu.setSaleable(!spu.getSaleable());//将spu的saleable字段值取反
        spuMapper.updateByPrimaryKeySelective(spu);//按主键更新其他不为空的字段值

       /* Spu spu = spuMapper.selectByPrimaryKey(id);
        System.out.println(spu.getSaleable());
        //boolean sale = !spu.getSaleable();
        spuMapper.updateSaleable(id);*/
    }

    //查询spu对象
    public Spu querySpuById(Long id) {

        Spu spu = spuMapper.selectByPrimaryKey(id);

        spu.setSkus(querySkusById(id));

        spu.setSpuDetail(querySpuDetailById(id));

        return spu;
    }


    //发送消息到mq
    private void sendMessage(Long spuId,String type){
        try {

            amqpTemplate.convertAndSend("item." + type, spuId);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
