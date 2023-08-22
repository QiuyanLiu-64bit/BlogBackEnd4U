package com.cqucs.blogbackend.controller;

import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "文件模块")
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    // 项目根目录路径
    private String rootPath;

    // 构造方法中获取项目根目录路径
    public FileController() {
        rootPath = System.getProperty("user.dir");
    }

    @ApiOperation(value = "文件上传",
            protocols = "http",
            httpMethod = "POST",
            consumes = "application/json",
            response = OperateResult.class,
            notes = "根据用户ID查询用户详细信息")
    // 处理上传文件的请求
    @PostMapping("/upload")
    public OperateResult uploadFile(@RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (file.isEmpty()) {
            return new OperateResult(500, "请选择要上传的文件", null);
        }
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 生成新的文件名
            fileName = UUID.randomUUID() + suffixName;
            // 创建保存文件的目录
            File dir = new File(rootPath + "/staticFiles");
            // 如果目录不存在，则创建
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 创建保存文件的对象
            File dest = new File(dir.getAbsolutePath() + "/" + fileName);
            // 将上传的文件保存到目标文件中
            file.transferTo(dest);
            // 返回相对路径
            return new OperateResult(200, "上传成功", "/StaticFiles/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new OperateResult(500, "上传失败", null);
        }
    }
}
