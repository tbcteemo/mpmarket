package com.mpprojects.mpmarket.controller.shop;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpprojects.mpmarket.dao.shop.ProductFormatMapper;
import com.mpprojects.mpmarket.model.shop.ProductFormat;
import com.mpprojects.mpmarket.service.shop.ProductFormatService;
import com.mpprojects.mpmarket.service.shop.impl.ProductFormatServiceImpl;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/format")
public class FormatController {

    @Autowired
    private ProductFormatMapper productFormatMapper;

    @Autowired
    private ProductFormatService productFormatService;

    @PostMapping("/add")
    public Response<ProductFormat> add(@RequestBody ProductFormat format){
        QueryWrapper<ProductFormat> addwrapper = new QueryWrapper<>();
        addwrapper.eq("format_value","format.getFormatValue()");
        ProductFormat selectFormat = productFormatMapper.selectOne(addwrapper);
        if (selectFormat != null){
            return new Response("1002","该规格已经存在",selectFormat);
        }
        productFormatMapper.insert(format);
        return new Response("200","添加成功",format);
    }

    @DeleteMapping("/delete")
    public Response delete(@RequestParam Long id){
        productFormatMapper.deleteById(id);
        return new Response("200","根据id删除成功");
    }

    @PutMapping("/put")
    public Response put(@RequestBody ProductFormat productFormat){
        ProductFormat format = productFormatMapper.selectById(productFormat.getId());
        if (format == null){
            return new Response("1002","该规格不存在，请转至添加");
        }
        productFormatMapper.updateById(productFormat);
        return new Response("200","修改成功");
    }

    @GetMapping("/get")
    public Response<ProductFormat> get(@RequestParam Long id){
        return new Response<>("200","根据id获取规格对象成功",productFormatMapper.selectById(id));
    }
}
