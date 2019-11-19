package com.wlkg.mapper;

import com.wlkg.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>{

    @Select("insert into tb_category_brand values(#{cid},#{bid})")
    void saveBrandWithCategory(@Param("cid")Long cid, @Param("bid")Long bid);

    @Select("delete from tb_category_brand where brand_id = #{bid}")
    void deleteByBrandId(@Param("bid")Long bid);

    @Select("select tb_brand.* from tb_brand join tb_category_brand on tb_brand.id = tb_category_brand.brand_id where tb_category_brand.category_id = #{cid}")
    List<Brand> queryBrandByCategoryId(Long cid);

}
