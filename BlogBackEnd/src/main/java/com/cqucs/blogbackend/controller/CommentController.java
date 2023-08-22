package com.cqucs.blogbackend.controller;

import com.cqucs.blogbackend.entity.Comment;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "评论模块")
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "根据评论ID查询评论详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "根据评论ID查询评论详细信息")
    @GetMapping("/getcomment/{c_id}")
    public OperateResult getById(@PathVariable Integer c_id){
        try {
            String sql = "select * from comments where c_id=?";
            Comment comment = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Comment.class),c_id);
            return new OperateResult(200, "数据查询成功", comment);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }


    @ApiOperation(value = "查询所有评论信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/list")
    public OperateResult queryAll(){
        try {
            String sql = "select * from comments";
            List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class));
            return new OperateResult(200, "数据查询成功", comments);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "删除评论数据",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")

    @DeleteMapping("/delete/{c_id}")
    public OperateResult delete(@PathVariable Integer c_id){
        try{
            String sql = "delete from comments where c_id=?";
            jdbcTemplate.update(sql,c_id);
            return new OperateResult(200,"数据删除成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据删除失败",null) ;
        }

    }


    @ApiOperation(value = "修改评论数据",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/update")
    public OperateResult update(Comment comment){
        String sql = "update comments set c_time=?,c_content=? where c_id=?";
        //准备占位符的参数
        Object[] args = {comment.getC_time(),comment.getC_content(),comment.getC_id()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据修改成功",null) ;
        }else{
            return new OperateResult(500,"数据修改失败",null) ;
        }
    }
    @ApiOperation(value = "添加评论数据",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/create")
    public OperateResult create(@RequestBody Comment comment){
        //将从前端接受的数据保存到数据库中
        //如下SQL语句可能会造成SQL注入问题
        //String sql = "insert into users values(default,'赵敏','zhaomin','123456',20,0)";
        //int num = jdbcTemplate.update(sql);
        //使用占位符的方式去编写SQL语句
        String sql = "insert into comments values(default,?,?,?,?)";
        //准备占位符的参数
        Object[] args = {comment.getU_id(),comment.getA_id(),comment.getC_time(),comment.getC_content()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据添加成功",null) ;
        }else{
            return new OperateResult(500,"数据添加失败",null) ;
        }
    }

    @ApiOperation(value = "统计评论量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/commentCount")
    public OperateResult commentCount(){
        try {
            String sql = "select count(*) from comments";
            Integer count = jdbcTemplate.queryForObject(sql,Integer.class);
            return new OperateResult(200, "数据查询成功", count);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }
}
