package com.mpprojects.mpmarket.service.shop;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpprojects.mpmarket.model.shop.Cart;

import java.math.BigDecimal;

public interface CartService extends IService<Cart> {

    BigDecimal preCalPrice(Long orderid);
}
