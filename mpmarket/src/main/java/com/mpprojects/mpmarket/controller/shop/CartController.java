package com.mpprojects.mpmarket.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpprojects.mpmarket.dao.relationships.CartProductMapper;
import com.mpprojects.mpmarket.dao.shop.CartMapper;
import com.mpprojects.mpmarket.model.shop.Cart;
import com.mpprojects.mpmarket.model.shop.CartShow;
import com.mpprojects.mpmarket.model.shop.relationship.CartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartProductMapper cartProductMapper;

    /** 创建购物车 */
    @PostMapping("/add")
    public Cart add(@RequestParam long userId){
        Cart cart = new Cart();
        cart.setUserId(userId);
//        cart.setCreateTime(System.currentTimeMillis());
//        cart.setLastUpdateTime(System.currentTimeMillis());
        cartMapper.insert(cart);
        return cart;
    }

//    @DeleteMapping("/delete")
//    public String delete(@RequestParam long id){
//        cartMapper.deleteById(id);
//        return "目标购物车删除成功";
//    }

//    @PutMapping("/update")
//    public String update(Cart cart){
//        cartMapper.updateById(cart);
//        return "修改完毕";
//    }

    /** 当前用户购物车展示 */
    @GetMapping("/show")
    public Cart getByUserId(@RequestParam long userid){
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userid);
        return cartMapper.selectOne(queryWrapper);
    }

    /** 添加商品到购物车*/
    @PostMapping("/addproduct")
    public BigDecimal addProduct(@RequestBody CartProduct cartProduct){
        cartProductMapper.insert(cartProduct);
        return cartProductMapper.preCal();
    }

    /** 勾选购物车商品，将返回预结算总价，且更新中间表isSelected属性 */
    @PutMapping("/selected")
    public BigDecimal updateSelectedProduct(@RequestBody CartProduct cartProduct){
        cartProductMapper.updateById(cartProduct);
        return cartProductMapper.preCal();
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
