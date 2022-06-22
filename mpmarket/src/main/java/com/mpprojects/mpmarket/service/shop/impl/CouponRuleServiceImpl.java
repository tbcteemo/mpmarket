package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.shop.CouponRuleMapper;
import com.mpprojects.mpmarket.model.shop.CouponRule;
import com.mpprojects.mpmarket.service.shop.CouponRuleService;
import org.springframework.stereotype.Service;

@Service
public class CouponRuleServiceImpl
        extends ServiceImpl<CouponRuleMapper, CouponRule> implements CouponRuleService {
}
