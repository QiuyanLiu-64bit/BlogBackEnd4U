package com.cqucs.blogbackend.controller;


import com.cqucs.blogbackend.entity.Article;
import com.cqucs.blogbackend.entity.Categories;
import com.cqucs.blogbackend.entity.vo.*;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "分类模块")
@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoriesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "根据分类ID查询文章详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getcategory/{cg_id}")
    public OperateResult getById(@PathVariable Integer cg_id){
        try {
            String sql = "SELECT a.* " +
                    "FROM articles a " +
                    "WHERE a.cg_id = ? and a.a_deliver_time <= NOW()";

            List<Article> category = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Article.class),cg_id);
            return new OperateResult(200, "数据查询成功", category);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }


    @ApiOperation(value = "查询所有分类信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/list")
    public OperateResult queryAll(){
        try {
            String sql = "select * from categories";
            List<Categories> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Categories.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "删除分类数据",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")

    @DeleteMapping("/delete/{cg_id}")
    public OperateResult delete(@PathVariable Integer cg_id){
        try{
            String sql = "delete from categories where cg_id=?";
            jdbcTemplate.update(sql,cg_id);
            return new OperateResult(200,"数据删除成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据删除失败",null) ;
        }

    }


    @ApiOperation(value = "修改分类数据",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/update")
    public OperateResult update(@RequestBody Categories category){
        try{
        String sql = "update categories set cg_name where cg_id=?";
        Object[] args = {category.getCg_name(),category.getCg_id()};
        jdbcTemplate.update(sql,args);
        return new OperateResult(200,"数据修改成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据修改失败",null) ;
        }
    }
    @ApiOperation(value = "添加分类数据",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/create")
    public OperateResult create(@RequestBody Categories category){
        try{
        String sql = "insert into categories values(default,?)";
        Object[] args = {category.getCg_name()};
        jdbcTemplate.update(sql,args);
        return new OperateResult(200,"数据添加成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据添加失败",null) ;
        }
    }

    @ApiOperation(value = "查询每个类下的文章量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getarticle")
    public OperateResult getArticle(){
        try {
            String sql = "SELECT c.cg_id, c.cg_name, COUNT(a.a_id) AS total_articles\n" +
                    "FROM categories c\n" +
                    "LEFT JOIN articles a ON c.cg_id = a.cg_id\n" +
                    "WHERE a.a_deliver_time <= NOW()\n"+
                    "GROUP BY c.cg_id, c.cg_name;\n";
            List<CategoriesNumVO> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoriesNumVO.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "查询每个类下的点赞量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getthumbsup")
    public OperateResult getThumbsUp(){
        try {
            String sql = "SELECT c.cg_id, c.cg_name, COUNT(al.u_id) AS total_likes\n" +
                    "FROM categories c\n" +
                    "LEFT JOIN articles a ON c.cg_id = a.cg_id\n" +
                    "LEFT JOIN article_likes al ON a.a_id = al.a_id\n" +
                    "GROUP BY c.cg_id, c.cg_name;\n";
            List<CategoriesLikeVO> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoriesLikeVO.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "查询每个类下的点赞率",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getthumbsuprate")
    public OperateResult getThumbsUpRate(){
        try {
            String sql = "SELECT c.cg_id, c.cg_name,\n" +
                    "       IFNULL(SUM(al.u_id IS NOT NULL), 0) AS total_likes,\n" +
                    "       IFNULL(COUNT(ra.v_id), 0) AS total_reads,\n" +
                    "       IFNULL(SUM(al.u_id IS NOT NULL) / COUNT(ra.v_id), 0) AS like_to_read_ratio\n" +
                    "FROM categories c\n" +
                    "LEFT JOIN articles a ON c.cg_id = a.cg_id\n" +
                    "LEFT JOIN article_likes al ON a.a_id = al.a_id\n" +
                    "LEFT JOIN read_article ra ON a.a_id = ra.a_id\n" +
                    "GROUP BY c.cg_id, c.cg_name;\n";
            List<CategoriesRateVO> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoriesRateVO.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "查询每个类下的评论量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getcomment")
    public OperateResult getComment(){
        try {
            String sql = "SELECT cg.cg_id, cg.cg_name, COUNT(c.c_id) AS total_comments\n" +
                    "FROM categories cg\n" +
                    "LEFT JOIN articles a ON cg.cg_id = a.cg_id\n" +
                    "LEFT JOIN comments c ON a.a_id = c.a_id\n" +
                    "GROUP BY cg.cg_id, cg.cg_name;\n";
            List<CategoriesComVO> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoriesComVO.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "查询每个类下的收藏量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getcollect")
    public OperateResult getCollect(){
        try {
            String sql = "SELECT cg.cg_id, cg.cg_name, COUNT(af.u_id) AS total_collects\n" +
                    "FROM categories cg\n" +
                    "LEFT JOIN articles a ON cg.cg_id = a.cg_id\n" +
                    "LEFT JOIN article_favorites af ON a.a_id = af.a_id\n" +
                    "GROUP BY cg.cg_id, cg.cg_name;\n";
            List<CategoriesColleVO> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoriesColleVO.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "查询每个类下的浏览量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getbrowse")
    public OperateResult getBrowse(){
        try {
            String sql = "SELECT cg.cg_id, cg.cg_name, COUNT(ra.u_id) AS total_browses\n" +
                    "FROM categories cg\n" +
                    "LEFT JOIN articles a ON cg.cg_id = a.cg_id\n" +
                    "LEFT JOIN read_article ra ON a.a_id = ra.a_id\n" +
                    "GROUP BY cg.cg_id, cg.cg_name;\n";
            List<CategoriesReadVO> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoriesReadVO.class));
            return new OperateResult(200, "数据查询成功", categories);
        }catch(Exception e){//Exception是所有异常的父类
            e.printStackTrace();
            return new OperateResult(500,"查询数据失败",null);
        }
    }
}
