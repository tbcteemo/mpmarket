package com.mpprojects.mpmarket.dao.relationships;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.CartShow;
import com.mpprojects.mpmarket.model.shop.relationship.CartProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CartProductMapper extends BaseMapper<CartProduct> {

//    此方法已因为增加"商品单独折扣"的原因废弃
    BigDecimal preCal(@Param("cartid")Long cartid);

    List<CartProduct> getSelectedProducts(@Param("userid") Long userid);
    List<CartProduct> selectAll(@Param("cartid") Long cartid);
    void deleteSelected(@Param("cartid") Long cartid);
    List<CartShow> showDetails(@Param("userid")Long userid);

    //传入购物车id，算出无商品特殊折扣的商品总价x。一个订单的总价z = x + y。
    BigDecimal preCalPriceWithoutProductDiscount(Long cartid);

    //传入购物车id，算出有商品特殊折扣的商品总价y。一个订单的总价z = x + y。
    BigDecimal preCalPriceWithProductDiscount(Long cartid);
}
