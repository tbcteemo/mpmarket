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
import com.mpprojects.mpmarket.service.users.AdminService;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;


@Api(tags = {"用户系统", "基础功能"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserToRoleMapper userToRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserCouponMapper userCouponMapper;

    @ApiOperation(value = "根据传入重要属性，新建一个User到数据库中",tags = "添加")
    @PostMapping("/add")
    public Response addUser(@ApiParam(type = "用户名",required = true) @RequestParam String name,
                            @ApiParam(type = "用户密码",required = true) @RequestParam String password,
                            @ApiParam(type = "用户email",required = true) @RequestParam String email,
                            @ApiParam(type = "用户手机号",required = true) @RequestParam String mobile,
                            @ApiParam(type = "用户角色",required = true) @RequestParam String role){

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

    @ApiOperation(value = "根据传入的id删除对应的用户",tags = "删除")
    @DeleteMapping("/delete")
    public Response deleteById(@ApiParam(type = "用户id",value = "主键id",required = true)
                                    @RequestParam long id){
        User user = userMapper.selectById(id);
        if (user != null) {
            userMapper.deleteById(id);
            return new Response("200","已根据删除指定用户");
        }
        return new Response("1004","此用户不存在");
    }

    @ApiOperation(value = "传入User对象，将其更新到数据库中",tags = "修改")
    @PutMapping("/update")
    public Response updateUser(@ApiParam(type = "用户对象",required = true)
                                    @RequestBody User user){
        if (user.getMoney() != null){
            return new Response("1002","涉及余额修改，修改非法！");
        }
        userMapper.updateById(user);
        return new Response("200","更改成功");
    }

    @ApiOperation(value = "根据传入的id获得对应的用户对象",tags = "获取")
    @GetMapping("/get")
    public Response<User> getUser(@ApiParam(type = "用户id",value = "主键id",required = true)
                                      @RequestParam long id){
        User user = userMapper.selectById(id);
        return new Response<>("200","根据id选择User成功",user);
    }

    @ApiOperation(value = "根据分页参数，将所有用户分页返回",tags = "分页")
    @GetMapping("/page")
    public IPage<User> pageAll(@ApiParam(type = "当前页码",value = "MybatisPlus的Ipage插件的current参数",required = true)
                                    @RequestParam long current,
                               @ApiParam(type = "每页条数",value = "MybatisPlus的Ipage插件的size参数",required = true)
                                    @RequestParam long size){
        IPage<User> ipage = new Page<>(current,size);
        return userMapper.selectPage(ipage,null);
    }

    /** VIP的金额充值 */
    @ApiOperation(value = "给用户充值金额",notes = "默认只允许vip就行充值，非vip用户充值会报错",tags = "功能性操作")
    @PutMapping("/recharge")
    public Response recharge(@ApiParam(type = "用户id",value = "主键id",required = true)
                                 @RequestParam Long userid,
                            @ApiParam(type = "充值金额",value = "充值金额，只接受BigDecimal类型",required = true)
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

    @ApiOperation(value = "更新一个优惠券的被选择状态",notes = "一个优惠券只有在被选中状态下，才会被提交结算",tags = "功能性操作")
    @PutMapping("/updateselectcoupon")
    public Response updateSelectCoupon(@ApiParam(type = "用户id",value = "主键id",required = true)
                                            @RequestParam Long userid,
                                       @ApiParam(type = "优惠券id",value = "主键id",required = true)
                                            @RequestParam Long couponid,
                                       @ApiParam(type = "是否被选择",value = "修改User-Coupon中间表中对应记录的isSelect属性",required = true)
                                            @RequestParam Boolean isselect){
        UserCoupon userCoupon = userCouponMapper.selectByUserIdAndCouponId(userid,couponid);
        userCoupon.setIsSelect(isselect);
        userCouponMapper.updateById(userCoupon);
        return new Response("200","此优惠券状态为：" + isselect.toString());
    }

    /** 根据传入的userid，选择其在user-role关系表中为vip的记录。判断是否为VIP用。如果返回为null，则不是VIP，返回具体对象，则是VIP */
    @ApiOperation(value = "根据传入的userid，选择其在user-role关系表中为vip的记录",
            notes = "判断是否为VIP用。如果返回为null，则不是VIP，返回具体对象，则是VIP",tags = "功能性操作")
    @GetMapping("/selectviprelation")
    public UserToRole select(@ApiParam(type = "用户id",value = "主键id",required = true)
                                 @RequestParam long userid){
        return userMapper.selectVipRelation(userid);
    }

}
