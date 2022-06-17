package com.mpprojects.mpmarket.service.shop;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mpprojects.mpmarket.model.shop.OrderShow;
import com.mpprojects.mpmarket.model.shop.UserOrder;
import com.mpprojects.mpmarket.utils.Response;

import java.util.List;

public interface OrderService extends IService<UserOrder> {
    public void createRelation(Long userid,Long orderid);
    public List<OrderShow> listAllOrders(Long userid);
    public List<OrderShow> listOneOrder(Long orderid);
    public PageInfo<OrderShow> pageAllOrders(Long userid, Integer pageNum, Integer pageSize);
    Response createOrder(Long userid);
    Response createOrder2(Long userid, Long couponid);
    Response createOrder3(Long userid);

    /**
     * 以下4个方法，为对应4种规则的计算方法。后面的数字对应规则的编号。如：
     * createOrderRule1 ： 计算方法1，适用于白名单规则；
     * createOrderRule2 ： 计算方法2，适用于黑名单规则；
     * createOrderRule3 ： 计算方法3，适用于全局规则；
     * createOrderRule4 ： 计算方法4，适用于按类别优惠规则。
     * 除了全局可用规则外，其余规则在使用之前，必须生成优惠规则与其相应的中间表。如：规则—商品的白名单，规则—商品的类别。
     */
    Response createOrderRule1(Long userid,Long couponid);
    Response createOrderRule2(Long userid,Long couponid);
    Response createOrderRule3(Long userid,Long couponid);
    Response createOrderRule4(Long userid,Long couponid);
}
