package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductTypeMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProductType;
import com.mpprojects.mpmarket.service.shop.CouponRuleToProductTypeService;
import org.springframework.stereotype.Service;

@Service
public class CouponRuleToProductTypeServiceImpl
        extends ServiceImpl<CouponRuleToProductTypeMapper, CouponRuleToProductType>
        implements CouponRuleToProductTypeService {
}
