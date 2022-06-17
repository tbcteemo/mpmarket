package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cart {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private LocalDateTime lastUpdateTime;

}
