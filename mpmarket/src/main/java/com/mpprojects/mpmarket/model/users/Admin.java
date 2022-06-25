package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "管理员实体")
@Data
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String password;
    private String email;
    private boolean isEnable;
}
