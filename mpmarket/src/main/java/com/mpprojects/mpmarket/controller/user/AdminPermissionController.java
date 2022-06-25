package com.mpprojects.mpmarket.controller.user;

import com.mpprojects.mpmarket.dao.users.AdminPermissionMapper;
import com.mpprojects.mpmarket.model.users.AdminPermission;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"管理员系统", "中间表"})
@RestController
@RequestMapping("/permission/admin")
public class AdminPermissionController {

    @Resource
    private AdminPermissionMapper adminPermissionMapper;

    @ApiOperation(value = "传入一个权限的名字，添加一个权限",tags = "添加")
    @PostMapping("/add")
    public Response add(@ApiParam(name = "权限名",value = "权限只有id：自动递增和名字",required = true)
                            @RequestParam String name){
        AdminPermission adminPermission = new AdminPermission();
        adminPermission.setName(name);
        adminPermissionMapper.insert(adminPermission);
        return new Response("200","添加管理员权限完成："+ adminPermission.getId().toString());
    }
}
