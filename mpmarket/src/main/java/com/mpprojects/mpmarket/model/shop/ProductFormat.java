package com.mpprojects.mpmarket.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "商品规格实体")
@Data
public class ProductFormat {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String formatValue;

}
