package com.cqucs.blogbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @ApiModelProperty(value = "评论编号")
    private Integer c_id;

    @ApiModelProperty(value = "用户编号")
    private Integer u_id;

    @ApiModelProperty(value = "文章编号")
    private Integer a_id;

    @ApiModelProperty(value = "评论时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date c_time;

    @ApiModelProperty(value = "评论内容")
    private String c_content;
}
