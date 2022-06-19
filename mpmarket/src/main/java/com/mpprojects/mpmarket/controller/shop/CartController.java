package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpprojects.mpmarket.dao.relationships.CartProductMapper;
import com.mpprojects.mpmarket.dao.shop.CartMapper;
import com.mpprojects.mpmarket.model.shop.Cart;
import com.mpprojects.mpmarket.model.shop.CartShow;
import com.mpprojects.mpmarket.model.shop.relationship.CartProduct;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private CartProductMapper cartProductMapper;

    /** 创建购物车 */
    @PostMapping("/add")
    public Response<Cart> add(@RequestParam long userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
//        cart.setCreateTime(System.currentTimeMillis());
//        cart.setLastUpdateTime(System.currentTimeMillis());
        cartMapper.insert(cart);
        return new Response<Cart>("200","添加购物车成功",cart);
    }

    /** 当前用户购物车展示 */
    @GetMapping("/show")
    public Response<Cart> getByUserId(@RequestParam long userid){
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userid);
        return new Response<>("200","根据用户id选择其购物车成功",cartMapper.selectOne(queryWrapper));
    }

    /** 添加商品到购物车*/
    @PostMapping("/addproduct")
    public Response<BigDecimal> addProduct(@RequestBody CartProduct cartProduct){
        cartProductMapper.insert(cartProduct);
        return new Response<>("200",
                "添加商品到购物车成功，目前预计总价：" + cartProductMapper.preCal(cartProduct.getCartId()).toString(),
                cartProductMapper.preCal(cartProduct.getCartId()));
    }

    /** 勾选购物车商品，将返回预结算总价，且更新中间表isSelected属性 */
    @PutMapping("/selected")
    public Response<BigDecimal> updateSelectedProduct(@RequestBody CartProduct cartProduct){
        cartProductMapper.updateById(cartProduct);
        return new Response<>("200",
                "根据勾选的商品预算总价为" + cartProductMapper.preCal(cartProduct.getCartId()).toString(),
                cartProductMapper.preCal(cartProduct.getCartId()));
    }

//    /**展示购物车内的详情，本质是返回中间表的所有对象*/
//    @GetMapping("/showall")
//    public List<CartProduct> showall(@RequestParam Long userid){
//        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id",userid);
//        Long cartid = cartMapper.selectOne(queryWrapper).getId();
//        return cartProductMapper.selectAll(cartid);
//    }

    /** 前端传入userid，向前端传回一个CartShow对象的集合*/
    @GetMapping("/showdetails")
    public List<CartShow> showDetails(@RequestParam Long userid){
        return cartProductMapper.showDetails(userid);
    }

}
