package com.mpprojects.mpmarket.model.users;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private BigDecimal money;

    private String uname;
    private String password;
    private String email;
    private String mobile;
    private String description;

}
