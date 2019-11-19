package com.wlkg.mapper;


import com.wlkg.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
    @Select("select * from tb_category where id = #{pid}")
    Category selectParentById(Long pid);

    //根据品牌ID查询商品的分类
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);
}
