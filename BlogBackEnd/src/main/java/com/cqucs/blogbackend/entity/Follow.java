package com.cqucs.blogbackend.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {
    @ApiModelProperty(value = "用户编号")
    private Integer u_id;
    @ApiModelProperty(value = "被关注用户的编号")
    private Integer use_u_id;
}
