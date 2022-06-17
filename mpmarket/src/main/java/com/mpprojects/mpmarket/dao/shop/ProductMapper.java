package com.mpprojects.mpmarket.dao.shop;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpprojects.mpmarket.model.shop.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    Product selectBySn(@Param("sn") Long sn);
}
