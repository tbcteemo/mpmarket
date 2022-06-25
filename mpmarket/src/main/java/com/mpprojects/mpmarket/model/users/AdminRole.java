package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "管理员系统用的角色实体类")
@Data
public class AdminRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String adminRoleName;
}
