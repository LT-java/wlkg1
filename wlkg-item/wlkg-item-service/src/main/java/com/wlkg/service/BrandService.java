package com.wlkg.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.mapper.BrandMapper;
import com.wlkg.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        Page<Brand> pageInfo = (Page<Brand>) brandMapper.selectByExample(example);

        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }



    @Transactional
    public void saveBrandWithCategory(Brand brand,List<Long> cids){

        Brand brand1 = brandMapper.selectByPrimaryKey(brand.getId());
        if(brand1 != null){
            brandMapper.updateByPrimaryKeySelective(brand);

            brandMapper.deleteByBrandId(brand.getId());

        }else{
            brandMapper.insertSelective(brand);
        }

        for (Long cid : cids) {
            brandMapper.saveBrandWithCategory(cid,brand.getId());
        }


        }


    public void deleteBrandAndCategory(Long bid){
        brandMapper.deleteByPrimaryKey(bid);
        brandMapper.deleteByBrandId(bid);
    }



    public List<Brand> queryBrandByCategoryId(Long cid){
        return brandMapper.queryBrandByCategoryId(cid);
    }


    public Brand queryBrandByPrimaryKey(Long id){
        return brandMapper.selectByPrimaryKey(id);
    }


}