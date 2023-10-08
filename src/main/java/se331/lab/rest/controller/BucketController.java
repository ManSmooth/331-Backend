package se331.lab.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletException;
import se331.lab.rest.util.CloudStorageHelper;

@RestController
public class BucketController {
    @Autowired
    CloudStorageHelper cloudStorageHelper;

    @Value("${firebase.bucket}")
    String bucketName;

    @PostMapping(value = "uploadfile")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "image") MultipartFile file)
            throws IOException, ServletException {

        return ResponseEntity.ok(cloudStorageHelper.getImageUrl(file, bucketName));
    }

    @PostMapping(value = "uploadimage")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "image") MultipartFile file)
            throws IOException, ServletException {

        return ResponseEntity.ok(cloudStorageHelper.getStorageFileDTO(file, bucketName));
    }

}
