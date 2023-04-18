package com.example.reggie.controller;

import com.example.reggie.commen.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Cavan
 * @date 2023-04-01
 * @qq 2069543852
 */

/*
 *@Description: 文件的上传和下载
 *@Author: Cavan
 *@Date: 2023/4/1
 */
@Api(tags = "文件控制器")
@RequestMapping("/common")
@RestController
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    @ApiOperation("文件下载")
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("file:{}", file.toString());
        //获取上传文件的后缀
        String originalFilename = file.getOriginalFilename();
        int indexOf = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(indexOf);
        //使用uuid重新生成文件名，防止上传的文件名相同，覆盖原来的文件,并将源文件的后缀添加到UUID后，组成新的文件名
        String fileName = UUID.randomUUID().toString() + suffix;
        //判断目录是否存在，不存在则创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }


    /*
     *@Description: 文件下载
     *@Author: Cavan
     *@Date: 2023/4/1
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //通过输入读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("/image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
