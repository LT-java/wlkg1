package com.wlkg.controller;


import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import com.wlkg.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;



    //查询分组列表
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>>
    querySpecGroups(@PathVariable("cid")Long cid){
        List<SpecGroup> list = specificationService.querySpecGroups(cid);
        System.out.println(list);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);


    }

    //查询参数信息列表
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParams(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching,
            @RequestParam(value="generic", required = false) Boolean generic
    ){
        System.out.println(cid);
        List<SpecParam> list = specificationService.querySpecParams(gid,cid,searching,generic);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }


    //添加分组信息
    @PostMapping("/group")
    public ResponseEntity<Void> addSpecGroup(@RequestBody SpecGroup specGroup){
        System.out.println(specGroup);
        specificationService.insertSpecGroup(specGroup);
        return ResponseEntity.ok(null);
    }

    //更改分组信息
    @PutMapping("/group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){
        System.out.println(specGroup);
        specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.ok(null);
    }

    //删除分组信息
    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id")Long id){
        System.out.println(id);
        specificationService.deleteSpecGroup(id);//删除tb_spec_group表中的内容
        specificationService.deleteSpecParamByGid(id);//删除tb_spec_param表中的内容
        return ResponseEntity.ok(null);
    }

    //添加参数信息
    @PostMapping("/param")
    public ResponseEntity<Void> addSpecParam(@RequestBody SpecParam specParam){
        System.out.println(specParam);
        specificationService.insertSpecParam(specParam);
        return ResponseEntity.ok(null);
    }

    //更改参数信息
    @PutMapping("/param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParam specParam){
        System.out.println(specParam);
        specificationService.updateSpecParam(specParam);
        return ResponseEntity.ok(null);
    }

    //删除参数信息
    @DeleteMapping("/param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id")Long id){
        System.out.println(id);
        specificationService.deleteSpecParamById(id);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@RequestParam("cid")Long cid){
        List<SpecGroup> list = specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(list);
    }

}
