package com.mpprojects.mpmarket.service.users.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.users.UserMapper;
import com.mpprojects.mpmarket.model.users.User;
import com.mpprojects.mpmarket.model.users.relationship.UserToRole;
import com.mpprojects.mpmarket.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean isVip(Long userid) {
        UserToRole role = userMapper.selectVipRelation(userid);
        if (role == null){
            return Boolean.FALSE;
        }else {
            return Boolean.TRUE;
        }
    }

    //方法settleVip是VIP的打折算法，输入原价，输出打折后的会员价。
    @Override
    public BigDecimal settleVip(BigDecimal tp) {
        BigDecimal multiplier = new BigDecimal("0.8");  //折扣率乘数
        BigDecimal p = tp.multiply(multiplier);
        return p;
    }

    //方法settleUser是针对普通用户的统一优惠算法。输入原价，输出优惠后价格。
    @Override
    public BigDecimal settleUser(BigDecimal tp) {
        BigDecimal p = tp;
        return p;
    }
}
