package com.fileupload.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Controller
public class AdminController {


    @GetMapping
    public String index() {
        return "index";
    }

    @RequestMapping("manyfileupload")
    @ResponseBody
    public String manyfileupload(HttpServletRequest request, @RequestParam MultipartFile[] manyfileupload) {
        for (MultipartFile imgFile : manyfileupload) {
            String impPath = "D://" + imgFile.getOriginalFilename();
            File dest = new File(impPath);
            if (!dest.getParentFile().exists()) {
                // 如果不存在就新建文件夹
                dest.getParentFile().mkdirs();
            }
            String jpgSuffix = imgFile.getOriginalFilename().substring(imgFile.getOriginalFilename().lastIndexOf(".") + 1);
            if (jpgSuffix.equals("jpg") || jpgSuffix.equals("png")) {
                try {
                    imgFile.transferTo(new File(impPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "success";
    }


    @PostMapping("upload")
    @ResponseBody
    public String upload(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        String result = "";

        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    String filename = file.getOriginalFilename();
                    // 检测是否存在目录
                    String path = "D://" + filename;
                    File dest = new File(path);
                    if (!dest.getParentFile().exists()) {
                        // 如果不存在就新建文件夹
                        dest.getParentFile().mkdirs();
                    }
                    try {
                        file.transferTo(new File(path));
                    } catch (IOException e) {
                        result = "error";
                        e.printStackTrace();
                    }

                }
            }
        }
        result = "seccess";
        return result;

    }
}
