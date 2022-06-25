package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductMapper;
import com.mpprojects.mpmarket.dao.shop.CouponRuleMapper;
import com.mpprojects.mpmarket.model.shop.CouponRule;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProduct;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = {"优惠券系统","辅助"})
@RequestMapping("/couponrule")
public class CouponRuleController {

    @Resource
    private CouponRuleMapper couponRuleMapper;

    @Resource
    private CouponRuleToProductMapper couponRuleToProductMapper;

    //添加一个规则对象
    @ApiOperation(value = "传入一个优惠券的规则对象，并添加到数据库中",tags = "添加")
    @PostMapping("/add")
    public Response add(@ApiParam(name = "优惠券规则实体",type = "优惠券规则实体对象",required = true)
                            @RequestBody CouponRule couponRule){
        couponRuleMapper.insert(couponRule);
        long id = couponRule.getId();
        return new Response("200","Coupon规则保存成功，主键id为：" + id);
    }

    //根据id删除指定规则
    @ApiOperation(value = "根据id删除数据库的记录",tags = "删除")
    @DeleteMapping("/delete")
    public Response delete(@ApiParam(name = "优惠券规则实体的id",type = "主键id",required = true)
                               @RequestParam long id){
        couponRuleMapper.deleteById(id);
        return new Response("200","id为：" + id + "的规则已删除");
    }

    //更新规则
    @ApiOperation(value = "传入一个商品类型类对象，并在数据库中更新",tags = "修改")
    @PutMapping("/put")
    public Response update(@ApiParam(name = "优惠券规则实体",type = "优惠券规则对象",required = true)
                               @RequestBody CouponRule couponRule){
        couponRuleMapper.updateById(couponRule);
        return new Response("200","id为：" + couponRule.getId() +"的规则已更新");
    }

    //根据id获得规则对象
    @ApiOperation(value = "根据id获取数据库的记录",tags = "获取")
    @GetMapping("/getById")
    public Response<CouponRule> getById(@ApiParam(name = "优惠券规则实体的id",type = "主键id",required = true)
                                            @RequestParam long id){
        return new Response<>("200","根据id选择优惠券规则实体已成功",couponRuleMapper.selectById(id));
    }

    //将所有规则分页。
    @ApiOperation(value = "传入current和size两个long类型值，选择所有的优惠券规则对象并进行分页",tags = "分页")
    @GetMapping("/pageAll")
    public IPage<CouponRule> pageAll(@ApiParam(name = "当前页",type = "MybatisPlus提供的Ipage分页中的当前页",required = true)
                                         @RequestParam long current,
                                     @ApiParam(name = "一页的条目数",type = "MybatisPlus提供的Ipage分页中的size参数",required = true)
                                        @RequestParam long size){
        IPage<CouponRule> iPage = new Page<>(current,size);
        return couponRuleMapper.selectPage(iPage,null);
    }

    /** 其他的有关规则的操作 */

    //生成规则与商品的中间表记录
    @ApiOperation(value = "根据传入的优惠券规则id和商品id，生成一条规则实体与商品实体的中间表记录",tags = {"中间表","其他操作"})
    @PostMapping("/relateProduct")
    public Response relate(@ApiParam(name = "优惠券规则id",type = "优惠券规则的主键id",required = true)
                                @RequestParam long ruleid,
                         @ApiParam(name = "商品id",type = "主键id",required = true)
                                @RequestParam long productid){
        CouponRuleToProduct couponRuleToProduct = new CouponRuleToProduct();
        couponRuleToProduct.setProductId(productid);
        couponRuleToProduct.setRuleId(ruleid);
        couponRuleToProductMapper.insert(couponRuleToProduct);
        return new Response("200","规则与商品关联成功，规则id为："+ ruleid + "商品id为:" + productid);
    }

}
