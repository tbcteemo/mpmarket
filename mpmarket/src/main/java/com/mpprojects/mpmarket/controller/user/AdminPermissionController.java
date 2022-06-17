package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.users.AdminPermissionMapper;
import com.mpprojects.mpmarket.model.users.AdminPermission;
import com.mpprojects.mpmarket.utils.Response;
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
    public Response add(@RequestParam String name){
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setName(name);
        adminPermissionMapper.insert(adminPermission);
        return new Response("200","添加管理员权限完成："+ adminPermission.getId().toString());
    }
}
