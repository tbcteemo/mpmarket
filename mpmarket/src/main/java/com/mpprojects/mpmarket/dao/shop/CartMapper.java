package com.mpprojects.mpmarket.dao.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.Cart;
import com.mpprojects.mpmarket.model.shop.CartShow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CartMapper extends BaseMapper<Cart> {
    List<CartShow> showDetails(Long userid);
}