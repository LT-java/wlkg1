package com.wlkg.service;

import com.wlkg.common.exception.WlkgException;
import com.wlkg.mapper.CategoryMapper;
import com.wlkg.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public List<Category> getCategoryList(Long id){

        Category category = new Category();
        category.setParentId(id);
        List<Category> list = categoryMapper.select(category);
        if(CollectionUtils.isEmpty(list)){
            throw new WlkgException();
        }else{
            return list;
        }

    }

    public int insertCategory(Category category) {
        return categoryMapper.insertSelective(category);
    }


    public Category selectPartent(Long pid){
        return categoryMapper.selectParentById(pid);
    }

    public int updateCategoryNameById(Category category){
        Category c = new Category();
        c.setId(category.getId());
        c.setName(category.getName());
        return categoryMapper.updateByPrimaryKeySelective(c);
    }


    public int updateCategoryIsParent(Category category){
        return categoryMapper.updateByPrimaryKeySelective(category);
}

    public int deleteCategoryById(Long id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }

    public List<Category> queryByBrandId(Long bid) {
        return categoryMapper.queryByBrandId(bid);
    }

    //按id查询分类名称
    public List<String> queryNameByIds(List<Long> asList) {
        return categoryMapper.selectByIdList(asList).stream().map((c)->c.getName()).collect(Collectors.toList());
    }

    public List<Category> queryById(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
        }
        return list;
    }
}
