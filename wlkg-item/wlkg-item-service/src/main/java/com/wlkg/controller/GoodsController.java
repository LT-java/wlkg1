package com.wlkg.controller;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.pojo.PageResult;
import com.wlkg.pojo.Sku;
import com.wlkg.pojo.Spu;
import com.wlkg.pojo.SpuDetail;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    /***
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoogs(@RequestBody Spu spu){
        goodsService.save(spu);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);

    }

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> getSpuByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page, //当前页面
            @RequestParam(value = "rows",defaultValue = "5")Integer rows, //每页的条数
            @RequestParam(value = "saleable",required = false)Boolean saleable, //是否上架
            @RequestParam(value = "valid",required = false)Boolean valid,//是否被逻辑删除
            @RequestParam(value = "key",required = false)String key){ //关键词搜索

            //分页查询信息
            PageResult<Spu> result = goodsService.querySpuByPageAndSort(page,rows,saleable,key,valid);


            return ResponseEntity.ok(result);
    }

    //查询回显spuDetial
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id")Long id){
        SpuDetail spuDetail = goodsService.querySpuDetailById(id);

        if(spuDetail != null){
            return ResponseEntity.ok(spuDetail);
        }else{
            throw new WlkgException(ExceptionEnums.SPUDETAIL_NOT_FOUND);
        }
    }


    //查询回显的sku集合
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkusById(@RequestParam("id")Long id){
        System.out.println(id);
        List<Sku> list = goodsService.querySkusById(id);


        if(list == null || list.size() == 0){
            throw new WlkgException(ExceptionEnums.SKUS_NOT_FOUND);
        }else{
            return ResponseEntity.ok(list);
        }
    }


    @PutMapping("/goods")
    public ResponseEntity<Void> updateSpu(@RequestBody Spu spu){
        goodsService.update(spu);

        return ResponseEntity.ok(null);
    }


    @DeleteMapping("/spu/delete/{id}")
    public ResponseEntity<Void> deleteSpu(@PathVariable("id")Long id){
        System.out.println(id);
        goodsService.delete(id);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/spu/sale/{id}")
    public ResponseEntity<Void> editSaleable(@PathVariable("id")Long id){
        System.out.println(id);
        goodsService.editSale(id);
        return ResponseEntity.ok(null);
    }



    //根据spuId查询spu对象
    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long id){
        Spu spu = goodsService.querySpuById(id);
        return ResponseEntity.ok(spu);
    }
}
