package com.atguigu.gmall.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

public class MyUploadUtil {
    public static String upload_image(MultipartFile multipartFile) {

        String url = "http://192.168.0.67";
        String trackPath = MyUploadUtil.class.getClassLoader().
                getResource("tracker.properties").getPath();

        try {


            ClientGlobal.init(trackPath);

            //        先获得tracker
            TrackerClient trackClient = new TrackerClient();
            TrackerServer connection = trackClient.getConnection();

//          在获得storage
            StorageClient storageClient = new StorageClient(connection);

            String originalFilename = multipartFile.getOriginalFilename();
            int i = originalFilename.lastIndexOf(".");
            String substring = originalFilename.substring(i + 1);


            String[] paths = storageClient.upload_file(multipartFile.getBytes(),substring , null);

             for (String path : paths) {
//            System.out.println(jpg);
                url = url + "/" + path;
            }
        } catch (Exception e) {

        }
        return url;
//        System.out.println(url);
    }
}
