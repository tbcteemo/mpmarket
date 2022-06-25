package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "普通用户系统的角色实体")
@Data
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userRoleName;

}
