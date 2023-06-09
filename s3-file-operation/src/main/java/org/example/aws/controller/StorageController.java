package org.example.aws.controller;

import org.example.aws.service.StorageService;
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
    public List<String> listAllFiles() {
        List<String> files = storageService.listAllFiles();
        return files;
    }

}
