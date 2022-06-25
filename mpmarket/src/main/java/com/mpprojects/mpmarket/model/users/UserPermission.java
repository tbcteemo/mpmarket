package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "普通用户的权限实体")
@Data
public class UserPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;

}
