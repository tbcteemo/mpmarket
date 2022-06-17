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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AdminToRoleMapper adminToRoleMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/add")
    public String addAdmin(@RequestParam String name,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String role){

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

                    return "用户创建成功";
            }
            return "email已存在";
        }
        return "用户名已存在";
    }

    @DeleteMapping("/delete")
    public String deleteById(@RequestParam long id){
        Admin admin = adminMapper.selectById(id);
        if (admin != null) {
            adminMapper.deleteById(id);
            return "已删除指定用户";
        }
        return "此用户不存在";
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody Admin admin){
        adminMapper.updateById(admin);
        return "更改成功";
    }

    @GetMapping("/show")
    public Admin showUser(@RequestParam long id){
        Admin admin = adminMapper.selectById(id);
        return admin;
    }

    @GetMapping("/showall")
    public IPage<Admin> showAll(@RequestParam long current, @RequestParam long limit){
        IPage<Admin> ipage = new Page<>(current,limit);
        adminMapper.selectPage(ipage,null);
        return ipage;
    }

    /**
     * 查询所有的vip用户列表，是管理员的权限，所以应该有权限的判定。可以通过spring security的链式编程实现。
     * 同时也因此，管理员要调用这些方法，也是需要先登录的，只要登录成功，那自然就有管理员身份，因此方法体中不需要鉴权。
     */
    @GetMapping("/getAllVip")
    public List<User> getAllVip(){
       return userMapper.selectVipUser();
    }

    /** 这个接口是管理员查看vip列表及其未使用的优惠券 */
    @GetMapping("/getAllVipAndHisUnusedCoupons")
    public List<UserAndCouponShow> getAllVipAndHisUnusedCoupon(){
        return userMapper.selectVipAndHisUnusedCoupon();
    }
}
