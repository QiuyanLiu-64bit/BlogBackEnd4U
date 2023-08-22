package com.cqucs.blogbackend.controller;

import io.swagger.annotations.ApiParam;
import com.cqucs.blogbackend.tools.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
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
            consumes = "application/octet-stream",
            response = OperateResult.class,
            notes = "code:200 表示成功")
    // 处理上传文件的请求
    @GetMapping("/download")
    public OperateResult downloadFile(@ApiParam(name = "fileName", value = "输入文件名", required = true) @RequestParam String fileName) {
        /*try {
            // 构建文件完整路径
            String dir = rootPath + "\\staticFiles\\";
            String filePath = dir + fileName;

            File file = new File(filePath);

            if (!file.exists()) {
                // 文件不存在，返回错误响应或其他处理
                return new OperateResult(500, "文件不存在", null);
            }
            log.info(file.getPath());
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            log.info("文件后缀名：" + ext);

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            return new OperateResult(500, "下载失败", null);
//            return ResponseEntity.ok("No!");
        }*/
        return new OperateResult(500, "下载失败", null);
    }
}
