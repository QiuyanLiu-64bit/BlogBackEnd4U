package com.cqucs.blogbackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    @ApiModelProperty(value = "验证码记录编号")
    private Integer id;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "验证码生成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generated_at;
}
