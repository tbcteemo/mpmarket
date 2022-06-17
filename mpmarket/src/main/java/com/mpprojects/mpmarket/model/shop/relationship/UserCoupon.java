package com.mpprojects.mpmarket.model.shop.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

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
