package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sun.istack.internal.NotNull;
import lombok.Data;

@Data

public class AdminRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String adminRoleName;
}
