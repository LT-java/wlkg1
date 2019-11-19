package com.wlkg.controller;
import com.wlkg.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;


@Controller
public class GoodsController {

    @Autowired
    private PageService pageService;


    @GetMapping("/item/{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long id){

        //查询模型数据
        Map<String,Object> attributes = pageService.loadModel(id);

        //准备模型数据
        model.addAllAttributes(attributes);


        if(!pageService.exists(id)){
            pageService.syncCreateHtml(id);
        }
        return "item";
    }

    //@GetMapping(value = "/item/{id}.html",produces = "application/json")
    /*@ResponseBody
    public Map<String,Object> toItemPage_back(Model model, @PathVariable("id")Long id){

        //查询模型数据
        Map<String,Object> attributes = pageService.loadModel(id);

        //准备模型数据
        //model.addAllAttributes(attributes);

        return attributes;
    }*/

}
