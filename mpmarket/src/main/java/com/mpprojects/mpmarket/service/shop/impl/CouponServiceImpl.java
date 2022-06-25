package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.relationships.UserCouponMapper;
import com.mpprojects.mpmarket.dao.shop.CouponMapper;
import com.mpprojects.mpmarket.model.shop.Coupon;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import com.mpprojects.mpmarket.service.shop.CouponService;
import com.mpprojects.mpmarket.utils.Response;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private UserCouponMapper userCouponMapper;

    @Override
    public boolean couponIsSame(Coupon coupon) {
        //判定是否为相同的优惠券
        Coupon coupon1 = couponMapper.selectSameCoupon(coupon.getSaleoff(),
                coupon.getStartPrice(),
                coupon.getStartTime(),
                coupon.getEndTime(),
                coupon.getIsVipOnly(),
                coupon.getRuleId());

        if (coupon1 != null){
            return true;
        }else {
            couponMapper.insert(coupon);
            return false;
        }
    }

    @Override
    public Response<UserCoupon> giveCoupon(Long userid, Coupon coupon) {
        boolean isSame = couponIsSame(coupon);
        if (isSame == true){
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserId(userid);
            userCoupon.setCouponId(coupon.getId());
            userCoupon.setIsEnable(true);
            userCoupon.setCreateTime(LocalDateTime.now());
            userCouponMapper.insert(userCoupon);
            return new Response("200","已有相同优惠券，且优惠券发放成功,关联表对象为：",userCoupon);
        }else{
            couponMapper.insert(coupon);
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setCouponId(coupon.getId());
            userCoupon.setUserId(userid);
            userCoupon.setIsEnable(true);
            userCoupon.setCreateTime(LocalDateTime.now());
            userCouponMapper.insert(userCoupon);
            return new Response("200","创建优惠券且发放成功,关联表对象为：",userCoupon);
        }
    }


}
