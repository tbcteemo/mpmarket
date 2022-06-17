package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.users.UserPermissionMapper;
import com.mpprojects.mpmarket.model.users.UserPermission;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission/user")
public class UserPermissionController {

    @Autowired
    private UserPermissionMapper userPermissionMapper;

    @PostMapping("/add")
    public Response add(@RequestParam String name){
        UserPermission userPermission = new UserPermission();
        userPermission.setName(name);
        userPermissionMapper.insert(userPermission);
        return new Response("200","用户权限添加完成："+ userPermission.getId().toString());
    }
}
