package com.cqucs.blogbackend.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    @ApiModelProperty(value = "用户昵称")
    private String u_nickname;

    @ApiModelProperty(value = "用户头像URL")
    private String u_avatar_url;
}
