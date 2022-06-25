package com.mpprojects.mpmarket.model.users.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "用户-权限的中间表实体")
@Data
public class UserToRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long roleId;

}
