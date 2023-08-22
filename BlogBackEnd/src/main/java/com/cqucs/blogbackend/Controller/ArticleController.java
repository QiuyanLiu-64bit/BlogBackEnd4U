package com.cqucs.blogbackend.Controller;


import com.cqucs.blogbackend.entity.Article;
import com.cqucs.blogbackend.entity.User;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
            notes = "根据用户ID查询用户详细信息")
    @GetMapping("/getarticle/{a_id}")
    public OperateResult getById(@PathVariable Integer a_id){
        try {
            String sql = "select * from articles where a_id=?";
            Article article = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Article.class),a_id);
            return new OperateResult(200, "数据查询成功", article);
        }catch(Exception e){//Exception是所有异常的父类
            return new OperateResult(500,"查询数据失败",null);
        }
    }


    @ApiOperation(value = "查询所有文章信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/list")
    public OperateResult queryAll(){
        try {
            String sql = "select * from articles";
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


    @ApiOperation(value = "修改用户数据",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/update")
    public OperateResult update(Article article){
        String sql = "update articles set a_tabloid=?,a_content=?,a_tags=?,a_title=?,a_views=?,a_create_time=?,a_deliver_time=?,a_update_time=?,a_cover_url=? where a_id=?";
        //准备占位符的参数
        Object[] args = {article.getA_tabloid(),article.getA_content(),article.getA_tags(),article.getA_title(),article.getA_views(),article.getA_create_time(),article.getA_deliver_time(),article.getA_update_time(),article.getA_cover_url(),article.getA_id()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据修改成功",null) ;
        }else{
            return new OperateResult(500,"数据修改失败",null) ;
        }
    }
    @ApiOperation(value = "添加文章数据",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/create")
    public OperateResult create(@RequestBody Article article){
        //将从前端接受的数据保存到数据库中
        //如下SQL语句可能会造成SQL注入问题
        //String sql = "insert into users values(default,'赵敏','zhaomin','123456',20,0)";
        //int num = jdbcTemplate.update(sql);
        //使用占位符的方式去编写SQL语句
        String sql = "insert into articles values(default,?,?,?,?,?,?,?,?,?,?,?)";
        //准备占位符的参数
        Object[] args = {article.getU_id(),article.getCg_id(),article.getA_tabloid(),article.getA_content(),article.getA_tags(),article.getA_title(),article.getA_views(),article.getA_create_time(),article.getA_deliver_time(),article.getA_update_time(),article.getA_cover_url()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据添加成功",null) ;
        }else{
            return new OperateResult(500,"数据添加失败",null) ;
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

    @ApiOperation(value = "取消收藏文章",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response=OperateResult.class,
            notes = "code:200 表示成功")
    @DeleteMapping("/deletecollect")
    public OperateResult deletecollect(@ApiParam(name="u_id",value="用户ID",required = true) String u_id,
                                       @ApiParam(name="a_id",value="文章ID", required = true) String a_id){
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
}
