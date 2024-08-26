package com.project.dropbox;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> readFile(@PathVariable String fileId) {
        try {
            byte[] fileData = fileStorageService.readFile(fileId);

            String fileType = "application/octet-stream";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileType));

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<FileMetadata> updateFile(
            @PathVariable String fileId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "newFileName", required = false) String newFileName) {

        try {
            FileMetadata updatedMetadata = fileStorageService.updateFile(fileId, file, newFileName);
            return ResponseEntity.ok(updatedMetadata);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
