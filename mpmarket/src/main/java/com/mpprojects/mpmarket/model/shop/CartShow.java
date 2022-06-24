package com.mpprojects.mpmarket.model.shop;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/** 用于接收sql多表联查的数据，发送给前端展示购物车详情的类，非数据库实体
 * 这样做的好处就是，要是以后要展示的内容变化了，直接新建一个展示的类接收数据，改一下sql语句的参数就好，不影响整体。
 * */
@ApiModel(value = "购物车详情展示实体")
@Data
public class CartShow {

    private String pname;
    private Long formatId;
    private String formatValue;

    private String unit;
    private Integer productCount;
    private BigDecimal price;
    private BigDecimal stPrice;
}
