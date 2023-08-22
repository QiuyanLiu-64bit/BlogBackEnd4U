package com.cqucs.blogbackend.controller;

import com.cqucs.blogbackend.entity.Dynamics;
import com.cqucs.blogbackend.entity.dto.DynamicsDTO;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "动态的模块")
@RestController
@RequestMapping("/Dynamics")
@CrossOrigin//跨域
public class DynamicsController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "添加动态数据",
            protocols = "http",
            httpMethod="POST",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @PostMapping("/create")
    public OperateResult create(@RequestBody DynamicsDTO dynamic){
        try {
            String sql = "insert into dynamics VALUES(default,?,?,NOW(),?,?,?,?)";
            Object[] args = {dynamic.getU_id(), dynamic.getD_content(), dynamic.getD_image1(), dynamic.getD_image2(), dynamic.getD_image3(), dynamic.getD_image4()};
            jdbcTemplate.update(sql, args);
            return new OperateResult(200, "动态数据添加成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"动态数据添加失败",null);
        }
    }

    @ApiOperation(value = "修改动态数据",
            protocols = "http",
            httpMethod="PUT",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @PutMapping("/update")
    public OperateResult update(@RequestBody DynamicsDTO dynamic){
        try {
            String sql = "update dynamics set u_id=?,d_content=?,d_create_time=NOW(),d_image1=?,d_image2=?,d_image3=?,d_image4=? where d_id=?";
            Object[] args = {dynamic.getU_id(), dynamic.getD_content(), dynamic.getD_image1(), dynamic.getD_image2(), dynamic.getD_image3(), dynamic.getD_image4()};
            jdbcTemplate.update(sql, args);
            return new OperateResult(200, "动态数据修改成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"动态数据修改失败",null);
        }
    }

    @ApiOperation(value = "删除动态数据",
            protocols = "http",
            httpMethod="DELETE",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @DeleteMapping("/delete/{d_id}")
    public OperateResult delete(@PathVariable Integer d_id){
        try {
            String sql = "delete from dynamics where d_id =?";
            jdbcTemplate.update(sql, d_id);
            return new OperateResult(200, "动态数据删除成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500, "动态数据删除失败", null);
        }
    }

    @ApiOperation(value = "按时间顺序降序查询所有动态数据",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "查询所有动态数据: code:200 表示成功")
    @GetMapping("/list")
    public OperateResult queryAll(){
        try {
            String sql = "select * from dynamics order by d_create_time desc";
            List<Dynamics> dynamics = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Dynamics.class));
            return new OperateResult(200, "动态数据查询成功", dynamics);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"动态数据查询失败",null);
        }
    }

    @ApiOperation(value = "根据动态ID查询动态详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "根据动态ID查询动态详细信息: code:200 表示成功")
    @GetMapping("/getdynamicbyd/{d_id}")
    public OperateResult getByD(@PathVariable Integer d_id){
        try {
            String sql = "select * from dynamics where d_id = ?";
            Dynamics dynamic = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Dynamics.class),d_id);
            return new OperateResult(200, "动态数据查询成功", dynamic);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"动态数据查询失败",null);
        }
    }

    @ApiOperation(value = "根据用户ID查询动态详细信息",
            protocols = "http",
            httpMethod="GET",
            consumes="application/json",
            response= OperateResult.class,
            notes = "code:200 表示成功")
    @GetMapping("/getdynamicbyu/{u_id}")
    public OperateResult getByU(@PathVariable Integer u_id){
        try {
            String sql = "select * from dynamics where u_id = ?";
            List<Dynamics> dynamic = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Dynamics.class),u_id);
            return new OperateResult(200, "动态数据查询成功", dynamic);
        }catch (Exception e){
            e.printStackTrace();
            return new OperateResult(500,"动态数据查询失败",null);
        }
    }
}
