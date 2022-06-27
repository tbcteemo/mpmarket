package com.mpprojects.mpmarket.controller.shop.productSystem;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.shop.ProductMapper;
import com.mpprojects.mpmarket.model.shop.Product;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = {"商品系统", "基础功能"})
@RestController
@RequestMapping("/product")
public class PruductController {

    @Resource
    private ProductMapper productMapper;

    @ApiOperation(value = "传入实体对象，添加到数据库中",tags = "添加")
    @PostMapping("/add")
    public Response addProduct(@ApiParam(type = "商品实体",required = true)
                                   @RequestBody Product product){

        //使用sn对传入的产品进行数据库比对，没有则添加。
        QueryWrapper<Product> snWrapper = new QueryWrapper<>();
        snWrapper.eq("sn",product.getSn());
        Product product1 = productMapper.selectOne(snWrapper);
//        Product product1 = productMapper.selectBySn(product.getSn());
        if (product1 != null){
            return new Response("1003","商品已存在！");
        }
        productMapper.insert(product);
        return new Response("200","商品" + product.getPname() + "已成功添加");
    }

    //更新信息之前要获取这一条商品记录，所以肯定有id，所以根据id进行返回更新
    @ApiOperation(value = "传入商品实体对象，并将新的信息更新到数据库对应记录中",tags = "修改")
    @PutMapping("/update")
    public Response updateProduct(@ApiParam(type = "商品实体",required = true)
                                      @RequestBody Product product){
        Long getid = product.getId();
        Integer a = productMapper.updateById(product);
        return new Response("200","更新完成,记录数:" + a.toString());
    }

    @ApiOperation(value = "传入分页参数，对所有商品进行分页返回操作",tags = "分页")
    @GetMapping("/page")
    public IPage<Product> pageAll(@ApiParam(type = "当前页",required = true)
                                      @RequestParam long current,
                                  @ApiParam(type = "每页条数",required = true)
                                        @RequestParam long size){
        IPage<Product> iPage = new Page<>(current,size);
        return productMapper.selectPage(iPage,null);
    }

    @ApiOperation(value = "根据传入的sn码获取商品对象",tags = "获取")
    @GetMapping("/getbysn")
    public Response<Product> getBySn(@ApiParam(type = "商品的sn码",value = "sn码是一种商品独一无二的身份识别码",required = true)
                                         @RequestParam long sn){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sn",sn);
        return new Response<>("200","根据SN获取商品成功",productMapper.selectOne(queryWrapper));
    }

    @ApiOperation(value = "根据传入的id删除商品对象",tags = "删除")
    @DeleteMapping("/deletebyid")
    public Response deletebyid(@ApiParam(type = "商品id",required = true)
                                   @RequestParam Long id){
        productMapper.deleteById(id);
        return new Response("200","此次删除成功。一旦产生交易记录，商品不应该被删除");
    }
}
