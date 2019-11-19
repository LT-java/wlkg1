package com.wlkg.mapper;

import com.wlkg.pojo.Spu;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<Spu> {

    @Select("update tb_spu set saleable = 0 where id = #{sid}")
    void updateSaleable(Long sid);

    @Select("update tb_spu set valid = 0 where id = #{id}")
    void updateValid(Long id);
}
