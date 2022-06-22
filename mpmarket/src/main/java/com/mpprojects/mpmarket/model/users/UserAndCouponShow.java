package com.mpprojects.mpmarket.model.users;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 这个类是用来展示用户及其优惠券用的 */
@Data
public class UserAndCouponShow {

    //以下为User类的属性
    private Long userId;
    private BigDecimal money;

    private String uname;
    private String password;
    private String email;
    private String mobile;
    private String description;


    //以下为Coupon类的属性
    private Long couponId;
    private BigDecimal saleoff;          //扣减额度
    private BigDecimal startPrice;       //起用金额（满多少才能减）

    private LocalDateTime createTime;       //创建时间
    private LocalDateTime startTime;        //开始生效时间
    private LocalDateTime endTime;          //截止时间

    private Boolean isVipOnly;
}
