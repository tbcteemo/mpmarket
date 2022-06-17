package com.mpprojects.mpmarket.service.users.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.users.AdminMapper;
import com.mpprojects.mpmarket.model.users.Admin;
import com.mpprojects.mpmarket.service.users.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
