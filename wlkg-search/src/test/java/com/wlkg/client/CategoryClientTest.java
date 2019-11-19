package com.wlkg.client;


import com.wlkg.WlkgSearchService;
import com.wlkg.pojo.Brand;
import com.wlkg.pojo.Category;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WlkgSearchService.class)
public class CategoryClientTest {


    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;




    @Test
    public void test1(){
        List<Category> list = categoryClient.queryCategoryByIds(Arrays.asList(1L,2L,3L));
        list.forEach(s-> System.out.println(s.getName()));
    }



    @Test
    public void test2(){
        Brand brand = brandClient.queryBrandById(8214L);
        System.out.println(brand);
    }
}