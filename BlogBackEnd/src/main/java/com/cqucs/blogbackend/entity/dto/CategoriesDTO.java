package com.cqucs.blogbackend.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDTO {

    @ApiModelProperty(value = "分类名称")
    private String cg_name;

    @ApiModelProperty(value = "分类图片")
    private String cg_img;
}
