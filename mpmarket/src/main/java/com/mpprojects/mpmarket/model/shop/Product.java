package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long sn;        //产品标识码，如条码等。
    private String pname;   //产品名
    private String description;     //描述
    private BigDecimal price;    //价格
    private String unit;   //计量单位，个，盒，只等
    private String formatId;  //规格，如250ml，200g等
    private Integer stock;  //库存数量

    private LocalDateTime lastUpdateTime;

    //以下的两个属性为后续增加的
    private Integer discount;       //商品的特有折扣，默认值为0。当有值时，使用此折扣；当值为0时，使用会员统一折扣。
    private Long typeId;        //商品的类别id
}
