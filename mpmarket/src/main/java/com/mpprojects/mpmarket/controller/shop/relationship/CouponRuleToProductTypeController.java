package com.mpprojects.mpmarket.controller.shop.relationship;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductTypeMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProductType;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = {"优惠券系统", "中间表"})
@RestController
@RequestMapping("/couponRuleToProductType")
public class CouponRuleToProductTypeController {

    @Resource
    private CouponRuleToProductTypeMapper couponRuleToProductTypeMapper;

    @ApiOperation(value = "添加优惠券规则-商品种类的中间表",tags = "添加")
    @PostMapping("/add")
    public Response add(@ApiParam(name = "优惠券规则-商品种类的中间表实体对象",required = true)
                            @RequestBody CouponRuleToProductType couponRuleToProductType){
        couponRuleToProductTypeMapper.insert(couponRuleToProductType);
        return new Response("200",
                "规则与商品类目关联成功，记录id为：" + couponRuleToProductType.getId().toString());
    }

    @ApiOperation(value = "传入id，根据主键id删除优惠券规则-商品种类的中间表记录",tags = "删除")
    @DeleteMapping("/delete")
    public Response add(@ApiParam(name = "中间表对象的id",value = "主键id",required = true)
                            @RequestParam long id){
        couponRuleToProductTypeMapper.deleteById(id);
        return new Response("200","根据id删除成功");
    }

    @ApiOperation(value = "修改优惠券规则-商品种类的中间表",tags = "修改")
    @PutMapping("/update")
    public Response update(@ApiParam(name = "优惠券规则-商品种类的中间表实体对象",required = true)
                               @RequestBody CouponRuleToProductType couponRuleToProductType){
        couponRuleToProductTypeMapper.updateById(couponRuleToProductType);
        return new Response("200","修改成功，该条关联表记录id为" + couponRuleToProductType.getId().toString());
    }

    @ApiOperation(value = "根据传入的id获取：优惠券规则-商品种类的中间表对象",tags = "获取")
    @GetMapping("/getOne")
    public Response<CouponRuleToProductType> getone(@ApiParam(name = "中间表对象的id",value = "主键id",required = true)
                                                        @RequestParam long id){
        return new Response<>("200",
                "根据主键id选择优惠券规则与商品的关联记录成功",
                couponRuleToProductTypeMapper.selectById(id));
    }

    @ApiOperation(value = "传入分页参数，将所有的优惠券规则-商品种类中间表对象以分页的形式返回",tags = "分页")
    @GetMapping("/pageAll")
    public IPage<CouponRuleToProductType> pageAll(@RequestParam long current,
                                                  @RequestParam long size){
        IPage iPage  = new Page(current,size);
        return couponRuleToProductTypeMapper.selectPage(iPage,null);
    }
}
