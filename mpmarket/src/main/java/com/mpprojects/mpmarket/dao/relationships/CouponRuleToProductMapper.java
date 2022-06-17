package com.mpprojects.mpmarket.dao.relationships;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponRuleToProductMapper extends BaseMapper<CouponRuleToProduct> {
}
