package com.mpprojects.mpmarket.dao.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

    Coupon selectSameCoupon(@Param("saleoff") BigDecimal saleoff,
                             @Param("startPrice") BigDecimal startPrice,
                             @Param("startTime") LocalDateTime startTime,
                             @Param("endTime") LocalDateTime endTime,
                             @Param("isVipOnly") Boolean isVipOnly);

    Coupon getSelectedCoupon(@Param("userid") Long userid);

}
