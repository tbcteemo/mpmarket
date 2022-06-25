package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "优惠券规则-商品种类的中间表实体")
@Data
public class CouponRuleToProductType {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long ruleId;
    private Long TypeId;
}
