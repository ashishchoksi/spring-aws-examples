package org.example.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private final Logger log = LoggerFactory.getLogger(StorageService.class);
    private final AmazonS3 s3Client;
    @Value("${application.bucket.name}")
    private String bucketName;
    public StorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        log.info("file upload request received for : {}", file.getOriginalFilename());
        File newFile = convertToFile(file);
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        log.info("Trying to upload file with name {} to bucket {}", fileName, bucketName);
        s3Client.putObject(bucketName, fileName, newFile);
        log.info("File uploaded successfully!!!");
        newFile.deleteOnExit();
        return fileName;
    }

    public List<String> listAllFiles() {
        log.info("Trying to list all the files...");
        List<String> files = new ArrayList<>();
        ObjectListing objects = s3Client.listObjects(bucketName);
        files.addAll(objects.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList()));
        return files;
    }

    private File convertToFile(MultipartFile file) {
        File newFile = new File(file.getOriginalFilename());
        try(FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            log.error("Failed to convert Multipart to File with Error: {}", e.getMessage());
            return newFile;
        }
        return newFile;
    }

    public byte[] downloadFile(String fileName) {
        log.info("Try to download file: {}", fileName);
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        byte[] data;
        try {
            data = IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            log.error("Failed to read content from s3");
            return null;
        }
        return data;
    }

    public void deleteFile(String fileName) {
        log.info("trying to delete file {}", fileName);
        s3Client.deleteObject(bucketName, fileName);
        log.info("file deleted successfully.");
    }
}
