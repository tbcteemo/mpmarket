package com.mpprojects.mpmarket.service.shop.impl.utils;

import com.mpprojects.mpmarket.dao.relationships.OrderProductMapper;
import com.mpprojects.mpmarket.dao.shop.OrderMapper;
import com.mpprojects.mpmarket.model.shop.Product;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.service.users.impl.UserServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 提要：
 * 1.这个类的作用：
 * 将引入优惠券使用范围之后，加上的计算方法汇总归类。
 * 2.建立这个类的原因：
 * 在引入优惠券的使用规则之后，结合原来的vip不同的优惠规则，优惠系统逐渐变得复杂，
 * 于是有了很多种计算的方法。全部放在service里面，会使得serviceImpl非常臃肿。
 */

@Component
public class CalculateMethodsForCouponRule {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserService userService;

    @Resource
    private OrderProductMapper orderProductMapper;

    /**
     * 当用户为vip时，传入订单号，输出该订单的总价。
     * 使用此方法前必须执行过createRelation方法，生成了Order——Product中间表才行
     * 适用规则：全局规则
     */
    public BigDecimal calVipPrice(Long orderid) {
        //x是根据订单中商品的特有折扣计算出来的打折价总和。
        BigDecimal x = orderMapper.priceOfVipHaveProductDiscount(orderid);
        //y是根据订单中无特有折扣的商品计算出来的打折价总和。
        BigDecimal y = orderMapper.priceOfNoProductDiscount(orderid);
        BigDecimal y2=userService.settleVip(y);
        BigDecimal z = x.add(y2);
        return z;
    }

    /** 当用户为普通user的时候，传入订单号，输出该订单的总价。
     *  使用此方法前必须执行过createRelation方法，生成了Order——Product中间表才行
     *  适用规则：全局规则
     */
    public BigDecimal calUserPrice(Long orderid) {
        //x是根据订单中商品的特有折扣计算出来的打折价总和
        // (实际上普通User不能使用商品单独的Vip折扣，此处算法只是让逻辑更完整。)
        BigDecimal x = orderMapper.priceOfUserHaveProductDiscount(orderid);
        //y是根据订单中无特有折扣的商品计算出来的打折价总和。
        BigDecimal y = orderMapper.priceOfNoProductDiscount(orderid);
        BigDecimal y2=userService.settleUser(y);
        BigDecimal z = x.add(y2);
        return z;
    }

    /**
     * 以下的4个方法，是在优惠券为使用名单模式（白名单，黑名单）的情况时，4种情况下的单项价格。
     */

