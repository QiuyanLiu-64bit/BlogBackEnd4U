package com.cqucs.blogbackend.Controller;

import com.cqucs.blogbackend.Configuration.bean.EmailBean;
import com.cqucs.blogbackend.tools.OperateResult;
import com.cqucs.blogbackend.util.CodeGeneratorUtil;
import com.cqucs.blogbackend.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.concurrent.TimeUnit;

@Api(tags = "实例演示-发送邮件")
@RestController
@AllArgsConstructor
@RequestMapping("/example")
@CrossOrigin//跨域
public class SendEmailController {

    private final EmailUtil emailUtil;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /*@ApiOperation(value = "纯文本验证码测试",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/sendEmailText")
    public void sendEmailText(String text) {
        List<String> attachmentList = new ArrayList<>();
        // 定义绝对路径
        attachmentList.add("D:\\xiaoxiaofeng.jpg");
        // 定义相对路径
        attachmentList.add("src/main/resources/templates/email.html");

        EmailBean emailBean = EmailBean.builder()
                .text(text)
                .subject("欢迎使用笑小枫个人博客")
                .toUser("1150640979@qq.com")
                .attachmentList(attachmentList)
                .build();

        emailUtil.sendEmail(emailBean);
    }*/

    @ApiOperation(value = "根据html模板发送验证码邮件",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/sendEmail/{email}")

    public void sendEmail(@PathVariable String email) {
        Context context = new Context();
        context.setVariable("project", "博客项目");
        // 生成6位数字验证码
        String code = CodeGeneratorUtil.generateCode(6);
        ValueOperations<String,String> vo = redisTemplate.opsForValue();
        vo.set(email, code, 300, TimeUnit.SECONDS);//5分钟过期
        context.setVariable("code", code);
        EmailBean emailBean = EmailBean.builder()
                .context(context)
                .templateName("email")//模板名称.html
                .subject("博客项目发送验证码")
                .toUser("email")
                .build();

        emailUtil.sendEmail(emailBean);
    }
}
