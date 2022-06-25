package com.mpprojects.mpmarket.model.users.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "管理员-角色中间表实体")
@Data
public class AdminToRole {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long adminId;
    private Long roleId;


}
