package com.wlkg.mapper;

import com.wlkg.pojo.Sku;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;


public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku> {

    @Select("delete from tb_sku where spu_id = #{id}")
    void deleteBySpuId(Long id);

    @Select("select id from tb_sku where spu_id = #{id}")
    List<Long> selectOneSkuBySpuId(Long id);
}
