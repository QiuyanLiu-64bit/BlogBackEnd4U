package com.cqucs.blogbackend.controller;


import cn.hutool.core.date.DateTime;
import com.cqucs.blogbackend.entity.Article;
import com.cqucs.blogbackend.entity.dto.ArticleDTO;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Api(tags = "文章模块")
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "根据文章ID查询文章详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "根据文章ID查询文章详细信息")
    @GetMapping("/getarticle/{a_id}")
    public OperateResult getById(@PathVariable Integer a_id){
        try {
            String sql = "select * from articles where a_id=? and a_deliver_time <= NOW()";
            Article article = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Article.class),a_id);
            return new OperateResult(200, "数据查询成功", article);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "根据用户ID查询用户所有文章信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "根据用户ID查询用户所有文章信息")
    @GetMapping("/getarticlebyuid/{u_id}")
    public OperateResult getByUid(@PathVariable Integer u_id){
        try {
            String sql = "select * from articles where u_id=? and a_deliver_time <= NOW()";
            List<Article> articles = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Article.class),u_id);
            return new OperateResult(200, "数据查询成功", articles);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }


    @ApiOperation(value = "查询所有已发布文章信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/list")
    public OperateResult queryAll(){
        try {
            String sql = "select * from articles where a_deliver_time <= NOW()";
            List<Article> articles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Article.class));
            return new OperateResult(200, "数据查询成功", articles);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "删除文章数据",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")

    @DeleteMapping("/delete/{a_id}")
    public OperateResult delete(@PathVariable Integer a_id){
        try{
            String sql = "delete from articles where a_id=?";
            jdbcTemplate.update(sql,a_id);
            return new OperateResult(200,"数据删除成功",null) ;
        }catch(Exception e){
            return new OperateResult(500,"数据删除失败",null) ;
        }

    }


    @ApiOperation(value = "修改文章数据",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/update")
    public OperateResult update(@RequestBody Article article) {
            String sql = "update articles set a_tabloid=?,a_content=?,a_tags=?,a_title=?,a_create_time=?,a_deliver_time=?,a_update_time=?,a_cover_url=? where a_id=?";
            //准备占位符的参数
            Object[] args = {article.getA_tabloid(), article.getA_content(), article.getA_tags(), article.getA_title(), article.getA_create_time(), article.getA_deliver_time(), article.getA_update_time(), article.getA_cover_url(), article.getA_id()};
            int num = jdbcTemplate.update(sql, args);
            if (num > 0) {
                return new OperateResult(200, "数据修改成功", null);
            } else {
                return new OperateResult(500, "数据修改失败", null);
            }
        }


    @ApiOperation(value = "添加文章数据",
            protocols = "http",
            httpMethod = "POST",
            consumes = "application/json",
            response = OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/create")
    public OperateResult create(@RequestBody ArticleDTO article) {
        LocalDateTime deliverTime = article.getA_deliver_time();
        LocalDateTime anotherTime = LocalDateTime.now();
        System.out.println(anotherTime);

        if (deliverTime == null) {
            deliverTime = LocalDateTime.now();
        }

        String sql = "insert into articles values(default,?,?,?,?,?,?,NOW(),?,NOW(),?)";
        // 准备占位符的参数
        Object[] args = {article.getU_id(), article.getCg_id(), article.getA_tabloid(), article.getA_content(), article.getA_tags(), article.getA_title(), deliverTime, article.getA_cover_url()};
        int num = jdbcTemplate.update(sql, args);

        if (num > 0) {
            return new OperateResult(200, "数据添加成功", null);
        } else {
            return new OperateResult(500, "数据添加失败", null);
        }
    }


    @ApiOperation(value = "添加收藏文章",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/addcollect")
    public OperateResult addcollect(@ApiParam(name="u_id",value="用户ID",required = true) String u_id,
                                    @ApiParam(name="a_id",value="文章ID", required = true) String a_id){
        String sql = "insert into article_favorites values(?,?)";
        Object[] args = {u_id,a_id};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"收藏成功",null) ;
        }else{
            return new OperateResult(500,"收藏失败",null) ;
        }
    }

    @ApiOperation(value = "查询文章是否已被收藏",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/iscollected")
    public OperateResult iscollected(@ApiParam(name="u_id",value="用户ID",required = true) String u_id,
                                     @ApiParam(name="a_id",value="文章ID", required = true) String a_id){
        try {
            String sql = "select count(*) from article_favorites where u_id=? and a_id=?";
            Object[] args = {u_id, a_id};
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, args);
            if (count == 0) {
                return new OperateResult(200, "未收藏", true);
            } else {
                return new OperateResult(200, "已收藏", false);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"错误",null);
        }
    }

    @ApiOperation(value = "取消收藏文章",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @DeleteMapping("/deletecollect/{u_id}/{a_id}")
    public OperateResult deletecollect(@ApiParam(name="u_id",value="用户ID",required = true) @PathVariable String u_id,
                                       @ApiParam(name="a_id",value="文章ID", required = true) @PathVariable String a_id){
        String sql = "delete from article_favorites where u_id=? and a_id=?";
        Object[] args = {u_id,a_id};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"取消收藏成功",null) ;
        }else{
            return new OperateResult(500,"取消收藏失败",null) ;
        }
    }

    @ApiOperation(value = "查询用户收藏文章",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getcollect/{u_id}")
    public OperateResult getcollect(@PathVariable String u_id){
        try {
            String sql = "SELECT a.*\n" +
                    "FROM article_favorites af\n" +
                    "JOIN articles a ON af.a_id = a.a_id\n" +
                    "WHERE af.u_id = ?;\n";
            List<Article> articles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Article.class),u_id);
            return new OperateResult(200, "数据查询成功", articles);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "查询文章收藏量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getcollectnum/{a_id}")
    public OperateResult getcollectnum(@PathVariable String a_id){
        try {
            String sql = "select count(*) from article_favorites where a_id=?";
            Integer collectnum = jdbcTemplate.queryForObject(sql,Integer.class,a_id);
            return new OperateResult(200, "数据查询成功", collectnum);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "添加文章点赞",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/addlike")
    public OperateResult addlike(@ApiParam(name="u_id",value="用户ID",required = true) String u_id,
                                 @ApiParam(name="a_id",value="文章ID", required = true) String a_id){
        String sql = "insert into article_likes values(?,?)";
        Object[] args = {u_id,a_id};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"点赞成功",null) ;
        }else{
            return new OperateResult(500,"点赞失败",null) ;
        }
    }

    @ApiOperation(value = "查询文章是否已被点赞",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/isliked")
    public OperateResult isliked(@ApiParam(name="u_id",value="用户ID",required = true) String u_id,
                                 @ApiParam(name="a_id",value="文章ID", required = true) String a_id){
        try {
            String sql = "select count(*) from article_likes where u_id=? and a_id=?";
            Object[] args = {u_id, a_id};
            int num = jdbcTemplate.queryForObject(sql, Integer.class, args);
            if (num == 0) {
                return new OperateResult(200, "未点赞", true);
            } else {
                return new OperateResult(200, "已点赞", false);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"错误",null);
        }
    }

    @ApiOperation(value = "取消文章点赞",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @DeleteMapping("/deletelike")
    public OperateResult deletelike(@ApiParam(name="u_id",value="用户ID",required = true) String u_id,
                                    @ApiParam(name="a_id",value="文章ID", required = true) String a_id){
        String sql = "delete from article_likes where u_id=? and a_id=?";
        Object[] args = {u_id,a_id};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"取消点赞成功",null) ;
        }else{
            return new OperateResult(500,"取消点赞失败",null) ;
        }
    }

    @ApiOperation(value = "查询文章点赞量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getlikenum/{a_id}")
    public OperateResult getlikenum(@PathVariable String a_id){
        try {
            String sql = "select count(*) from article_likes where a_id=?";
            Integer likenum = jdbcTemplate.queryForObject(sql,Integer.class,a_id);
            return new OperateResult(200, "数据查询成功", likenum);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "计算文章点赞率",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getlikerate/{a_id}")
    public OperateResult getlikerate(@ApiParam(name = "a_id",value = "输入文章ID",required = true) @RequestParam String a_id){
        try {
            String sql = "select count(*) from article_likes where a_id=?";
            Integer likenum = jdbcTemplate.queryForObject(sql,Integer.class,a_id);
            sql = "select count(*) from article_likes";
            Integer allnum = jdbcTemplate.queryForObject(sql,Integer.class);
            return new OperateResult(200, "数据查询成功", likenum/allnum);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }

    @ApiOperation(value = "关键词搜索文章",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/search")
    public OperateResult search(@ApiParam(name = "keyword",value = "输入关键词",required = true) @RequestParam  String keyword){
        try {
            String sql = "select * from `articles` where MATCH(`a_title`,`a_content`) AGAINST (? IN NATURAL LANGUAGE MODE) and a_deliver_time <= NOW();";
            List<Article> articles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Article.class),keyword);
            return new OperateResult(200, "搜索成功", articles);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"搜索失败",null);
        }
    }

    @ApiOperation(value = "文章阅读量+1",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/addviews")
    public OperateResult addviews(@ApiParam(name = "a_id",value = "输入文章ID",required = true) @RequestParam  String a_id,
                                  @ApiParam(name = "u_id",value = "输入用户ID",required = true) @RequestParam  String u_id){
        String sql = "insert into read_article values(default, ?, ?, NOW())";
        //准备占位符的参数
        Object[] args = {u_id,a_id};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"阅读量+1",null) ;
        }else{
            return new OperateResult(500,"阅读量+1失败",null) ;
        }
    }

    @ApiOperation(value = "查询文章阅读量",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getviews/{a_id}")
    public OperateResult getviews(@PathVariable String a_id){
        try {
            String sql = "select count(*) from read_article where a_id=?";
            Integer views = jdbcTemplate.queryForObject(sql,Integer.class,a_id);
            return new OperateResult(200, "数据查询成功", views);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"阅读量为空，值为0",null);
        }
    }

    @ApiOperation(value = "根据用户id查询关注的用户的文章信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getmessage/{u_id}")
    public OperateResult getmessage(@PathVariable String u_id){
        try {
            String sql = "select articles.* from articles inner join (SELECT * FROM follow WHERE u_id = ?) as f on articles.u_id = f.use_u_id";
            List<Article> message = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Article.class),u_id);
            return new OperateResult(200, "数据查询成功", message);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"数据查询失败",null);
        }
    }
}
