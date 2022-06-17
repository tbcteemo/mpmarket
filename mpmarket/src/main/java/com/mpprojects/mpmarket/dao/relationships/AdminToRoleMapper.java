package com.mpprojects.mpmarket.dao.relationships;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.users.relationship.AdminToRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminToRoleMapper extends BaseMapper<AdminToRole> {

}
