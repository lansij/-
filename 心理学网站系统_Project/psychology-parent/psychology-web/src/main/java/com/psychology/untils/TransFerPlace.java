package com.psychology.untils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.net.URL;

public class TransFerPlace {

    //将文件复制到resource/templates/static/img目录
    public static void transfer(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            System.out.println("复制文件的目标文件名称：" + file.getName());

            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:static/img/");
            URL url = resource.getURL();
            OutputStream outputStream = new FileOutputStream(url.getFile() + file.getName());

            byte b[] = new byte[100];
            int len = -1;
            while ((len = inputStream.read(b, 0, b.length)) != -1) {
                outputStream.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
