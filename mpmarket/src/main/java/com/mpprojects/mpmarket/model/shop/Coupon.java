package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 优惠券的实体类。如果扣减额度，起用金额，生效时间，截止时间，是否VIP专属这几个属性同时相同，则会被认为是同一个优惠券*/
@Data
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;

    private BigDecimal saleoff;          //扣减额度
    private BigDecimal startPrice;       //起用金额（满多少才能减）

    private LocalDateTime createTime;       //创建时间
    private LocalDateTime startTime;        //开始生效时间
    private LocalDateTime endTime;          //截止时间

    private Boolean isVipOnly;              //是否为VIP专属

    private Long ruleId;        //一张券属于一个规则，一个规则可能包含多个券。
}
