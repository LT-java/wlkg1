package com.wlkg.controller;


import com.wlkg.common.pojo.PageResult;
import com.wlkg.pojo.Brand;
import com.wlkg.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class BrandController {

    @Autowired
    private BrandService brandService;


    //查询显示brand列表
    @GetMapping("/brand/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "true") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        PageResult<Brand> result = this.brandService.queryBrandByPageAndSort(page,rows,sortBy,desc, key);
        return ResponseEntity.ok(result);
    }



    //保存品牌
    @PostMapping("/brand")
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){

        brandService.saveBrandWithCategory(brand,cids);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    //修改品牌信息
    @PutMapping("/brand")
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids){

        brandService.saveBrandWithCategory(brand,cids);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    //删除品牌信息
    @DeleteMapping("/brand/delete/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid")Long bid){
        brandService.deleteBrandAndCategory(bid);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/brand/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandCategory(@PathVariable("cid")Long cid){
        System.out.println(cid);
        List<Brand> list = brandService.queryBrandByCategoryId(cid);

        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        return ResponseEntity.ok(brandService.queryBrandByPrimaryKey(id));
    }

}
