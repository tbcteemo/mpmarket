package com.mpprojects.mpmarket.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpprojects.mpmarket.dao.relationships.UserCouponMapper;
import com.mpprojects.mpmarket.dao.relationships.UserToRoleMapper;
import com.mpprojects.mpmarket.dao.users.UserMapper;
import com.mpprojects.mpmarket.dao.users.UserRoleMapper;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import com.mpprojects.mpmarket.model.users.User;
import com.mpprojects.mpmarket.model.users.UserRole;
import com.mpprojects.mpmarket.model.users.relationship.UserToRole;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.service.users.impl.UserServiceImpl;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserToRoleMapper userToRoleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @PostMapping("/add")
    public Response addUser(@RequestParam String name,
                            @RequestParam String password,
                            @RequestParam String email,
                            @RequestParam String mobile,
                            @RequestParam String role){

        QueryWrapper<User> queryWrapper1 = new QueryWrapper();
        QueryWrapper<User> queryWrapper2 = new QueryWrapper();
        QueryWrapper<User> queryWrapper3 = new QueryWrapper();

        queryWrapper1.eq("uname",name);

        User nameUser = userMapper.selectOne(queryWrapper1);
        if (nameUser == null){
            queryWrapper2.eq("email",email);
            User emailUser = userMapper.selectOne(queryWrapper2);
            if (emailUser == null){
                queryWrapper3.eq("mobile",mobile);
                User mobileUser = userMapper.selectOne(queryWrapper3);
                if (mobileUser == null){

                    //填充用户的信息
                    User user = new User();
                    user.setUname(name);
                    user.setEmail(email);
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    user.setPassword(encoder.encode(password));
                    user.setMobile(mobile);
                    userMapper.insert(user);


                    //根据传入的role查询到记录
                    QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper();
                    userRoleQueryWrapper.eq("user_role_name",role);
                    UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);

                    //将role与user的信息写到关联表中
                    UserToRole userToRole = new UserToRole();
                    userToRole.setUserId(user.getId());
                    userToRole.setRoleId(userRole.getId());
                    userToRoleMapper.insert(userToRole);

                    return new Response("200","用户创建成功");
                }
                return new Response("1003","mobile已存在");
            }
            return new Response("1003","email已存在");
        }
        return new Response("1003","用户名已存在");
    }

    @DeleteMapping("/delete")
    public Response deleteById(@RequestParam long id){
        User user = userMapper.selectById(id);
        if (user != null) {
            userMapper.deleteById(id);
            return new Response("200","已根据删除指定用户");
        }
        return new Response("1004","此用户不存在");
    }

    @PutMapping("/update")
    public Response updateUser(@RequestBody User user){
        if (user.getMoney() != null){
            return new Response("1002","涉及余额修改，修改非法！");
        }
        userMapper.updateById(user);
        return new Response("200","更改成功");
    }

    @GetMapping("/get")
    public Response<User> getUser(@RequestParam long id){
        User user = userMapper.selectById(id);
        return new Response<>("200","根据id选择User成功",user);
    }

    @GetMapping("/page")
    public IPage<User> pageAll(@RequestParam long current, @RequestParam long size){
        IPage<User> ipage = new Page<>(current,size);
        return userMapper.selectPage(ipage,null);
    }

    /** VIP的金额充值 */
    @PutMapping("/recharge")
    public Response recharge(@RequestParam Long userid,
                            @RequestParam BigDecimal income){
        Boolean isvip = userService.isVip(userid);
        if (isvip == false){
            return new Response("1002","非VIP不允许充值，请使用其他支付方式");
        }
        User oldUser = userMapper.selectById(userid);
        oldUser.setMoney(oldUser.getMoney().add(income));
        userMapper.updateById(oldUser);
        return new Response("200","金额充值成功,余额："+userMapper.selectById(userid).getMoney().toString());
    }

    @PutMapping("/updateselectcoupon")
    public Response updateSelectCoupon(@RequestParam Long userid,
                               @RequestParam Long couponid,
                                @RequestParam Boolean isselect){
        UserCoupon userCoupon = userCouponMapper.selectByUserIdAndCouponId(userid,couponid);
        userCoupon.setIsSelect(isselect);
        userCouponMapper.updateById(userCoupon);
        return new Response("200","此优惠券状态为：" + isselect.toString());
    }

    @GetMapping("/testisvip")
    public boolean isvip(@RequestParam long userid){
       return userService.isVip(userid);
    }

    @GetMapping("/selectviprelation")
    public UserToRole select(@RequestParam long userid){
        return userMapper.selectVipRelation(userid);
    }

}
