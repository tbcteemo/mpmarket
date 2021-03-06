package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 此类是优惠券适用范围的规则类。
 * 规则有4种，ruleNumber对应为：1,2,3,4
 * 规则1：白名单规则：List中的所有商品才可以使用；
 * 规则2：黑名单规则：List中的商品不可以使用；
 * 规则3：全局可用；
 * 规则4：仅制定类别的商品可以使用。此时需要结合product类中的typeNumber进行判定。
 *
 * 对应的商品List：采用中间表的形式。Rule与Product是多对多的关系。
 */
@ApiModel(value = "优惠券规则实体",description = "优惠券规则的实体类，有4种规则，分别是：白名单，黑名单，全局，指定类目可用，ruleNumber对应为 1，2，3，4")
@Data
public class CouponRule {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String ruleName;        //规则的名称
    @ApiModelProperty(value = "优惠券规则的编号，有4种规则，分别是：白名单，黑名单，全局，指定类目可用，ruleNumber对应为 1，2，3，4")
    private Integer ruleNumber;        //规则类型的编号，见上面。
    private String description;
}
