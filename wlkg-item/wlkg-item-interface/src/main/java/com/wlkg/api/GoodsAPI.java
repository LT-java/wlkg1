package com.wlkg.api;

import com.wlkg.common.pojo.PageResult;
import com.wlkg.pojo.Sku;
import com.wlkg.pojo.Spu;
import com.wlkg.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface GoodsAPI {

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param saleable
     * @param valid
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<Spu> getSpuByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page, //当前页面
            @RequestParam(value = "rows",defaultValue = "5")Integer rows, //每页的条数
            @RequestParam(value = "saleable",required = false)Boolean saleable, //是否上架
            @RequestParam(value = "valid",required = false)Boolean valid,//是否被逻辑删除
            @RequestParam(value = "key",required = false)String key);



    //查询回显spuDetial
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id")Long id);

    //根据spu_id查询sku集合
    @GetMapping("/sku/list")
    List<Sku> querySkusById(@RequestParam("id")Long id);

    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id")Long id);

}
