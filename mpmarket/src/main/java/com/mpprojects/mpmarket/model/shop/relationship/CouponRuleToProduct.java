package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CouponRuleToProduct {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ruleId;
    private Long productId;
}
