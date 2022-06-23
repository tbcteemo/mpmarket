package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data

public class AdminPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}
