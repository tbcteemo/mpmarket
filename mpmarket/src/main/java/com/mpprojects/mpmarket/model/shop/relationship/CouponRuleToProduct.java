package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "优惠券规则-商品的中间表实体类")
@Data
public class CouponRuleToProduct {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ruleId;
    private Long productId;
}
