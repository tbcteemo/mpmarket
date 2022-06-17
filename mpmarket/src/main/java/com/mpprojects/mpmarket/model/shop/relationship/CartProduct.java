package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sun.istack.internal.NotNull;
import lombok.Data;

@Data
public class CartProduct {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long cartId;
    private Long productId;
    private Integer productCount;
    private Boolean isSelected;

}
