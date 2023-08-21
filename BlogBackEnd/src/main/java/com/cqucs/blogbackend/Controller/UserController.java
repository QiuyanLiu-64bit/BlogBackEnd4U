package com.cqucs.blogbackend.Controller;

import com.cqucs.blogbackend.entity.User;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "根据用户ID查询用户详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "根据用户ID查询用户详细信息")
    @GetMapping("/getuser/{u_id}")
    public OperateResult getById(@PathVariable Integer u_id){
        try {
            String sql = "select * from users where u_id=?";
            User user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),u_id);
            return new OperateResult(200, "数据查询成功", user);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }


    @ApiOperation(value = "查询所有用户数据",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/list")//http://localhost:8080/user/list
    public OperateResult queryAll(){
        try {
            String sql = "select * from users";
            List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
            return new OperateResult(200, "数据查询成功", users);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "删除用户数据",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")

    @DeleteMapping("/delete/{u_id}")
    public OperateResult delete(@PathVariable Integer u_id){
        try{
            String sql = "delete from users where u_id=?";
            jdbcTemplate.update(sql,u_id);
            return new OperateResult(200,"数据删除成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据删除失败",null) ;
        }

    }


    @ApiOperation(value = "修改用户数据",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/update")
    public OperateResult update(User user){
        String sql = "update users set u_nickname=?,u_birth_date=?,u_register_date=?,u_signature=?,u_avatar_url=?,u_email=?,u_password=?,u_type=? where u_id=?";
        //准备占位符的参数
        Object[] args = {user.getU_nickname(),user.getU_birth_date(),user.getU_register_date(),user.getU_signature(),user.getU_avatar_url(),user.getU_email(),user.getU_password(),user.getU_type(),user.getU_id()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据修改成功",null) ;
        }else{
            return new OperateResult(500,"数据修改失败",null) ;
        }
    }
    @ApiOperation(value = "添加/注册用户数据",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/create")
    public OperateResult create(@RequestBody User user){
        //将从前端接受的数据保存到数据库中
        //如下SQL语句可能会造成SQL注入问题
        //String sql = "insert into users values(default,'赵敏','zhaomin','123456',20,0)";
        //int num = jdbcTemplate.update(sql);
        //使用占位符的方式去编写SQL语句
        String sql = "insert into users values(default,?,?,?,?,?,?,?,?)";
        //准备占位符的参数
        Object[] args = {user.getU_email(),user.getU_password(),user.getU_type(),user.getU_nickname(),user.getU_birth_date(),user.getU_register_date(),user.getU_signature(),user.getU_avatar_url()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据添加成功",null) ;
        }else{
            return new OperateResult(500,"数据添加失败",null) ;
        }
    }
}