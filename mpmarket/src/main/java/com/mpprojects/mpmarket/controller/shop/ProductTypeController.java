package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.shop.ProductTypeMapper;
import com.mpprojects.mpmarket.model.shop.ProductType;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = {"商品系统","辅助"})
@RequestMapping("/productType")
public class ProductTypeController {

    @Resource
    private ProductTypeMapper productTypeMapper;

    @ApiOperation(value = "传入一个商品类型类对象，并添加到数据库中",tags = "添加")
    @PostMapping("/add")
    public Response<ProductType> add(@ApiParam(name = "商品类实体",type = "商品类实体对象",required = true)
                                         @RequestBody ProductType productType){
        productTypeMapper.insert(productType);
        return new Response<>("200","商品类别添加成功，结果为：",productType);
    }

    @ApiOperation(value = "根据id删除数据库的记录",tags = "删除")
    @DeleteMapping("/delete")
    public Response delete(@ApiParam(name = "商品类型id",type = "主键id",required = true)
                               @RequestParam Long typeid){
        productTypeMapper.deleteById(typeid);
        return new Response<>("200","根据id删除成功",null);
    }

    @ApiOperation(value = "传入一个商品类型类对象，并修改数据库的对应的记录",tags = "修改")
    @PutMapping("/update")
    public Response<ProductType> update(@ApiParam(name = "商品类实体",type = "商品类实体对象",required = true)
                                            @RequestBody ProductType productType){
        productTypeMapper.updateById(productType);
        return new Response<>("200","商品类别添加成功",productType);
    }

    @ApiOperation(value = "根据id获取一条商品类型记录",tags = "获取")
    @GetMapping("/getOne")
    public Response<ProductType> getOne(@ApiParam(name = "商品类型id",type = "主键id",required = true)
                                            @RequestParam Long typeid){
        return new Response<>("200","根据id获取到对象",productTypeMapper.selectById(typeid));
    }

    @ApiOperation(value = "传入current和size两个long类型值，选择所有的商品类型记录并进行分页",tags = "分页")
    @GetMapping("/pageAll")
    public IPage<ProductType> pageAll(@ApiParam(name = "当前页",type = "MybatisPlus提供的Ipage分页中的当前页",required = true)
                                                    @RequestParam long current,
                         @ApiParam(name = "一页的条目数",type = "MybatisPlus提供的Ipage分页中的size参数",required = true)
                                                    @RequestParam long size){
        IPage iPage = new Page(current,size);
        return iPage;
    }
}
