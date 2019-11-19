package com.wlkg.controller;


import com.wlkg.common.pojo.PageResult;
import com.wlkg.pojo.Goods;
import com.wlkg.pojo.SearchRequest;
import com.wlkg.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private GoodsService goodsService;


    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request){
        return ResponseEntity.ok(goodsService.search(request));
    }
}
