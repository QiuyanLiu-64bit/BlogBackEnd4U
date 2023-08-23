package com.cqucs.blogbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.annotations.ApiParam;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "文件模块")
@Slf4j
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

    /*private static final Logger log = LoggerFactory.getLogger(FileController.class);*/

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
            return new OperateResult(200, "上传成功",  rootPath + "\\StaticFiles\\" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new OperateResult(500, "上传失败", null);
        }
    }


    @ApiOperation(value = "文件下载",
            protocols = "http",
            httpMethod = "GET",
            /*consumes = "application/octet-stream",*/
            response = OperateResult.class,
            notes = "code:200 表示成功")
    // 处理上传文件的请求
    /*String dir = rootPath + "\\staticFiles\\";
    String filePath = dir + fileName;

    File file = new File(filePath);*/
    /**
     * @param path     想要下载的文件的路径
     * @param response
     * @功能描述 下载文件:
     */
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@ApiParam(name = "fileName", value = "输入文件名", required = true) @RequestParam String fileName) throws Exception {
        String dir = rootPath + "\\staticFiles\\";
        String filePath = dir + fileName;
        InputStreamResource isr = new InputStreamResource(new FileInputStream(filePath));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=test1.png")
                .body(isr);
    }
}
