package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mpprojects.mpmarket.dao.relationships.CartProductMapper;
import com.mpprojects.mpmarket.dao.relationships.OrderProductMapper;
import com.mpprojects.mpmarket.dao.relationships.UserCouponMapper;
import com.mpprojects.mpmarket.dao.shop.*;
import com.mpprojects.mpmarket.dao.users.UserMapper;
import com.mpprojects.mpmarket.model.shop.Cart;
import com.mpprojects.mpmarket.model.shop.Coupon;
import com.mpprojects.mpmarket.model.shop.OrderShow;
import com.mpprojects.mpmarket.model.shop.UserOrder;
import com.mpprojects.mpmarket.model.shop.relationship.CartProduct;
import com.mpprojects.mpmarket.model.shop.relationship.OrderProduct;
import com.mpprojects.mpmarket.model.shop.relationship.UserCoupon;
import com.mpprojects.mpmarket.model.users.User;
import com.mpprojects.mpmarket.service.shop.OrderService;
import com.mpprojects.mpmarket.service.shop.impl.utils.CalculateMethodsForCouponRule;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.utils.Response;
import com.mpprojects.mpmarket.utils.SettlementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, UserOrder> implements OrderService {

    @Autowired
    private CartProductMapper cartProductMapper;

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CalculateMethodsForCouponRule calculateMethodsForCouponRule;

    @Resource
    private CouponRuleMapper couponRuleMapper;

    //生成order-product中间表的记录。
    @Override
    public void createRelation(Long userid,Long orderid){
        List<CartProduct> cartProductList = cartProductMapper.getSelectedProducts(userid);
        for (CartProduct cartProduct:cartProductList) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProductId(cartProduct.getProductId());
            orderProduct.setProductPrice(productMapper.selectById(cartProduct.getProductId()).getPrice());
            orderProduct.setProductCount(cartProduct.getProductCount());
            orderProduct.setOrderId(orderid);
            orderProductMapper.insert(orderProduct);
        }
    }

    @Override
    public List<OrderShow> listAllOrders(Long userid){
        return orderMapper.listAllOrders(userid);
    }

    @Override
    public List<OrderShow> listOneOrder(Long orderid){
        return orderMapper.listOneOrder(orderid);
    }

    @Override
    public PageInfo<OrderShow> pageAllOrders(Long userid, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<OrderShow> list = listAllOrders(userid);
        PageInfo<OrderShow> page = new PageInfo<>(list);
        return page;
    }

    @Override
    //传入用户id进行结算的方法，优惠券和vip判定的代码块在内部。
    public Response createOrder(Long userId) throws SettlementException {

        //1.new一个order对象，并指定其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userId);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userId,orderid);

        //totalPrice计算订单总价，通过xml文件实现的计算总价。
        BigDecimal tp = orderProductMapper.totalPrice(orderid);
        BigDecimal finalPrice = tp;
        LocalDateTime timenow = LocalDateTime.now();

        //这一层if判断是判定是否是vip,vip和普通用户有各自的计算和打折逻辑，所以要分别。最后得出一个最终价格finalPrice
        if (userService.isVip(userId) == Boolean.TRUE){
            //viptp是原价tp进行VIP专用的打折算法算出来之后的打折后总价。
            BigDecimal viptp = userService.settleVip(tp);
            Coupon coupon = couponMapper.getSelectedCoupon(userId);
            if (coupon == null){
                finalPrice = viptp;
            }else {
                //此处的if判断是判定打折之后的价格是否满足满减条件
                if (viptp.compareTo(coupon.getStartPrice()) >= 0
                        && coupon.getStartTime().isBefore(timenow)
                        && coupon.getEndTime().isAfter(timenow)) {
                    finalPrice = viptp.subtract(coupon.getSaleoff());
                }
            }
        }else{
            BigDecimal usertp = userService.settleUser(tp);
            Coupon coupon = couponMapper.getSelectedCoupon(userId);
            if (coupon == null){
                finalPrice = usertp;
            }
            //此处的if判断是判定打折之后的价格是否满足满减条件
            if (usertp.compareTo(coupon.getStartPrice()) >= 0
                    && coupon.getStartTime().isBefore(timenow)
                    && coupon.getEndTime().isAfter(timenow)){
                finalPrice = usertp.subtract(coupon.getSaleoff());
            }
        }
        //余额判定的逻辑，余额不足则报错充值，余额够则扣费；
        User usernow = userMapper.selectById(userId);
        BigDecimal moneynow = usernow.getMoney();
        if (moneynow.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }
        usernow.setMoney(moneynow.subtract(finalPrice));
        userMapper.updateById(usernow);
        userOrder.setStatus(1);
        orderMapper.updateById(userOrder);


        //此处结算成功之后要删除cart product的中间表已结算商品的记录。
        //根据order id获取已结算商品的信息再去删除cart product中间表步骤冗余且复杂，所以直接选择删除用户购物车中，结算前选定的商品即可。
        //第三种方法，直接在xml中并表查询，这样输入userid就直接操作了。
        QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
        cartidWrapper.eq("user_id",userId);
        Long cartid = cartMapper.selectOne(cartidWrapper).getId();
        cartProductMapper.deleteSelected(cartid);

        return new Response("200",
                "订单创建且扣费成功，余额：" + userMapper.selectById(userId).getMoney().toString() +
                "原价：" + tp + "成交价：" + finalPrice);

    }

    /** 此方法是传入用户id和一个优惠券的id进行结算。优惠券若不存在则按照原价计算*/
    @Override
    public Response createOrder2(Long userid, Long couponid) throws SettlementException{
        //1.new一个order对象，并指定其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userid);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userid,orderid);

        //totalPrice计算订单总价，通过xml文件
        BigDecimal tp = orderProductMapper.totalPrice(orderid);
        BigDecimal finalPrice = tp;
        LocalDateTime timenow = LocalDateTime.now();

        if (userService.isVip(userid) == Boolean.TRUE){
            //viptp是原价tp进行VIP专用的打折算法算出来之后的打折后总价。
            BigDecimal viptp = userService.settleVip(tp);
            Coupon coupon = couponMapper.selectById(couponid);
            if (coupon == null){
                finalPrice = viptp;
            }else {
                //此处的if判断是判定打折之后的价格是否满足满减条件
                if (viptp.compareTo(coupon.getStartPrice()) >= 0
                        && coupon.getStartTime().isBefore(timenow)
                        && coupon.getEndTime().isAfter(timenow)) {
                    finalPrice = viptp.subtract(coupon.getSaleoff());
                }
            }
        }else{
            BigDecimal usertp = userService.settleUser(tp);
            Coupon coupon = couponMapper.selectById(couponid);
            if (coupon == null){
                finalPrice = usertp;
            }
            //此处的if判断是判定打折之后的价格是否满足满减条件
            if (usertp.compareTo(coupon.getStartPrice()) >= 0
                    && coupon.getStartTime().isBefore(timenow)
                    && coupon.getEndTime().isAfter(timenow)){
                finalPrice = usertp.subtract(coupon.getSaleoff());
            }
        }

        //用户余额和价格进行比对，并且更新user的和order的数据库记录。
        User usernow = userMapper.selectById(userid);
        BigDecimal moneynow = usernow.getMoney();
        if (moneynow.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }
        usernow.setMoney(moneynow.subtract(finalPrice));
        userMapper.updateById(usernow);
        userOrder.setStatus(1);
        userOrder.setCreateTime(timenow);
        orderMapper.updateById(userOrder);


        //此处结算成功之后要删除cart product的中间表已结算商品的记录。
        //根据order id获取已结算商品的信息再去删除cart product中间表步骤冗余且复杂，所以直接选择删除用户购物车中，结算前选定的商品即可。
        //第三种方法，直接在xml中并表查询，这样输入userid就直接操作了。
        QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
        cartidWrapper.eq("user_id",userid);
        Long cartid = cartMapper.selectOne(cartidWrapper).getId();
        cartProductMapper.deleteSelected(cartid);

        return new Response("200","优惠券订单创建且扣费成功，余额：" + userMapper.selectById(userid).getMoney().toString()
                +"原价：" + tp + "成交价：" + finalPrice);
    }

    @Override
    public Response createOrder3(Long userid) throws SettlementException{
        //1.new一个order对象，并获得其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userid);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userid,orderid);

        //totalPrice计算订单总价，通过xml文件
        BigDecimal tp = orderProductMapper.totalPrice(orderid);
        BigDecimal finalPrice = tp;
        LocalDateTime timenow = LocalDateTime.now();

        if (userService.isVip(userid) == Boolean.TRUE){
            finalPrice = userService.settleVip(tp);
        }else{
            finalPrice = userService.settleUser(tp);
        }

        User usernow = userMapper.selectById(userid);
        BigDecimal moneynow = usernow.getMoney();
        if (moneynow.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }
        usernow.setMoney(moneynow.subtract(finalPrice));
        userMapper.updateById(usernow);
        userOrder.setStatus(1);
        userOrder.setCreateTime(timenow);
        orderMapper.updateById(userOrder);


        //此处结算成功之后要删除cart product的中间表已结算商品的记录。
        //根据order id获取已结算商品的信息再去删除cart product中间表步骤冗余且复杂，所以直接选择删除用户购物车中，结算前选定的商品即可。
        //第三种方法，直接在xml中并表查询，这样输入userid就直接操作了。
        QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
        cartidWrapper.eq("user_id",userid);
        Long cartid = cartMapper.selectOne(cartidWrapper).getId();
        cartProductMapper.deleteSelected(cartid);

        return new Response("200","无优惠券订单创建且扣费成功，余额：" + userMapper.selectById(userid).getMoney().toString()
                +"原价：" + tp + "成交价：" + finalPrice);
    }



    /** 此方法是根据会员统一折扣A和商品单独折扣B进行结算的，仅适用于全局规则(规则编号：3)的优惠券。*/
    @Override
    public Response createOrderRule3(Long userid, Long couponid) throws SettlementException{
        //1.new一个order对象，并获得其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userid);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userid,orderid);
        LocalDateTime timenow = LocalDateTime.now();

        //3.进行vip身份判定,并根据不同的身份调用不同的价格算法。
        BigDecimal price = new BigDecimal(0);
        if (userService.isVip(userid) == true){
            price = calculateMethodsForCouponRule.calVipPrice(orderid);
        }else{
            price = calculateMethodsForCouponRule.calUserPrice(orderid);
        }

        //判定是否能用优惠券，并计算相应的最终价格
        Coupon coupon = couponMapper.selectById(couponid);
        BigDecimal finalPrice = new BigDecimal(0);
        if (coupon == null){
            finalPrice = price;
        }else{
            if (price.compareTo(coupon.getStartPrice()) > 0
                    && LocalDateTime.now().isAfter(coupon.getStartTime())
                    && LocalDateTime.now().isBefore(coupon.getEndTime())){
                finalPrice = price.subtract(coupon.getSaleoff());
            }else{
                finalPrice = price;
            }
        }

        //最终价格与钱包余额进行判断比对，决定订单是否成功,成功则进行收尾处理。
        User user = userMapper.selectById(userid);
        BigDecimal money = user.getMoney();
        if (money.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }else{
            //订单能够结算，则进行金额及更新数据库等收尾操作
            user.setMoney(money.subtract(finalPrice));
            userMapper.updateById(user);
            userOrder.setStatus(1);
            userOrder.setCreateTime(timenow);
            orderMapper.updateById(userOrder);
            //软删除已使用的优惠券
            UserCoupon userCoupon = userCouponMapper.selectByUserIdAndCouponId(userid,couponid);
            userCoupon.setIsEnable(false);
            userCouponMapper.updateById(userCoupon);

            //收尾操作之删除购物车中已经结算的商品。
            QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
            cartidWrapper.eq("user_id",userid);
            Long cartid = cartMapper.selectOne(cartidWrapper).getId();
            cartProductMapper.deleteSelected(cartid);

            return new Response("200","订单创建成功，最终成交额：" + finalPrice + "时间：" + timenow.toString());
        }

    }

    /** 此方法适用于：白名单规则；规则码：1 */
    @Override
    public Response createOrderRule1(Long userid, Long couponid) throws SettlementException{
        //1.new一个order对象，并获得其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userid);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userid,orderid);
        LocalDateTime timenow = LocalDateTime.now();

        //身份判定，并套用相应的算法
        BigDecimal finalPrice = new BigDecimal(0);
        Coupon coupon = couponMapper.selectById(couponid);
        long ruleid = coupon.getRuleId();

        if (userService.isVip(userid) == true){
            //p1是计算了商品单独折扣的金额，所以不用调用settleVip方法使用Vip专属折扣。P3同。
            BigDecimal p1 = calculateMethodsForCouponRule.calP1Vip(orderid,ruleid);
            //p2是没有单独折扣的商品价格总和，因此需要进行vip固定折扣打折。P4同。
            BigDecimal p2 = calculateMethodsForCouponRule.calP2(orderid,ruleid);
            BigDecimal p3 = calculateMethodsForCouponRule.calP3Vip(orderid,ruleid);
            BigDecimal p4 = calculateMethodsForCouponRule.calP4(orderid,ruleid);
            //会员的计算
            if (
//                   原代码：p1 + userService.settleVip(p2) >= coupon.getStartPrice()
                    p1.add(userService.settleVip(p2)).compareTo(coupon.getStartPrice()) >= 0
                    && timenow.isBefore(coupon.getEndTime())
                    && timenow.isAfter(coupon.getStartTime())){
                finalPrice = p1.add(userService.settleVip(p2)).subtract(coupon.getSaleoff())
                        .add(p3).add(userService.settleVip(p4));
            }else{
                finalPrice = p1.add(userService.settleVip(p2)).add(p3).add(userService.settleVip(p4));
            }
        }else{
            //非会员的计算
            BigDecimal p1 = calculateMethodsForCouponRule.calP1User(orderid,ruleid);
            //p2是没有单独折扣的商品价格总和，因此需要进行vip固定折扣打折。P4同。
            BigDecimal p2 = calculateMethodsForCouponRule.calP2(orderid,ruleid);
            BigDecimal p3 = calculateMethodsForCouponRule.calP3User(orderid,ruleid);
            BigDecimal p4 = calculateMethodsForCouponRule.calP4(orderid,ruleid);
            if (
//                  原代码：p1 + userService.settleUser(p2) >= coupon.getStartPrice()
                    p1.add(userService.settleUser(p2)).compareTo(coupon.getStartPrice()) >= 0
                    && timenow.isBefore(coupon.getEndTime())
                    && timenow.isAfter(coupon.getStartTime())){
                finalPrice = p1.add(userService.settleUser(p2)).subtract(coupon.getSaleoff())
                        .add(p3).add(userService.settleUser(p4));
            }else{
                finalPrice = p1.add(userService.settleUser(p2)).add(p3).add(userService.settleUser(p4));
            }
        }
        //最终价格与钱包余额进行判断比对，决定订单是否成功,成功则进行收尾处理。
        User user = userMapper.selectById(userid);
        BigDecimal money = user.getMoney();
        if (money.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }else{
            //订单能够结算，则进行金额及更新数据库等收尾操作
            user.setMoney(money.subtract(finalPrice));
            userMapper.updateById(user);
            userOrder.setStatus(1);
            userOrder.setCreateTime(timenow);
            orderMapper.updateById(userOrder);

            //软删除已使用的优惠券
            UserCoupon userCoupon = userCouponMapper.selectByUserIdAndCouponId(userid,couponid);
            userCoupon.setIsEnable(false);
            userCouponMapper.updateById(userCoupon);

            //收尾操作之删除购物车中已经结算的商品。
            QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
            cartidWrapper.eq("user_id",userid);
            Long cartid = cartMapper.selectOne(cartidWrapper).getId();
            cartProductMapper.deleteSelected(cartid);

            return new Response("200","订单创建成功，最终成交额：" + finalPrice + "时间：" + timenow.toString());
        }

    }

    /** 此方法适用于：黑名单规则；规则码：2 */
    @Override
    public Response createOrderRule2(Long userid, Long couponid) throws SettlementException{
        //1.new一个order对象，并获得其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userid);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userid,orderid);
        LocalDateTime timenow = LocalDateTime.now();

        //身份判定，并套用相应的算法
        BigDecimal finalPrice = new BigDecimal(0);
        Coupon coupon = couponMapper.selectById(couponid);
        long ruleid = coupon.getRuleId();

        if (userService.isVip(userid) == true){
            //会员的计算
            //p1是计算了商品单独折扣的金额，所以不用调用settleVip方法使用Vip专属折扣。P3同。
            BigDecimal p1 = calculateMethodsForCouponRule.calP1Vip(orderid,ruleid);
            //p2是没有单独折扣的商品价格总和，因此需要进行vip固定折扣打折。P4同。
            BigDecimal p2 = calculateMethodsForCouponRule.calP2(orderid,ruleid);
            BigDecimal p3 = calculateMethodsForCouponRule.calP3Vip(orderid,ruleid);
            BigDecimal p4 = calculateMethodsForCouponRule.calP4(orderid,ruleid);
            if (p3.add(userService.settleVip(p4)).compareTo(coupon.getStartPrice()) >= 0
                    && timenow.isBefore(coupon.getEndTime())
                    && timenow.isAfter(coupon.getStartTime())){
                finalPrice = p1.add(userService.settleVip(p2)).subtract(coupon.getSaleoff())
                        .add(p3).add(userService.settleVip(p4));
            }else{
                finalPrice = p1.add(userService.settleVip(p2)).add(p3).add(userService.settleVip(p4));
            }
        }else{
            //非会员的计算
            //p1是计算了商品单独折扣的金额，所以不用调用settleVip方法使用Vip专属折扣。P3同。
            BigDecimal p1 = calculateMethodsForCouponRule.calP1User(orderid,ruleid);
            //p2是没有单独折扣的商品价格总和，因此需要进行vip固定折扣打折。P4同。
            BigDecimal p2 = calculateMethodsForCouponRule.calP2(orderid,ruleid);
            BigDecimal p3 = calculateMethodsForCouponRule.calP3User(orderid,ruleid);
            BigDecimal p4 = calculateMethodsForCouponRule.calP4(orderid,ruleid);
            if (p3.add(userService.settleUser(p4)).compareTo(coupon.getStartPrice()) >= 0
                    && timenow.isBefore(coupon.getEndTime())
                    && timenow.isAfter(coupon.getStartTime())){
                finalPrice = p1.add(userService.settleUser(p2)).subtract(coupon.getSaleoff())
                        .add(p3).add(userService.settleUser(p4));
            }else{
                finalPrice = p1.add(userService.settleUser(p2)).add(p3).add(userService.settleUser(p4));
            }
        }
        //最终价格与钱包余额进行判断比对，决定订单是否成功,成功则进行收尾处理。
        User user = userMapper.selectById(userid);
        BigDecimal money = user.getMoney();
        if (money.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }else{
            //订单能够结算，则进行金额及更新数据库等收尾操作
            user.setMoney(money.subtract(finalPrice));
            userMapper.updateById(user);
            userOrder.setStatus(1);
            userOrder.setCreateTime(timenow);
            orderMapper.updateById(userOrder);

            //软删除已使用的优惠券
            UserCoupon userCoupon = userCouponMapper.selectByUserIdAndCouponId(userid,couponid);
            userCoupon.setIsEnable(false);
            userCouponMapper.updateById(userCoupon);

            //收尾操作之删除购物车中已经结算的商品。
            QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
            cartidWrapper.eq("user_id",userid);
            Long cartid = cartMapper.selectOne(cartidWrapper).getId();
            cartProductMapper.deleteSelected(cartid);

            return new Response("200","订单创建成功，最终成交额：" + finalPrice + "时间：" + timenow.toString());
        }
    }

    /** 此方法适用：按品类使用规则；规则码：4。即：类别id在列表中的商品才能用。 */
    @Override
    public Response createOrderRule4(Long userid, Long couponid) throws SettlementException{
        //1.new一个order对象，并获得其userId；
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userid);

        //2.保存order对象到数据库，以获得order id；
        orderMapper.insert(userOrder);
        Long orderid = userOrder.getId();
        createRelation(userid,orderid);
        LocalDateTime timenow = LocalDateTime.now();

        //身份判定，并套用相应的算法
        BigDecimal finalPrice = new BigDecimal(0);
        Coupon coupon = couponMapper.selectById(couponid);
        if (couponRuleMapper.selectById(coupon.getRuleId()).getRuleNumber() != 4){
            throw new SettlementException("优惠券使用范围错误，请重新选择");
        }
        long ruleid = coupon.getRuleId();

        if (userService.isVip(userid) == true){
            //p1是计算了商品单独折扣的金额，所以不用调用settleVip方法使用Vip专属折扣。P3同。
            BigDecimal p1 = calculateMethodsForCouponRule.calP1VipForType(orderid,ruleid);
            //p2是没有单独折扣的商品价格总和，因此需要进行vip固定折扣打折。P4同。
            BigDecimal p2 = calculateMethodsForCouponRule.calP2ForType(orderid,ruleid);
            BigDecimal p3 = calculateMethodsForCouponRule.calP3VipForType(orderid,ruleid);
            BigDecimal p4 = calculateMethodsForCouponRule.calP4ForType(orderid,ruleid);
            //会员的计算
            if (p1.add(userService.settleVip(p2)).compareTo(coupon.getStartPrice()) >= 0
                    && timenow.isBefore(coupon.getEndTime())
                    && timenow.isAfter(coupon.getStartTime())){
                finalPrice = p1.add(userService.settleVip(p2)).subtract(coupon.getSaleoff())
                        .add(p3).add(userService.settleVip(p4));
            }else{
                finalPrice = p1.add(userService.settleVip(p2)).add(p3).add(userService.settleVip(p4));
            }
        }else{
            //非会员的计算
            BigDecimal p1 = calculateMethodsForCouponRule.calP1UserForType(orderid,ruleid);
            //p2是没有单独折扣的商品价格总和，因此需要进行vip固定折扣打折。P4同。
            BigDecimal p2 = calculateMethodsForCouponRule.calP2ForType(orderid,ruleid);
            BigDecimal p3 = calculateMethodsForCouponRule.calP3UserForType(orderid,ruleid);
            BigDecimal p4 = calculateMethodsForCouponRule.calP4ForType(orderid,ruleid);
            if (p1.add(userService.settleUser(p2)).compareTo(coupon.getStartPrice()) >= 0
                    && timenow.isBefore(coupon.getEndTime())
                    && timenow.isAfter(coupon.getStartTime())){
                finalPrice = p1.add(userService.settleUser(p2)).subtract(coupon.getSaleoff())
                        .add(p3).add(userService.settleUser(p4));
            }else{
                finalPrice = p1.add(userService.settleUser(p2)).add(p3).add(userService.settleUser(p4));
            }
        }
        //最终价格与钱包余额进行判断比对，决定订单是否成功,成功则进行收尾处理。
        User user = userMapper.selectById(userid);
        BigDecimal money = user.getMoney();
        if (money.compareTo(finalPrice) < 0){
            throw new SettlementException("余额不足");
        }else{
            //订单能够结算，则进行金额及更新数据库等收尾操作
            user.setMoney(money.subtract(finalPrice));
            userMapper.updateById(user);
            userOrder.setStatus(1);
            userOrder.setCreateTime(timenow);
            orderMapper.updateById(userOrder);

            //软删除已使用的优惠券
            UserCoupon userCoupon = userCouponMapper.selectByUserIdAndCouponId(userid,couponid);
            userCoupon.setIsEnable(false);
            userCouponMapper.updateById(userCoupon);

            //收尾操作之删除购物车中已经结算的商品。
            QueryWrapper<Cart> cartidWrapper = new QueryWrapper<>();
            cartidWrapper.eq("user_id",userid);
            Long cartid = cartMapper.selectOne(cartidWrapper).getId();
            cartProductMapper.deleteSelected(cartid);

            return new Response("200","订单创建成功，最终成交额：" + finalPrice + "时间：" + timenow.toString());
        }

    }
}
