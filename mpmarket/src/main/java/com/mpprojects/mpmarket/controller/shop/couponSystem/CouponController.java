package com.mpprojects.mpmarket.controller.shop.couponSystem;

import com.mpprojects.mpmarket.dao.relationships.UserCouponMapper;
import com.mpprojects.mpmarket.dao.shop.CouponMapper;
import com.mpprojects.mpmarket.model.shop.Coupon;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = "优惠券系统")
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserCouponMapper userCouponMapper;

//    创建一个优惠券实体。
    @ApiOperation(value = "添加优惠券",tags = "添加")
    @PostMapping("/add")
    public Response add(@ApiParam(type = "优惠券实体",required = true)
                            @RequestBody Coupon coupon){

//        判定是否为相同的优惠券
        Coupon coupon1 = couponMapper.selectSameCoupon(coupon.getSaleoff(),
                coupon.getStartPrice(),
                coupon.getStartTime(),
                coupon.getEndTime(),
                coupon.getIsVipOnly(),
                coupon.getRuleId());

        if (coupon1 != null){
            return new Response("1003","该优惠券已存在");
        }
        couponMapper.insert(coupon);
        return new Response("200","添加优惠券成功");
    }

    //删除一条优惠券记录
    @ApiOperation(value = "删除优惠券",tags = "删除")
    @DeleteMapping("/delete")
    public Response delete(@ApiParam(type = "优惠券id",required = true)
                               @RequestParam Long id){
        couponMapper.deleteById(id);
        return new Response("200","删除优惠券成功");
    }

    //修改一条优惠券
    @ApiOperation(value = "修改优惠券",tags = "修改")
    @PutMapping("/put")
    public Response update(@ApiParam(type = "优惠券实体",required = true)
                               @RequestBody Coupon coupon){
        Coupon coupon1 = couponMapper.selectById(coupon.getId());
        if (coupon1 == null){
            return new Response("1002","id不存在或优惠券记录不存在，请检查id或者转到添加页面");
        }
        couponMapper.updateById(coupon);
        return new Response("200","修改优惠券内容成功，优惠券id为："+coupon.getId().toString());
    }

    //获取一条优惠券记录
    @ApiOperation(value = "获取一个优惠券",tags = "获取")
    @GetMapping("/get")
    public Response<Coupon> get(@ApiParam(type = "优惠券id",required = true)@RequestParam Long id){
        return new Response<>("200","根据id获取优惠券对象成功",couponMapper.selectById(id));
    }

    //下发优惠券
    @ApiOperation(value = "发放优惠券（不分角色）",tags = {"功能性操作"})
    @PostMapping("/givecoupon")
    public Response<UserCoupon> givecoupon(@ApiParam(type = "用户id",required = true)
                                               @RequestParam Long userid,
                               @ApiParam(type = "优惠券id",required = true)
                               @RequestParam Long couponid){
        Coupon coupon = couponMapper.selectById(couponid);
        UserCoupon userCoupon = new UserCoupon();
        if (coupon.getIsVipOnly() == true){
            Boolean isVip = userService.isVip(userid);
            if (isVip == false){
                return new Response("1002","此优惠券为vip专属，用户角色不匹配！",null);
            }else{
                userCoupon.setUserId(userid);
                userCoupon.setCouponId(couponid);
                userCoupon.setIsEnable(true);
                userCouponMapper.insert(userCoupon);
                return new Response("200","优惠券发放成功",userCoupon);
            }
        }else {
            userCoupon.setUserId(userid);
            userCoupon.setCouponId(couponid);
            userCoupon.setIsEnable(true);
            userCouponMapper.insert(userCoupon);
            return new Response("200","优惠券发放成功",userCoupon);
        }
    }


}
