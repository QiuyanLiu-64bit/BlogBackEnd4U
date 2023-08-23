package com.cqucs.blogbackend.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDVO {
    @ApiModelProperty(value = "用户昵称")
    private String u_nickname;

    @ApiModelProperty(value = "用户头像URL")
    private String u_avatar_url;

    @ApiModelProperty(value = "被关注用户的u_id")
    private String use_u_id;
}