    //在名单中，有单独优惠的商品的累加价格P1。只适用于vip
    public BigDecimal calP1Vip(long orderid,
                     long ruleid){
        List<Product> productsInListWithDiscount = orderMapper.getProductsInListWithDiscount(orderid, ruleid);
        BigDecimal p1 = new BigDecimal(0);
        for (Product product:
             productsInListWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice()).multiply(new BigDecimal(product.getDiscount())).divide(new BigDecimal(10));
            p1 = p1.add(singlePrice);
        }
        return p1;
    }

    //在名单中，有单独优惠的商品的累加价格P1。只适用于普通User。
    public BigDecimal calP1User(long orderid,
                           long ruleid){
        List<Product> productsInListWithDiscount = orderMapper.getProductsInListWithDiscount(orderid, ruleid);
        BigDecimal p1 = new BigDecimal(0);
        for (Product product:
                productsInListWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p1 = p1.add(singlePrice);
        }
        return p1;
    }

    //在名单中，无单独优惠的商品的累加价格P2。
    public BigDecimal calP2(long orderid,
                        long ruleid){
        List<Product> productsInListWithoutDiscount = orderMapper.getProductsInListWithoutDiscount(orderid, ruleid);
        BigDecimal p2 = new BigDecimal(0);
        for (Product product:
                productsInListWithoutDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p2 = p2.add(singlePrice);
        }
        return p2;
    }

    //不在名单中，有单独优惠的商品的累加价格P3。适用角色：vip。
    public BigDecimal calP3Vip(long orderid,
                        long ruleid){
        List<Product> productsNotInListWithDiscount = orderMapper.getProductsNotInListWithDiscount(orderid, ruleid);
        BigDecimal p3 = new BigDecimal(0);
        for (Product product:
                productsNotInListWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice()).multiply(new BigDecimal(product.getDiscount())).divide(new BigDecimal(10));
            p3 = p3.add(singlePrice);
        }
        return p3;
    }

    //不在名单中，有单独优惠的商品的累加价格P3。适用角色：仅普通用户。
    public BigDecimal calP3User(long orderid,
                           long ruleid){
        List<Product> productsNotInListWithDiscount = orderMapper.getProductsNotInListWithDiscount(orderid, ruleid);
        BigDecimal p3 = new BigDecimal(0);
        for (Product product:
                productsNotInListWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p3 = p3.add(singlePrice);
        }
        return p3;
    }

    //不在名单中，无单独优惠的商品的累加价格P4。
    public BigDecimal calP4(long orderid,
                        long ruleid){
        List<Product> productsNotInListWithoutDiscount = orderMapper.getProductsNotInListWithoutDiscount(orderid, ruleid);
        BigDecimal p4 = new BigDecimal(0);
        for (Product product:
                productsNotInListWithoutDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p4 = p4.add(singlePrice);
        }
        return p4;
    }

    /**
     * 以下的4个方法，是在优惠券为使用类别模式时使用的。规则代码：4
     */

    //符合类别编号，有单独优惠的商品的累加价格P1。只适用于vip（会计算商品的单独折扣）。
    public BigDecimal calP1VipForType(long orderid,
                           long ruleid){
        List<Product> productsInTypeWithDiscount = orderMapper.getProductsInTypeWithDiscount(orderid, ruleid);
        BigDecimal p1 = new BigDecimal(0);
        for (Product product:
                productsInTypeWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice()).multiply(new BigDecimal(product.getDiscount())).divide(new BigDecimal(10));
            p1 = p1.add(singlePrice);
        }
        return p1;
    }

    //在类别中，有单独优惠的商品的累加价格P1。只适用于普通User。
    public BigDecimal calP1UserForType(long orderid,
                            long ruleid){
        List<Product> productsInTypeWithDiscount = orderMapper.getProductsInTypeWithDiscount(orderid, ruleid);
        BigDecimal p1 = new BigDecimal(0);
        for (Product product:
                productsInTypeWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p1 = p1.add(singlePrice);
        }
        return p1;
    }

    //在类别中，无单独优惠的商品的累加价格P2。
    public BigDecimal calP2ForType(long orderid,
                        long ruleid){
        List<Product> productsInTypeWithoutDiscount = orderMapper.getProductsInTypeWithoutDiscount(orderid, ruleid);
        BigDecimal p2 = new BigDecimal(0);
        for (Product product:
                productsInTypeWithoutDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p2 = p2.add(singlePrice);
        }
        return p2;
    }

    //不在类别中，有单独优惠的商品的累加价格P3。适用用户角色：vip
    public BigDecimal calP3VipForType(long orderid,
                           long ruleid){
        List<Product> productsNotInTypeWithDiscount = orderMapper.getProductsNotInTypeWithDiscount(orderid, ruleid);
        BigDecimal p3 = new BigDecimal(0);
        for (Product product:
                productsNotInTypeWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice()).multiply(new BigDecimal(product.getDiscount())).divide(new BigDecimal(10));
            p3 = p3.add(singlePrice);
        }
        return p3;
    }

    //不在类别中，有单独优惠的商品的累加价格P3。适用用户角色：普通用户。
    public BigDecimal calP3UserForType(long orderid,
                            long ruleid){
        List<Product> productsNotInTypeWithDiscount = orderMapper.getProductsNotInTypeWithDiscount(orderid, ruleid);
        BigDecimal p3 = new BigDecimal(0);
        for (Product product:
                productsNotInTypeWithDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p3 = p3.add(singlePrice);
        }
        return p3;
    }

    //不在类别中，无单独优惠的商品的累加价格P4。
    public BigDecimal calP4ForType(long orderid,
                        long ruleid){
        List<Product> productsNotInTypeWithoutDiscount = orderMapper.getProductsNotInTypeWithoutDiscount(orderid, ruleid);
        BigDecimal p4 = new BigDecimal(0);
        for (Product product:
                productsNotInTypeWithoutDiscount) {
            BigDecimal count = new BigDecimal(orderProductMapper.getProductCount(orderid,product.getId()));
            BigDecimal singlePrice = count.multiply(product.getPrice());
            p4 = p4.add(singlePrice);
        }
        return p4;
    }
}
