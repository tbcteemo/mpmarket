package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.relationships.CouponRuleToProductMapper;
import com.mpprojects.mpmarket.model.shop.relationship.CouponRuleToProduct;
import com.mpprojects.mpmarket.service.shop.CouponRuleToProductService;
import org.springframework.stereotype.Service;

@Service
public class CouponRuleToProductServiceImpl
        extends ServiceImpl<CouponRuleToProductMapper,CouponRuleToProduct>
        implements CouponRuleToProductService {
}
