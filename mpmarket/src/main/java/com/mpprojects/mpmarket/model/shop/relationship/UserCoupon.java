package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@ApiModel(value = "用户-优惠券中间表")
@Data
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long couponId;

    private Boolean isSelect;
    private Boolean isEnable;

    private LocalDateTime createTime;
    private LocalDateTime useTime;
}
