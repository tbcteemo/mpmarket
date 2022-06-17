package com.mpprojects.mpmarket.service.shop;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mpprojects.mpmarket.model.shop.Coupon;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import com.mpprojects.mpmarket.utils.Response;

public interface CouponService extends IService<Coupon> {
    public boolean couponIsSame(Coupon coupon);
    public Response<UserCoupon> giveCoupon(Long userid, Coupon coupon);
}
