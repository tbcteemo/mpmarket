package com.mpprojects.mpmarket.controller.shop;

import com.mpprojects.mpmarket.dao.relationships.UserCouponMapper;
import com.mpprojects.mpmarket.dao.shop.CouponMapper;
import com.mpprojects.mpmarket.model.shop.Coupon;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import com.mpprojects.mpmarket.service.shop.CouponService;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private CouponService couponService;

    @Resource
    private UserService userService;

    @Resource
    private UserCouponMapper userCouponMapper;

//    创建一个优惠券实体。
    @PostMapping("/add")
    public Response add(@RequestBody Coupon coupon){

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
    @DeleteMapping("/delete")
    public Response delete(@RequestParam Long id){
        couponMapper.deleteById(id);
        return new Response("200","删除优惠券成功");
    }

    //修改一条优惠券
    @PutMapping("/put")
    public Response update(@RequestBody Coupon coupon){
        Coupon coupon1 = couponMapper.selectById(coupon.getId());
        if (coupon1 == null){
            return new Response("1002","id不存在或优惠券记录不存在，请检查id或者转到添加页面");
        }
        couponMapper.updateById(coupon);
        return new Response("200","修改优惠券内容成功，优惠券id为："+coupon.getId().toString());
    }

    //获取一条优惠券记录
    @GetMapping("/get")
    public Response<Coupon> get(@RequestParam Long id){
        return new Response<>("200","根据id获取优惠券对象成功",couponMapper.selectById(id));
    }

    //下发优惠券
    @PostMapping("/givecoupon")
    public Response<UserCoupon> givecoupon(@RequestParam Long userid,
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
