package com.wlkg.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wlkg.controller.UploadController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;


@Service
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Autowired
    FastFileStorageClient storageClient;

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                logger.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }

            //将图片上传到FastDFS
            //获取文件的后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");

            //上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(),extension,null);


            //返回完整路径
            String url = "http://image.wlkg.com/" + storePath.getFullPath();
            return url;


            // 2、保存图片
            // 2.1、生成保存目录
           /* File dir = new File("F:\\file\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }*/
            // 2.2、保存图片
           /* file.transferTo(new File(dir, file.getOriginalFilename()));*/
            // 2.3、拼接图片地址

        } catch (Exception e) {
            return null;
        }
    }
}
