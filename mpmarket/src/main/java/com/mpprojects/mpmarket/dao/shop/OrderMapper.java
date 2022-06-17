package com.mpprojects.mpmarket.dao.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.OrderShow;
import com.mpprojects.mpmarket.model.shop.Product;
import com.mpprojects.mpmarket.model.shop.UserOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<UserOrder> {

    /** 此方法是根据传入的用户ID，将当前用户的所有订单都返回。*/
    List<OrderShow> listAllOrders(Long userid);

    /** 此方法是根据订单号，传回这一个订单的详情。*/
    List<OrderShow> listOneOrder(Long orderid);

    BigDecimal priceOfNoProductDiscount(Long orderid);
    BigDecimal priceOfVipHaveProductDiscount(Long orderid);
    BigDecimal priceOfUserHaveProductDiscount(Long orderid);

    /**
     * 在优惠券规则为使用名单的时候，分类获取product列表的方法，共4种。
     * 依次分别为：
     * 1.在名单中，有单独折扣的商品；
     * 2.在名单中，无单独折扣的商品；
     * 3.不在名单中，有单独折扣的商品；
     * 4.不在名单中，无单独折扣的商品。
     */
    List<Product> getProductsInListWithDiscount(@Param("orderid")long orderid,
                                                @Param("ruleid") long ruleid);
    List<Product> getProductsInListWithoutDiscount(@Param("orderid")long orderid,
                                                @Param("ruleid") long ruleid);
    List<Product> getProductsNotInListWithDiscount(@Param("orderid")long orderid,
                                                @Param("ruleid") long ruleid);
    List<Product> getProductsNotInListWithoutDiscount(@Param("orderid")long orderid,
                                                @Param("ruleid") long ruleid);

    /**
     * 下面的方法是用于按品类使用规则的。规则码：4
     */
    List<Product> getProductsInTypeWithDiscount(@Param("orderid")long orderid,
                                                @Param("rule_id")long ruleid);
    List<Product> getProductsInTypeWithoutDiscount(@Param("orderid")long orderid,
                                                   @Param("rule_id")long ruleid);
    List<Product> getProductsNotInTypeWithDiscount(@Param("orderid")long orderid,
                                                   @Param("rule_id")long ruleid);
    List<Product> getProductsNotInTypeWithoutDiscount(@Param("orderid")long orderid,
                                                      @Param("rule_id")long ruleid);
}
