package com.mpprojects.mpmarket.service.shop.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpprojects.mpmarket.dao.shop.ProductMapper;
import com.mpprojects.mpmarket.model.shop.Product;
import com.mpprojects.mpmarket.service.shop.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
