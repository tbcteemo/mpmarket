package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(value = "订单实体")
@Data
public class UserOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private LocalDateTime createTime;
    private Integer status;     //状态，0关闭，1为成功，2为等待支付；
    private BigDecimal totalPrice;
}
