package com.cqucs.blogbackend.Controller;

import com.cqucs.blogbackend.Configuration.bean.EmailBean;
import com.cqucs.blogbackend.entity.VerificationCode;
import com.cqucs.blogbackend.tools.OperateResult;
import com.cqucs.blogbackend.util.CodeGeneratorUtil;
import com.cqucs.blogbackend.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "实例演示-发送邮件")
@RestController
@AllArgsConstructor
@RequestMapping("/example")
@CrossOrigin//跨域
public class EmailController {

    private final EmailUtil emailUtil;

    public static final int TimeOut = 3*60;//3min

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public OperateResult SendEmail(@PathVariable String email) {
        try {
            Context context = new Context();
            context.setVariable("project", "博客项目");
            // 生成6位数字验证码
            String code = CodeGeneratorUtil.generateCode(6);

            String sql = "DELETE FROM verification_codes WHERE email = ?";
            jdbcTemplate.update(sql, email);
            sql = "INSERT INTO verification_codes VALUES (default, ?, ?, NOW())";
            Object[] args = {email, code};
            jdbcTemplate.update(sql, args);

            context.setVariable("code", code);
            EmailBean emailBean = EmailBean.builder()
                    .context(context)
                    .templateName("email")//模板名称.html
                    .subject("博客项目发送验证码")
                    .toUser(email)
                    .build();

            emailUtil.sendEmail(emailBean);
            return new OperateResult(200, "发送成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"发送失败",null);
        }
    }

    @ApiOperation(value = "核对验证码",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/{email}/verifycode")
    public OperateResult VerifyCode(@PathVariable String email,String authCode) {
        try {
            String sql = "select * from verification_codes where email = ?";
            List<VerificationCode> Coder = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(VerificationCode.class),email);
            if (Coder.isEmpty()) {
                return new OperateResult(500, "账号不存在", null);
            }
            VerificationCode firstCode = Coder.get(0);
            String trueCode = firstCode.getCode();
            //判断验证码是否正确
            if (!authCode.equals(trueCode)) {
                return new OperateResult(500, "验证码错误", null);
            }
            // 获取当前时间
            LocalDateTime currentDateTime = LocalDateTime.now();
            // 获取表中时间（假设是VerificationCode对象的generatedAt属性）
            LocalDateTime tableDateTime = firstCode.getGenerated_at();
            // 计算时间差值
            Duration duration = Duration.between(tableDateTime, currentDateTime);
            long seconds = duration.getSeconds();
            if (seconds > TimeOut) {
                sql = "DELETE FROM verification_codes WHERE email = ?";
                jdbcTemplate.update(sql, email);
                return new OperateResult(500, "验证码超时", null);
            }
            //修改密码成功删除redis中的验证码
            sql = "DELETE FROM verification_codes WHERE email = ?";
            jdbcTemplate.update(sql, email);
            return new OperateResult(200, "验证码正确", 1);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"验证失败",null);
        }
    }
}
