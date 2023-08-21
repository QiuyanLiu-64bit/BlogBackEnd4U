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
public class User {
    @ApiModelProperty(value = "用户编号")
    private Integer u_id;

    @ApiModelProperty(value = "用户邮箱")
    private String u_email;

    @ApiModelProperty(value = "用户密码")
    private String u_password;

    @ApiModelProperty(value = "用户类型")
    private Boolean u_type;

    @ApiModelProperty(value = "用户昵称")
    private String u_nickname;

    @ApiModelProperty(value = "用户生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date u_birth_date;

    @ApiModelProperty(value = "用户注册日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date u_register_date;

    @ApiModelProperty(value = "用户个性签名")
    private String u_signature;

    @ApiModelProperty(value = "用户头像URL")
    private String u_avatar_url;
}
