package com.revision1.controller;

import com.revision1.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    private BucketService bucketService;

    public ImageController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping(path="/upload/file/{bucketName}/property",consumes= MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPhotos(
            @RequestParam MultipartFile file,
            @PathVariable String bucketName

    ){
        String imageUrl=bucketService.uploadFile(file, bucketName);
        return new ResponseEntity<>(imageUrl, HttpStatus.OK);
    }
}