package com.mpprojects.mpmarket.controller.shop.relationship;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductTypeMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProductType;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/couponRuleToProductType")
public class CouponRuleToProductTypeController {

    @Resource
    private CouponRuleToProductTypeMapper couponRuleToProductTypeMapper;

    @PostMapping("/add")
    public Response add(@RequestBody CouponRuleToProductType couponRuleToProductType){
        couponRuleToProductTypeMapper.insert(couponRuleToProductType);
        return new Response("200",
                "规则与商品类目关联成功，记录id为：" + couponRuleToProductType.getId().toString());
    }

    @DeleteMapping("/delete")
    public Response add(@RequestParam long id){
        couponRuleToProductTypeMapper.deleteById(id);
        return new Response("200","根据id删除成功");
    }

    @PutMapping("/update")
    public Response update(@RequestBody CouponRuleToProductType couponRuleToProductType){
        couponRuleToProductTypeMapper.updateById(couponRuleToProductType);
        return new Response("200","修改成功，该条关联表记录id为" + couponRuleToProductType.getId().toString());
    }

    @GetMapping("/getOne")
    public Response<CouponRuleToProductType> getone(@RequestParam long id){
        return new Response<>("200",
                "根据主键id选择优惠券规则与商品的关联记录成功",
                couponRuleToProductTypeMapper.selectById(id));
    }

    @GetMapping("/pageAll")
    public IPage<CouponRuleToProductType> pageAll(@RequestParam long current,
                                                  @RequestParam long size){
        IPage iPage  = new Page(current,size);
        return couponRuleToProductTypeMapper.selectPage(iPage,null);
    }
}
