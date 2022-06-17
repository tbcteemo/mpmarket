package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProduct {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private Long productId;
    private Integer productCount;
    private BigDecimal productPrice;
}
