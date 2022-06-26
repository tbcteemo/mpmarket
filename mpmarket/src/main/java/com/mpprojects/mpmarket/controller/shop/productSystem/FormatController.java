package com.mpprojects.mpmarket.controller.shop.productSystem;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpprojects.mpmarket.dao.shop.ProductFormatMapper;
import com.mpprojects.mpmarket.model.shop.ProductFormat;
import com.mpprojects.mpmarket.service.shop.ProductFormatService;
import com.mpprojects.mpmarket.service.shop.impl.ProductFormatServiceImpl;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = {"商品系统", "辅助功能"})
@RestController
@RequestMapping("/format")
public class FormatController {

    @Resource
    private ProductFormatMapper productFormatMapper;

    @Resource
    private ProductFormatService productFormatService;

    @ApiOperation(value = "传入一个规格实体，并添加到数据库中",tags = "添加")
    @PostMapping("/add")
    public Response<ProductFormat> add(@ApiParam(name = "规格实体",required = true)
                                           @RequestBody ProductFormat format){
        QueryWrapper<ProductFormat> addwrapper = new QueryWrapper<>();
        addwrapper.eq("format_value","format.getFormatValue()");
        ProductFormat selectFormat = productFormatMapper.selectOne(addwrapper);
        if (selectFormat != null){
            return new Response("1002","该规格已经存在",selectFormat);
        }
        productFormatMapper.insert(format);
        return new Response("200","添加成功",format);
    }


    @ApiOperation(value = "根据传入id删除对应规格",tags = "删除")
    @DeleteMapping("/delete")
    public Response delete(@ApiParam(name = "规格id",type = "主键id",required = true)
                               @RequestParam Long id){
        productFormatMapper.deleteById(id);
        return new Response("200","根据id删除成功");
    }

    @ApiOperation(value = "传入规格实体，修改对应记录并更新到数据库",tags = "修改")
    @PutMapping("/put")
    public Response put(@ApiParam(name = "规格实体",required = true)
                            @RequestBody ProductFormat productFormat){
        ProductFormat format = productFormatMapper.selectById(productFormat.getId());
        if (format == null){
            return new Response("1002","该规格不存在，请转至添加");
        }
        productFormatMapper.updateById(productFormat);
        return new Response("200","修改成功");
    }

    @ApiOperation(value = "根据传入的id获取对应规格记录",tags = "获取")
    @GetMapping("/get")
    public Response<ProductFormat> get(@ApiParam(name = "规格id",type = "主键id",required = true)
                                           @RequestParam Long id){
        return new Response<>("200","根据id获取规格对象成功",productFormatMapper.selectById(id));
    }
}
