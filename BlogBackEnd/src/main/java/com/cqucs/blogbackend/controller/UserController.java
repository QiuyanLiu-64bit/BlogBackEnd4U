package com.cqucs.blogbackend.controller;

import com.cqucs.blogbackend.entity.dto.UserDTO;
import com.cqucs.blogbackend.entity.Follow;
import com.cqucs.blogbackend.entity.User;
import com.cqucs.blogbackend.entity.vo.UserVO;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @ApiOperation(value = "根据用户邮箱查询用户是否存在",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "根据用户邮箱查询用户是否存在")
    @GetMapping("/getuserbyemail")   //http://localjost:8888/user/getuserbyemail?u_email=xxx
    public OperateResult getByEmail(@ApiParam(name = "u_email",value = "邮箱地址",required = true)
                                    @RequestParam String u_email){
        try {
            /*String sql = "select * from users where u_email=?";
            User user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(User.class),u_email);*/
            String sql = "select count(*) from users where u_email=?";
            Integer count = jdbcTemplate.queryForObject(sql,Integer.class,u_email);
            if(count==0){
                return new OperateResult(200, "用户不存在", true);
            }
            return new OperateResult(200, "用户存在", true);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"程序内部错误",false);
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
    public OperateResult update(@RequestBody User user){
        String sql = "update users set u_nickname=?,u_gender=?,u_birth_date=?,u_register_date=?,u_signature=?,u_avatar_url=?,u_email=?,u_password=?,u_type=? where u_id=?";
        Object[] args = {user.getU_nickname(),user.getU_gender(),user.getU_birth_date(),user.getU_register_date(),user.getU_signature(),user.getU_avatar_url(),user.getU_email(),user.getU_password(),user.getU_type(),user.getU_id()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据修改成功",null) ;
        }else{
            return new OperateResult(500,"数据修改失败",null) ;
        }
    }

    @ApiOperation(value = "修改用户密码",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/updatepassword")
    public OperateResult updatePassword(@ApiParam(name="u_email",value="用户邮箱",required = true) String u_email,
                                        @ApiParam(name="u_password",value="用户密码", required = true) String u_password){
        String sql = "update users set u_password=? where u_email=?";
        Object[] args = {u_password,u_email};
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
    public OperateResult create(@RequestBody UserDTO user){
        String sql = "insert into users values(default,?,?,?,?,?,Null,NOW(),?,?)";
        Object[] args = {user.getU_email(),user.getU_password(),user.getU_type(),user.getU_nickname(),user.getU_gender(),user.getU_signature(),user.getU_avatar_url()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据添加成功",null) ;
        }else{
            return new OperateResult(500,"数据添加失败",null) ;
        }
    }

    @ApiOperation(value = "用户登录查询",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "用户登录查询: code:200 表示成功")
    @GetMapping("/login")
    public OperateResult login(@ApiParam(name="email",value="账户名",required = true) String email,
                               @ApiParam(name="password",value="账户密码", required = true) String password){
        try {
            String sql = "select * from users where u_email = ?";
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class),email);
            if(password.equals(user.getU_password()))
                return new OperateResult(200, "登录成功", user);
            else
                return new OperateResult(510,"密码错误",null);
        }catch (EmptyResultDataAccessException e){
            return new OperateResult(506,"用户不存在",null);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"登录失败",null);
        }
    }


    @ApiOperation(value = "添加关注",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/follow")
    public OperateResult follow(@RequestBody Follow follow){
        String sql = "insert into follow values(?,?)";
        Object[] args = {follow.getU_id(),follow.getUse_u_id()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据添加成功",null) ;
        }else{
            return new OperateResult(500,"数据添加失败",null) ;
        }
    }

    @ApiOperation(value = "取消关注",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")

    @DeleteMapping("/unfollow/{use_u_id}")
    public OperateResult unfollow(@PathVariable Integer use_u_id,Integer u_id){
        try{
            String sql = "delete from follow where u_id=? and use_u_id=?";
            jdbcTemplate.update(sql,u_id,use_u_id);
            return new OperateResult(200,"数据删除成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据删除失败",null) ;
        }

    }

    @ApiOperation(value = "查询我关注的用户列表",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/followlist/{u_id}")
    public OperateResult followlist(@PathVariable Integer u_id){
        try {
            String sql = "SELECT u.u_avatar_url, u.u_nickname, f.use_u_id\n" +
                    "FROM follow f\n" +
                    "JOIN users u ON f.use_u_id = u.u_id\n" +
                    "WHERE f.u_id = ?;\n";
            List<UserVO> follows = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserVO.class),u_id);
            return new OperateResult(200, "数据查询成功", follows);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }
}
