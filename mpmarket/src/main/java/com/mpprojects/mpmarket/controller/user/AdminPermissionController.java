package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.users.AdminPermissionMapper;
import com.mpprojects.mpmarket.model.users.AdminPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission/admin")
public class AdminPermissionController {

    @Autowired
    private AdminPermissionMapper adminPermissionMapper;

    @PostMapping("/add")
    public String add(@RequestParam String name){
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setName(name);
        adminPermissionMapper.insert(adminPermission);
        return "添加完成："+ adminPermission.getId().toString();
    }
}
