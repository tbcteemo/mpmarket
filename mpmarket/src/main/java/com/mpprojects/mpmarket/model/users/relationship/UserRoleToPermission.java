package com.mpprojects.mpmarket.model.users.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "普通用户系统的角色-权限中间表实体")
@Data
public class UserRoleToPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userRoleId;
    private Long userPermissionId;

}
