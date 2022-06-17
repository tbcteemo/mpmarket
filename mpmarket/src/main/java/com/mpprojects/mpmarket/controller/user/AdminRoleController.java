package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.relationships.AdminRoleToPermissionMapper;
import com.mpprojects.mpmarket.dao.users.AdminRoleMapper;
import com.mpprojects.mpmarket.model.users.AdminRole;
import com.mpprojects.mpmarket.model.users.relationship.AdminRoleToPermission;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** role的添加应该有一个权限认证的过程，此项目无security所以省略了。*/

@RestController
@RequestMapping("/role/admin")
public class AdminRoleController {

    @Autowired
    private AdminRoleToPermissionMapper adminRoleToPermissionMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /** 这个方法在在添加user的角色role的时候，会要求传入对应的permissionId，同时生成role-permission的关联记录*/
    @PostMapping("/add")
    public Response add(@RequestParam String name,
                        @RequestParam Long permissionId){
        //根据传入的name生成admin的role记录；
        AdminRole role = new AdminRole();
        role.setAdminRoleName(name);
        adminRoleMapper.insert(role);

        AdminRoleToPermission adminRoleToPermission = new AdminRoleToPermission();
        adminRoleToPermission.setAdminRoleId(role.getId());
        adminRoleToPermission.setAdminPermissionId(permissionId);
        adminRoleToPermissionMapper.insert(adminRoleToPermission);
        return new Response("200","关联管理员和权限成功" + role.getId().toString());
    }
}
