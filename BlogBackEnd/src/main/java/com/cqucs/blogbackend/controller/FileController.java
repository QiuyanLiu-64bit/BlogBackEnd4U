package com.cqucs.blogbackend.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            notes = "code:200 表示成功")
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


    @ApiOperation(value = "文件下载",
            protocols = "http",
            httpMethod = "GET",
            consumes = "application/json",
            response = OperateResult.class,
            notes = "code:200 表示成功")
    // 创建一个GetMapping方法，接收文件名作为参数
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@ApiParam(name = "fileName",value = "输入文件名",required = true) @RequestParam String fileName) {
        try{
            // 定义静态文件路径
            String dir = rootPath + "\\staticFiles\\";
            // 根据文件名拼接完整的路径
            String filePath = dir + fileName;
            System.out.println(filePath);
            File file = new File(filePath);

            if (!file.exists()) {
                // 文件不存在，返回错误响应或其他处理
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 创建一个HttpHeaders对象，用于设置响应头
            HttpHeaders headers = new HttpHeaders();
            // 设置响应头为附件形式，并指定文件名
            headers.setContentDispositionFormData("attachment", fileName);
            // 设置响应类型为自动判断
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // 创建一个FileSystemResource对象，用于包装文件
            FileSystemResource resource = new FileSystemResource(file);

            // 返回一个ResponseEntity对象，包含资源，响应头和状态码
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
