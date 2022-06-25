package com.mpprojects.mpmarket.model.shop;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于向前端传递所需要展示的内容的实体类。非ORM映射到数据库。
 * 其中，最后两项属性，totalPrice和orderTime，是一个订单的属性而非订单详情的属性，但是每条都返回，前端选择一条进行展示即可。
 */

@ApiModel(value = "订单详情展示实体")
@Data
public class OrderShow {

    private String userName;
    private Long userId;

    private Long productId;
    private String productName;

    private Integer productCount;
    private String productPrice;
    private String singlePrice;      //单种商品的汇总价格，即单项价格。

    private String totalPrice;
    private LocalDateTime orderTime;

}