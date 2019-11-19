package com.wlkg.api;

import com.wlkg.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface BrandAPI {

    //根据spu_id查询brand名称
    @GetMapping("/brand/{id}")
    Brand queryBrandById(@PathVariable("id")Long id);

}
