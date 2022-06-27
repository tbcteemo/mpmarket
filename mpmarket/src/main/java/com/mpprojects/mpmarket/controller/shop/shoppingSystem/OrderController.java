package com.mpprojects.mpmarket.controller.shop.shoppingSystem;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.mpprojects.mpmarket.dao.relationships.CartProductMapper;
import com.mpprojects.mpmarket.dao.relationships.OrderProductMapper;
import com.mpprojects.mpmarket.dao.shop.CartMapper;
import com.mpprojects.mpmarket.dao.shop.CouponMapper;
import com.mpprojects.mpmarket.dao.shop.CouponRuleMapper;
import com.mpprojects.mpmarket.dao.shop.OrderMapper;
import com.mpprojects.mpmarket.dao.users.UserMapper;
import com.mpprojects.mpmarket.model.shop.Cart;
import com.mpprojects.mpmarket.model.shop.CouponRule;
import com.mpprojects.mpmarket.model.shop.OrderShow;
import com.mpprojects.mpmarket.model.shop.UserOrder;
import com.mpprojects.mpmarket.model.users.User;
import com.mpprojects.mpmarket.service.shop.OrderService;
import com.mpprojects.mpmarket.service.shop.impl.OrderServiceImpl;
import com.mpprojects.mpmarket.service.shop.impl.OrderServiceImpl;


import com.mpprojects.mpmarket.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"购物系统", "订单系统"})
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private CouponRuleMapper couponRuleMapper;

    @ApiOperation(value = "传入用户id，自动获取其持有的优惠券、角色等优惠信息，创建一个订单",
            notes = "加入优惠券系统后已经废弃！禁止使用！请使用统一结算接口！",
            tags = {"添加", "已废弃方法"})
    @PostMapping("/add")
    public Response createOrder(@ApiParam(type = "用户id",value = "主键id",required = true)
                                    @RequestParam Long userId){
        return orderService.createOrder(userId);
    }

    @ApiOperation(value = "传入用户id和优惠券id，创建一个订单。自动识别用户身份采用不同的算法。",
            notes = "加入优惠券系统后已经废弃！禁止使用！请使用统一结算接口！",tags = {"添加", "已废弃方法"})
    @PostMapping("/add2")
    public Response createOrder2(@ApiParam(type = "用户id",value = "主键id",required = true)
                                     @RequestParam Long userid,
                               @ApiParam(type = "优惠券id",value = "主键id",required = true)
                                    @RequestParam Long couponid){
        return orderService.createOrder2(userid, couponid);
    }

    @ApiOperation(value = "传入用户id，不使用优惠券时，创建一个订单。自动识别用户身份采用不同的算法。",
            notes = "加入优惠券系统后已经废弃！禁止使用！请使用统一结算接口！",tags = {"添加", "已废弃方法"})
    @PostMapping("/add3")
    public Response createOrder3(@RequestParam Long userid){
        return orderService.createOrder3(userid);
    }

    /** 这是直接返回订单实体的入口 */
    @ApiOperation(value = "传入用户id和分页参数，将此用户的所有订单以分页形式返回",tags = "分页")
    @GetMapping("/page")
    public IPage<UserOrder> page(@ApiParam(type = "用户id",value = "主键id",required = true)
                                        @RequestParam long userId,
                                 @ApiParam(type = "MyBatisPlus的Ipage分页插件的current值",value = "当前页",required = true)
                                        @RequestParam long current,
                                 @ApiParam(type = "MyBatisPlus的Ipage分页插件的size值",value = "一页的条目数",required = true)
                                        @RequestParam long size){
        LambdaQueryWrapper<UserOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserOrder::getUserId,userId);
        IPage<UserOrder> page = new Page<>(current,size);
        return orderMapper.selectPage(page,lqw);
    }

    /** 这是返回订单展示实体OrderShow的入口*/
    @ApiOperation(value = "传入用户id和分页参数，将此用户的所有订单以及订单详情以分页的形式返回",tags = "分页")
    @GetMapping("/ordershow/page")
    public PageInfo<OrderShow> orderShowPage(@ApiParam(type = "用户id",value = "主键id",required = true)
                                                    @RequestParam Long userId,
                                             @ApiParam(type = "MyBatisPlus的Ipage分页插件的current值",value = "当前页",required = true)
                                                    @RequestParam Integer limit,
                                             @ApiParam(type = "MyBatisPlus的Ipage分页插件的size值",value = "一页的条目数",required = true)
                                                    @RequestParam Integer size){
        return orderService.pageAllOrders(userId,limit,size);
    }

    /** 这是根据订单号单独返回一个订单的所有明细条目*/
    @ApiOperation(value = "传入订单id，将该订单以订单详情返回",tags = "获取")
    @GetMapping("/ordershow/one")
    public List<OrderShow> getOneOrder(@ApiParam(type = "订单id",value = "主键id",required = true)
                                           @RequestParam Long orderid){
        return orderService.listOneOrder(orderid);
    }

    /** 此方法是加入了优惠券使用规则之后的统一结算接口 */
    @ApiOperation(value = "传入用户id和优惠券id，自动判断优惠信息和角色计算",tags = {"功能性操作", "结算"})
    @PostMapping("/unitedSettle")
    public Response unitedSettle(@ApiParam(type = "用户id",value = "主键id",required = true)
                                        @RequestParam Long userid,
                                 @ApiParam(type = "优惠券id",value = "主键id",required = true)
                                        @RequestParam Long couponid){
        CouponRule rule = couponRuleMapper.selectById(couponMapper.selectById(couponid).getRuleId());
        Integer type = rule.getRuleNumber();
        switch (type){
            case 1:
                return orderService.createOrderRule1(userid,couponid);
            case 2:
                return orderService.createOrderRule2(userid,couponid);
            case 3:
                return orderService.createOrderRule3(userid,couponid);
            case 4:
                return orderService.createOrderRule4(userid,couponid);
            default:
                return new Response("1002","无对应规则的计算方法");
        }
    }
}
