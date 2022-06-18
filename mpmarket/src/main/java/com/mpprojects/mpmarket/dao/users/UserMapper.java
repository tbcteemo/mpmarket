package com.mpprojects.mpmarket.dao.users;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.users.User;
import com.mpprojects.mpmarket.model.users.UserAndCouponShow;
import com.mpprojects.mpmarket.model.users.relationship.UserToRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    UserToRole selectVipRelation(@Param("userid") Long userid);
    List<User> selectVipUser();     //选取数据库中所有有VIP这个role的user
    List<UserAndCouponShow> selectVipAndHisUnusedCoupon();
}
