package com.cqucs.blogbackend.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUDTO {
    @ApiModelProperty(value = "评论编号")
    private Integer c_id;

    @ApiModelProperty(value = "用户编号")
    private Integer u_id;

    @ApiModelProperty(value = "文章编号")
    private Integer a_id;

    @ApiModelProperty(value = "评论内容")
    private String c_content;
}
