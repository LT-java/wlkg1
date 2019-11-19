package com.wlkg.controller;


import com.wlkg.pojo.Brand;
import com.wlkg.pojo.Category;
import com.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getCategoryList(@RequestParam("pid")Long pid){

         List<Category>list = categoryService.getCategoryList(pid);
        return ResponseEntity.ok(list);
    }



    @PostMapping("/handAdd")
    public ResponseEntity<Category> handAdd(@RequestBody Category category){
        System.out.println(category);
        categoryService.insertCategory(category);
        Category c = categoryService.selectPartent(category.getParentId());
        c.setIsParent(true);
        categoryService.updateCategoryIsParent(c);
        System.out.println(c+"-------");
        return ResponseEntity.ok(category);
    }

    @PutMapping("/handEdit")
    public ResponseEntity<String> handEdit(@RequestBody Category category){
        System.out.println(category);
        categoryService.updateCategoryNameById(category);
        return ResponseEntity.ok("修改成功");

    }



    @PostMapping("/handDelete")
    public ResponseEntity<String> handDelete(@RequestBody Category category){
        System.out.println(category);
        int row = categoryService.deleteCategoryById(category.getId());
        if(row > 0){
            return ResponseEntity.ok("删除成功");
        }else{
            return ResponseEntity.ok("删除失败");
        }
    }

    @GetMapping("/bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid")Long bid){
        List<Category> list = categoryService.queryByBrandId(bid);

        if(list == null || list.size()<1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(list);
        }
    }


    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(categoryService.queryById(ids));
    }
}
