package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.users.UserPermissionMapper;
import com.mpprojects.mpmarket.model.users.UserPermission;
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
@RequestMapping("/permission/user")
public class UserPermissionController {

    @Resource
    private UserPermissionMapper userPermissionMapper;

    @ApiOperation(value = "添加一个普通用户的权限记录",tags = "添加")
    @PostMapping("/add")
    public Response add(@ApiParam(name = "权限名",required = true)
                            @RequestParam String name){
        UserPermission userPermission = new UserPermission();
        userPermission.setName(name);
        userPermissionMapper.insert(userPermission);
        return new Response("200","用户权限添加完成："+ userPermission.getId().toString());
    }
}
