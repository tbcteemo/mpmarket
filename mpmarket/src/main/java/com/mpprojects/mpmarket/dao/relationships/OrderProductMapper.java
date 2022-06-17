package com.mpprojects.mpmarket.dao.relationships;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.relationship.OrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderProductMapper extends BaseMapper<OrderProduct> {
    BigDecimal totalPrice(@Param("orderid")Long orderid);
    List<OrderProduct> selectAll(@Param("orderid")Long orderid);
    Integer getProductCount(@Param("orderid") long orderid,
                        @Param("productid") long productid);
}
