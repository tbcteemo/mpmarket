package com.mpprojects.mpmarket.model.users.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class AdminRoleToPermission {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long adminRoleId;
    private Long adminPermissionId;
}
