package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "管理员权限实体")
@Data
public class AdminPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}
