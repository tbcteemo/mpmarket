package com.mpprojects.mpmarket.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.AdminToRoleMapper;
import com.mpprojects.mpmarket.dao.users.AdminMapper;
import com.mpprojects.mpmarket.dao.users.AdminRoleMapper;
import com.mpprojects.mpmarket.dao.users.UserMapper;
import com.mpprojects.mpmarket.model.users.Admin;
import com.mpprojects.mpmarket.model.users.AdminRole;
import com.mpprojects.mpmarket.model.users.User;
import com.mpprojects.mpmarket.model.users.UserAndCouponShow;
import com.mpprojects.mpmarket.model.users.relationship.AdminToRole;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "管理员系统")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Resource
    private AdminToRoleMapper adminToRoleMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private UserMapper userMapper;

    @ApiOperation(value = "通过传入关键属性添加一个管理员",notes = "关键参数依次为：name，password，email，role",tags = "添加")
    @PostMapping("/add")
    public Response addAdmin(@ApiParam(type = "管理员用户名",required = true) @RequestParam String name,
                             @ApiParam(type = "管理员密码",required = true) @RequestParam String password,
                             @ApiParam(type = "管理员邮箱",required = true) @RequestParam String email,
                             @ApiParam(type = "管理员角色",required = true) @RequestParam String role){

        QueryWrapper<Admin> queryWrapper1 = new QueryWrapper();
        QueryWrapper<Admin> queryWrapper2 = new QueryWrapper();

        queryWrapper1.eq("name",name);

        Admin nameAdmin = adminMapper.selectOne(queryWrapper1);
        if (nameAdmin == null){
            queryWrapper2.eq("email",email);
            Admin emailAdmin = adminMapper.selectOne(queryWrapper2);
            if (emailAdmin == null){

                    //填充用户的信息
                    Admin admin = new Admin();
                    admin.setName(name);
                    admin.setEmail(email);
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    admin.setPassword(encoder.encode(password));
                    admin.setEnable(true);
                    adminMapper.insert(admin);


                    //根据传入的role查询到记录
                    QueryWrapper<AdminRole> adminRoleQueryWrapper = new QueryWrapper();
                    adminRoleQueryWrapper.eq("admin_role_name",role);
                    AdminRole adminRole = adminRoleMapper.selectOne(adminRoleQueryWrapper);

                    //将role与user的信息写到关联表中
                    AdminToRole adminToRole = new AdminToRole();
                    adminToRole.setAdminId(admin.getId());
                    adminToRole.setRoleId(adminRole.getId());
                    adminToRoleMapper.insert(adminToRole);

                    return new Response("200","用户创建成功");
            }
            return new Response("1002","email已存在");
        }
        return new Response("1002","用户名已存在");
    }

    @ApiOperation(value = "传入主键id删除对应的管理员用户",tags = "删除")
    @DeleteMapping("/delete")
    public Response deleteById(@ApiParam(type = "管理员id",value = "主键id",required = true)
                                   @RequestParam long id){
        Admin admin = adminMapper.selectById(id);
        if (admin != null) {
            adminMapper.deleteById(id);
            return new Response("200","已删除指定用户");
        }
        return new Response("1004","此用户不存在");
    }

    @ApiOperation(value = "传入管理员对象，修改数据库对应的内容",tags = "修改")
    @PutMapping("/update")
    public Response updateUser(@ApiParam(type = "管理员对象",required = true)
                                   @RequestBody Admin admin){
        adminMapper.updateById(admin);
        return new Response("200","更改成功");
    }

    @ApiOperation(value = "传入主键id获取对应的管理员用户",tags = "获取")
    @GetMapping("/show")
    public Response<Admin> showUser(@ApiParam(type = "管理员id",value = "主键id",required = true)
                                        @RequestParam long id){
        Admin admin = adminMapper.selectById(id);
        return new Response<>("200","根据id选取管理员成功",admin);
    }

    @ApiOperation(value = "传入分页参数对所有管理员用户进行分页操作",tags = "分页")
    @GetMapping("/showall")
    public IPage<Admin> showAll(@ApiParam(type = "当前页码",required = true)
                                    @RequestParam long current,
                                @ApiParam(type = "每页条数",required = true)
                                    @RequestParam long limit){
        IPage<Admin> ipage = new Page<>(current,limit);
        adminMapper.selectPage(ipage,null);
        return ipage;
    }

    /**
     * 查询所有的vip用户列表，是管理员的权限，所以应该有权限的判定。可以通过spring security的链式编程实现。
     * 同时也因此，管理员要调用这些方法，也是需要先登录的，只要登录成功，那自然就有管理员身份，因此方法体中不需要鉴权。
     */
    @ApiOperation(value = "将所有的vip用户以列表List形式返回，无需传参",tags = "获取")
    @GetMapping("/getAllVip")
    public List<User> getAllVip(){
       return userMapper.selectVipUser();
    }

    /** 这个接口是管理员查看vip列表及其未使用的优惠券 */
    @ApiOperation(value = "将所有的vip用户及其未使用的优惠券，以列表List形式返回，无需传参",tags = "获取")
    @GetMapping("/getAllVipAndHisUnusedCoupons")
    public List<UserAndCouponShow> getAllVipAndHisUnusedCoupon(){
        return userMapper.selectVipAndHisUnusedCoupon();
    }
}
