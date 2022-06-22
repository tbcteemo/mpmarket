package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 这个实体类是商品的类别。一个商品有一个类别，一个类别包含多个商品。
 * 故而商品to类别是一对多的关系，在商品中需要有属性type_id以进行关联。
 */
@Data
public class ProductType {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String description;
}
