package com.cqucs.blogbackend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categories {

    @ApiModelProperty(value = "分类编号")
    private Integer cg_id;

    @ApiModelProperty(value = "分类名称")
    private String cg_name;
}
