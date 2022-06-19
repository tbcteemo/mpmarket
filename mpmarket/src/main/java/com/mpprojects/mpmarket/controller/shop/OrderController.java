package com.mpprojects.mpmarket.controller.shop;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/add")
    public Response createOrder(@RequestParam Long userId){
        return orderService.createOrder(userId);
    }

    @PostMapping("/add2")
    public Response createOrder2(@RequestParam Long userid,
                               @RequestParam Long couponid){
        return orderService.createOrder2(userid, couponid);
    }

    @PostMapping("/add3")
    public Response createOrder3(@RequestParam Long userid){
        return orderService.createOrder3(userid);
    }

    /** 这是直接返回订单实体的入口 */
    @GetMapping("/page")
    public IPage<UserOrder> page(@RequestParam long userId,
                                 @RequestParam long current,
                                 @RequestParam long size){
        LambdaQueryWrapper<UserOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserOrder::getUserId,userId);
        IPage<UserOrder> page = new Page<>(current,size);
        return orderMapper.selectPage(page,lqw);
    }

    /** 这是返回订单展示实体OrderShow的入口*/
    @GetMapping("/ordershow/page")
    public PageInfo<OrderShow> orderShowPage(@RequestParam Long userId,
                                              @RequestParam Integer limit,
                                              @RequestParam Integer size){
        return orderService.pageAllOrders(userId,limit,size);
    }

    /** 这是根据订单号单独返回一个订单的所有明细条目*/
    @GetMapping("/ordershow/one")
    public List<OrderShow> getOneOrder(@RequestParam Long orderid){
        return orderService.listOneOrder(orderid);
    }

    /** 此方法是加入了优惠券使用规则之后的统一结算接口 */
    @PostMapping("/unitedSettle")
    public Response unitedSettle(@RequestParam Long userid,
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
