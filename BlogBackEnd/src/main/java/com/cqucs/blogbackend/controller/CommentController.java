package com.cqucs.blogbackend.controller;

import com.cqucs.blogbackend.entity.Comment;
import com.cqucs.blogbackend.entity.dto.CommentCDTO;
import com.cqucs.blogbackend.entity.dto.CommentUDTO;
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

    @ApiOperation(value = "根据文章ID查询评论详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "根据文章ID查询评论详细信息")
    @GetMapping("/getcommentbyarticle/{a_id}")
    public OperateResult getByArticleId(@PathVariable Integer a_id){
        try {
            String sql = "select * from comments where a_id=?";
            List<Comment> comments = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Comment.class),a_id);
            return new OperateResult(200, "数据查询成功", comments);
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
    public OperateResult update(CommentUDTO comment){
        try{
        String sql = "update comments set c_time=NOW(),c_content=? where c_id=?";
        Object[] args = {comment.getC_content(),comment.getC_id()};
        jdbcTemplate.update(sql,args);
        return new OperateResult(200,"数据修改成功",null) ;
        }catch(Exception e){
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
    public OperateResult create(@RequestBody CommentCDTO comment){
        try{
        String sql = "insert into comments values(default,?,?,NOW(),?)";
        Object[] args = {comment.getU_id(),comment.getA_id(),comment.getC_content()};
        jdbcTemplate.update(sql,args);
        return new OperateResult(200,"数据添加成功",null) ;
        }catch(Exception e){
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
