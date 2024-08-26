package com.project.dropbox.controller;

import com.project.dropbox.entity.FileMetadata;
import com.project.dropbox.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping
    public ResponseEntity<List<FileMetadata>> listAllFiles() {
        List<FileMetadata> files = fileStorageService.listAllFiles();
        return ResponseEntity.ok(files);
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

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
        try {
            fileStorageService.deleteFile(fileId);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
