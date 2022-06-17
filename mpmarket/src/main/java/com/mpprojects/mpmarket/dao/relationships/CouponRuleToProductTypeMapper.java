package com.mpprojects.mpmarket.dao.relationships;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProductType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponRuleToProductTypeMapper extends BaseMapper<CouponRuleToProductType> {
}
