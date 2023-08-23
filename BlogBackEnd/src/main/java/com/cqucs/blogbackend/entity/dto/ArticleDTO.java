package com.cqucs.blogbackend.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@ApiModel(value = "文章信息", description="文章信息")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {

    @ApiModelProperty(value = "用户编号")
    private Integer u_id;

    @ApiModelProperty(value = "分类编号")
    private Integer cg_id;

    @ApiModelProperty(value = "文章摘要")
    private String a_tabloid;

    @ApiModelProperty(value = "文章内容")
    private String a_content;

    @ApiModelProperty(value = "文章标签")
    private String a_tags;

    @ApiModelProperty(value = "文章标题")
    private String a_title;

    @ApiModelProperty(value = "文章发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp a_deliver_time;

    @ApiModelProperty(value = "文章封面")
    private String a_cover_url;
}
