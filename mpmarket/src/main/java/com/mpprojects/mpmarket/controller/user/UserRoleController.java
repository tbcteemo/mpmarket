package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.relationships.UserRoleToPermissionMapper;
import com.mpprojects.mpmarket.dao.users.UserRoleMapper;
import com.mpprojects.mpmarket.model.users.UserRole;
import com.mpprojects.mpmarket.model.users.relationship.UserRoleToPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role/user")
public class UserRoleController {

    @Autowired
    private UserRoleToPermissionMapper userRoleToPermissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /** 这个方法在在添加user的角色role的时候，会要求传入对应的permissionId，同时生成role-permission的关联记录*/

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam Long permissionId){
        //根据传入的name生成admin的role记录；
        UserRole role = new UserRole();
        role.setUserRoleName(name);
        userRoleMapper.insert(role);

        UserRoleToPermission userRoleToPermission = new UserRoleToPermission();
        userRoleToPermission.setUserRoleId(role.getId());
        userRoleToPermission.setUserPermissionId(permissionId);
        userRoleToPermissionMapper.insert(userRoleToPermission);
        return "添加成功" + role.getId().toString();
    }
}
