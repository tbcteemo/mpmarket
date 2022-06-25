package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.relationships.UserRoleToPermissionMapper;
import com.mpprojects.mpmarket.dao.users.UserRoleMapper;
import com.mpprojects.mpmarket.model.users.UserRole;
import com.mpprojects.mpmarket.model.users.relationship.UserRoleToPermission;
import com.mpprojects.mpmarket.utils.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"用户系统", "基础功能"})
@RestController
@RequestMapping("/role/user")
public class UserRoleController {

    @Resource
    private UserRoleToPermissionMapper userRoleToPermissionMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /** 这个方法在在添加user的角色role的时候，会要求传入对应的permissionId，同时生成role-permission的关联记录*/

    @ApiOperation(value = "添加一个角色，角色必须指定一个权限",tags = "添加")
    @PostMapping("/add")
    public Response add(@ApiParam(name = "角色名",required = true)
                            @RequestParam String name,
                      @ApiParam(name = "权限id",required = true)
                            @RequestParam Long permissionId){
        //根据传入的name生成admin的role记录；
        UserRole role = new UserRole();
        role.setUserRoleName(name);
        userRoleMapper.insert(role);

        UserRoleToPermission userRoleToPermission = new UserRoleToPermission();
        userRoleToPermission.setUserRoleId(role.getId());
        userRoleToPermission.setUserPermissionId(permissionId);
        userRoleToPermissionMapper.insert(userRoleToPermission);
        return new Response("200","添加角色成功" + role.getId().toString());
    }
}
