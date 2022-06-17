package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductMapper;
import com.mpprojects.mpmarket.dao.shop.CouponRuleMapper;
import com.mpprojects.mpmarket.model.shop.CouponRule;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couponrule")
public class CouponRuleController {

    @Autowired
    private CouponRuleMapper couponRuleMapper;

    @Autowired
    private CouponRuleToProductMapper couponRuleToProductMapper;

    //添加一个规则对象
    @PostMapping("/add")
    public String add(@RequestBody CouponRule couponRule){
        couponRuleMapper.insert(couponRule);
        long id = couponRule.getId();
        return "Coupon规则保存成功，主键id为：" + id;
    }

    //根据id删除指定规则
    @DeleteMapping("/delete")
    public String delete(@RequestParam long id){
        couponRuleMapper.deleteById(id);
        return "id为：" + id + "的规则已删除";
    }

    //更新规则
    @PutMapping("/put")
    public String update(@RequestBody CouponRule couponRule){
        couponRuleMapper.updateById(couponRule);
        return "id为：" + couponRule.getId() +"的规则已更新";
    }

    //根据id获得规则对象
    @GetMapping("/getById")
    public CouponRule getById(@RequestParam long id){
        return couponRuleMapper.selectById(id);
    }

    //将所有规则分页。
    @GetMapping("/pageAll")
    public IPage<CouponRule> pageAll(@RequestParam long current,
                                     @RequestParam long size){
        IPage<CouponRule> iPage = new Page<>(current,size);
        return couponRuleMapper.selectPage(iPage,null);
    }

    /** 其他的有关规则的操作 */

    //生成规则与商品的中间表记录
    @PostMapping("/relateProduct")
    public String relate(@RequestParam long ruleid,
                         @RequestParam long productid){
        CouponRuleToProduct couponRuleToProduct = new CouponRuleToProduct();
        couponRuleToProduct.setProductId(productid);
        couponRuleToProduct.setRuleId(ruleid);
        couponRuleToProductMapper.insert(couponRuleToProduct);
        return "规则与商品关联成功，规则id为："+ ruleid + "商品id为:" + productid;
    }

}
