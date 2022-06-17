package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.shop.ProductMapper;
import com.mpprojects.mpmarket.model.shop.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class PruductController {

    @Autowired
    private ProductMapper productMapper;

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product){

        //使用sn对传入的产品进行数据库比对，没有则添加。
        QueryWrapper<Product> snWrapper = new QueryWrapper<>();
        snWrapper.eq("sn",product.getSn());
        Product product1 = productMapper.selectOne(snWrapper);
//        Product product1 = productMapper.selectBySn(product.getSn());
        if (product1 != null){
            return "商品已存在！";
        }
        productMapper.insert(product);
        return "商品" + product.getPname() + "已成功添加";
    }

    //更新信息之前要获取这一条商品记录，所以肯定有id，所以根据id进行返回更新
    @PutMapping("/update")
    public String updateProduct(@RequestBody Product product){
        Long getid = product.getId();
        Integer a = productMapper.updateById(product);
        return "更新完成,记录数:" + a.toString();
    }

    @GetMapping("/page")
    public IPage<Product> pageAll(@RequestParam long current,
                                  @RequestParam long size){
        IPage<Product> iPage = new Page<>(current,size);
        return productMapper.selectPage(iPage,null);
    }

    @GetMapping("/getbysn")
    public Product getBySn(@RequestParam long sn){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sn",sn);
        return productMapper.selectOne(queryWrapper);
    }

    @DeleteMapping("/deletebyid")
    public String deletebyid(@RequestParam Long id){
        productMapper.deleteById(id);
        return "此次删除成功。一旦产生交易记录，商品不应该被删除";
    }
}
