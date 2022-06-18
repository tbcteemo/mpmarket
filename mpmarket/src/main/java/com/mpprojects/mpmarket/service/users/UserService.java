package com.mpprojects.mpmarket.service.users;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpprojects.mpmarket.model.users.User;

import java.math.BigDecimal;

public interface UserService extends IService<User> {
    Boolean isVip(Long userid);
    BigDecimal settleVip(BigDecimal tp);
    BigDecimal settleUser(BigDecimal tp);
}
