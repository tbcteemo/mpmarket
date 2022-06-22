package com.mpprojects.mpmarket.dao.relationships;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface UserCouponMapper extends BaseMapper<UserCoupon> {
    UserCoupon isUseOnlyOne(Long userid);
    UserCoupon selectOneByUserId(@Param("userid") Long userid);
    UserCoupon selectByUserIdAndCouponId(@Param("userid")Long userid,
                                         @Param("couponid")Long couponid);
}
