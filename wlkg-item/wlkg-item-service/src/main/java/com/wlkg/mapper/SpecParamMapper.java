package com.wlkg.mapper;

import com.wlkg.pojo.SpecParam;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SpecParamMapper extends Mapper<SpecParam> {

    @Select("delete from tb_spec_param where group_id = #{gid}")
    void deleteSpecParam(Long gid);
}
