package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.shop.ProductTypeMapper;
import com.mpprojects.mpmarket.model.shop.ProductType;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/productType")
public class ProductTypeController {

    @Resource
    private ProductTypeMapper productTypeMapper;

    @PostMapping("/add")
    public Response<ProductType> add(@RequestBody ProductType productType){
        productTypeMapper.insert(productType);
        return new Response<ProductType>("200","商品类别添加成功，结果为：",productType);
    }

    @DeleteMapping("/delete")
    public Response delete(@RequestParam Long typeid){
        productTypeMapper.deleteById(typeid);
        return new Response("200","根据id删除成功",null);
    }

    @PutMapping("/update")
    public Response<ProductType> update(@RequestBody ProductType productType){
        productTypeMapper.updateById(productType);
        return new Response("200","商品类别添加成功",productType);
    }

    @GetMapping("/getOne")
    public Response<ProductType> getOne(@RequestParam Long typeid){
        return new Response("200","根据id获取到对象",productTypeMapper.selectById(typeid));
    }

    @GetMapping("/pageAll")
    public Response<IPage<ProductType>> pageAll(@RequestParam long current,
                         @RequestParam long size){
        IPage iPage = new Page(current,size);
        return new Response<>("200","获取到Ipage分页对象",productTypeMapper.selectPage(iPage,null));
    }
}
