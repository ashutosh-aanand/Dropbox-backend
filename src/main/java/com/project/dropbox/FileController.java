package com.project.dropbox;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> upload(@RequestParam("file") MultipartFile file) {
        try {
            FileMetadata savedFileMetadata = fileStorageService.storeFile(file);
            return ResponseEntity.ok(savedFileMetadata);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
