package com.wlkg.service;

import com.wlkg.mapper.SpecGroupMapper;
import com.wlkg.mapper.SpecParamMapper;
import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;



    //查询SpecGroup表
    public List<SpecGroup> querySpecGroups(Long cid){
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return specGroupMapper.select(specGroup);
    }


    //查询SpecParam表
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic){
        SpecParam specParam = new SpecParam();
        if(gid != null){
            specParam.setGroupId(gid);
        }
        if(cid != null){
            specParam.setCid(cid);
        }
        if(searching != null){
            specParam.setSearching(searching);
        }
        if(generic != null){
            specParam.setGeneric(generic);
        }

        return specParamMapper.select(specParam);
    }

    //向SpecGroup表中插入数据
    public void insertSpecGroup(SpecGroup specGroup){
        specGroupMapper.insert(specGroup);
    }

    //更新SpecGroup表中的内容
    public void updateSpecGroup(SpecGroup specGroup){
        specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }


    //根据id删除SpecGroup表中的内容
    public void deleteSpecGroup(Long id){
        specGroupMapper.deleteByPrimaryKey(id);
    }


    //根据group_id删除SpecParam表中的内容
    public void deleteSpecParamByGid(Long gid){
        specParamMapper.deleteSpecParam(gid);
    }

    //向SpecParam表中插入数据
    public void insertSpecParam(SpecParam specParam){
        specParamMapper.insert(specParam);
    }

    //根据主键更新SpecParam表中的内容
    public void updateSpecParam(SpecParam specParam){
        specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    //根据主键删除
    public void deleteSpecParamById(Long id){
        specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecGroup> querySpecsByCid(Long cid) {
        //查询规格组
        List<SpecGroup> groups = querySpecGroups(cid);

        //查询当前分类下的参数
        List<SpecParam> specParams = querySpecParams(null,cid,null,null);

        Map<Long,List<SpecParam>> map = new HashMap<>();

        for (SpecParam param : specParams) {
            if(!map.containsKey(param.getGroupId())){
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(),new ArrayList<>());
            }

            map.get(param.getGroupId()).add(param);
        }

        //填充param到group中
        for(SpecGroup specGroup : groups){
            specGroup.setParams(map.get(specGroup.getId()));
        }

        return groups;
    }
}
