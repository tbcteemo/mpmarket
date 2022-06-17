package com.mpprojects.mpmarket.controller.shop.relationship;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductTypeMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProductType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/couponRuleToProductType")
public class CouponRuleToProductTypeController {

    @Resource
    private CouponRuleToProductTypeMapper couponRuleToProductTypeMapper;

    @PostMapping("/add")
    public String add(@RequestBody CouponRuleToProductType couponRuleToProductType){
        couponRuleToProductTypeMapper.insert(couponRuleToProductType);
        return "规则与商品类目关联成功，记录id为：" + couponRuleToProductType.getId().toString();
    }

    @DeleteMapping("/delete")
    public String add(@RequestParam long id){
        couponRuleToProductTypeMapper.deleteById(id);
        return "删除成功";
    }

    @PutMapping("/update")
    public String update(@RequestBody CouponRuleToProductType couponRuleToProductType){
        couponRuleToProductTypeMapper.updateById(couponRuleToProductType);
        return "修改成功，该条记录id为" + couponRuleToProductType.getId().toString();
    }

    @GetMapping("/getOne")
    public CouponRuleToProductType getone(@RequestParam long id){
        return couponRuleToProductTypeMapper.selectById(id);
    }

    @GetMapping("/pageAll")
    public IPage<CouponRuleToProductType> pageAll(@RequestParam long current,
                                                  @RequestParam long size){
        IPage iPage  = new Page(current,size);
        return couponRuleToProductTypeMapper.selectPage(iPage,null);
    }
}
