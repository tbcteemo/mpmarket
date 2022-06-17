package com.mpprojects.mpmarket.controller.shop;

import com.mpprojects.mpmarket.dao.shop.CouponMapper;
import com.mpprojects.mpmarket.model.shop.Coupon;
import com.mpprojects.mpmarket.service.shop.CouponService;
import com.mpprojects.mpmarket.service.users.UserService;
import com.mpprojects.mpmarket.utils.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private CouponService couponService;

    @Resource
    private UserService userService;

    //创建一个优惠券实体。
    @PostMapping("/add")
    public String add(@RequestBody Coupon coupon){

        //判定是否为相同的优惠券
        Coupon coupon1 = couponMapper.selectSameCoupon(coupon.getSaleoff(),
                coupon.getStartPrice(),
                coupon.getStartTime(),
                coupon.getEndTime(),
                coupon.getIsVipOnly());

        if (coupon1 != null){
            return "该优惠券已存在";
        }
        couponMapper.insert(coupon);
        return "添加优惠券成功";
    }

    //删除一条优惠券记录
    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id){
        couponMapper.deleteById(id);
        return "删除优惠券成功";
    }

    //修改一条优惠券
    @PutMapping("/put")
    public String update(@RequestBody Coupon coupon){
        Coupon coupon1 = couponMapper.selectById(coupon.getId());
        if (coupon1 == null){
            return "id不存在或优惠券记录不存在，请检查id或者转到添加页面";
        }
        couponMapper.updateById(coupon);
        return "修改优惠券内容成功，优惠券id为："+coupon.getId().toString();
    }

    //获取一条优惠券记录
    @GetMapping("/get")
    public Coupon get(@RequestParam Long id){
        return couponMapper.selectById(id);
    }

    //下发优惠券
    @PostMapping("/givecoupon")
    public Response givecoupon(@RequestParam Long userid,
                               @RequestBody Coupon coupon){
        if (coupon.getIsVipOnly() == true){
            if (userService.hasVip(userid) != true){
                return new Response("1002","此优惠券为vip专属，用户角色不匹配！",null);
            }
        }
        return couponService.giveCoupon(userid, coupon);
    }


}
