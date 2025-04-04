package com.revision1.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class BucketService {
    @Autowired
    private AmazonS3 amazonS3;
    public String uploadFile(MultipartFile file, String bucketName) {
        if (file.isEmpty()){
            throw new IllegalArgumentException("can't upload empty file");
        }
        try {
            File convFile=new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);
            try {
                amazonS3.putObject(bucketName, convFile.getName(), convFile);
                return amazonS3.getUrl(bucketName,file.getOriginalFilename()).toString();
            }
            catch (AmazonS3Exception s3Exception) {
                return "unable to upload :" + s3Exception.getMessage();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error while uploading file: " + e.getMessage());
        }
    }
}

