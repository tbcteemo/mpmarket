package com.mpprojects.mpmarket.dao.users;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.users.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
