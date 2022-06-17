package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.relationships.CartProductMapper;
import com.mpprojects.mpmarket.dao.shop.CartMapper;
import com.mpprojects.mpmarket.dao.users.UserMapper;
import com.mpprojects.mpmarket.model.shop.Cart;
import com.mpprojects.mpmarket.service.shop.CartService;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.service.users.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartProductMapper cartProductMapper;

    @Autowired
    private UserService userService;

    //根据购物车编号进行预结算，得到需要的金额。
    // 在引入优惠券使用规则之后，此方法已被淘汰。
    @Override
    public BigDecimal preCalPrice(Long cartid) {
        BigDecimal x = cartProductMapper.preCalPriceWithProductDiscount(cartid);
        BigDecimal y = cartProductMapper.preCalPriceWithoutProductDiscount(cartid);

        //y2是根据普通用户统一优惠算法得到的优惠值。目前无优惠，为原价
        BigDecimal y2 = userService.settleUser(y);
        BigDecimal z = x.add(y);
        return z;
    }
}
