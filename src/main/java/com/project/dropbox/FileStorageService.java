package com.project.dropbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileMetadataRepository fileMetadataRepository;


    public String storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get("./uploads", fileName);

        Files.copy(file.getInputStream(), filePath);

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setName(fileName);
        fileMetadata.setType(file.getContentType());
        fileMetadata.setSize(file.getSize());
        fileMetadata.setPath(filePath.toString());
        fileMetadata.setCreatedAt(Timestamp.from(Instant.now()));

        fileMetadataRepository.save(fileMetadata);

        return "File uploaded Successfully!";
    }
}
