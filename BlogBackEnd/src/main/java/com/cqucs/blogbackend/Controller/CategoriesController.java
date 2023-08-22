package com.cqucs.blogbackend.Controller;


import com.cqucs.blogbackend.entity.Article;
import com.cqucs.blogbackend.entity.Categories;
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

    @ApiOperation(value = "根据分类ID查询分类详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "根据分类ID查询分类详细信息")
    @GetMapping("/getcategory/{cg_id}")
    public OperateResult getById(@PathVariable Integer cg_id){
        try {
            String sql = "select * from categories where cg_id=?";
            Categories category = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<>(Categories.class),cg_id);
            return new OperateResult(200, "数据查询成功", category);
        }catch(Exception e){//Exception是所有异常的父类
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
    public OperateResult update(Categories category){
        String sql = "update categories set cg_name where cg_id=?";
        //准备占位符的参数
        Object[] args = {category.getCg_name(),category.getCg_id()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据修改成功",null) ;
        }else{
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
        //将从前端接受的数据保存到数据库中
        //如下SQL语句可能会造成SQL注入问题
        //String sql = "insert into users values(default,'赵敏','zhaomin','123456',20,0)";
        //int num = jdbcTemplate.update(sql);
        //使用占位符的方式去编写SQL语句
        String sql = "insert into categories values(default,?)";
        //准备占位符的参数
        Object[] args = {category.getCg_name()};
        int num = jdbcTemplate.update(sql,args);
        if(num>0){
            return new OperateResult(200,"数据添加成功",null) ;
        }else{
            return new OperateResult(500,"数据添加失败",null) ;
        }
    }

}