package com.cqucs.blogbackend.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicsDTO {
    @ApiModelProperty(value = "用户编号")
    private Integer u_id;

    @ApiModelProperty(value = "动态内容")
    private String d_content;

    @ApiModelProperty(value = "动态图片1")
    private String d_image1;

    @ApiModelProperty(value = "动态图片2")
    private String d_image2;

    @ApiModelProperty(value = "动态图片3")
    private String d_image3;

    @ApiModelProperty(value = "动态图片4")
    private String d_image4;
}
