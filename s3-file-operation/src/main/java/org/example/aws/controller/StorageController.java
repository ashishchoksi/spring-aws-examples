package org.example.aws.controller;

import org.example.aws.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        String response = storageService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listAllFiles() {
        List<String> files = storageService.listAllFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource byteArrayResource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", fileName))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(byteArrayResource);
    }

    @DeleteMapping("/file/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        storageService.deleteFile(fileName);
        return ResponseEntity.ok("File deleted successfully!");
    }
}
