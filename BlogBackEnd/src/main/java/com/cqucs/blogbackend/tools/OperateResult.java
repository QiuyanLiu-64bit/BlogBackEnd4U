package com.cqucs.blogbackend.tools;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("操作的结果")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateResult {

    @ApiModelProperty(value = "返回码", notes = "200:成功,400:参数错误,404:访问资源不存在,500:服务器错误...")
    private Integer code;
    @ApiModelProperty(value = "返回的消息")
    private String message;
    @ApiModelProperty(value = "返回的数据")
    private Object data;
}