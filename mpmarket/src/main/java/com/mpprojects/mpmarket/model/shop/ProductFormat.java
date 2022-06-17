package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProductFormat {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String formatValue;

}
