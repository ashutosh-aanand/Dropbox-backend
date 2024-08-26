package com.project.dropbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileMetadataRepository fileMetadataRepository;

    private static final String UPLOAD_DIR = "./uploads";


    public FileMetadata storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        Files.copy(file.getInputStream(), filePath);

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setName(fileName);
        fileMetadata.setType(file.getContentType());
        fileMetadata.setSize(file.getSize());
        fileMetadata.setPath(filePath.toString());
        fileMetadata.setCreatedAt(Timestamp.from(Instant.now()));

        return fileMetadataRepository.save(fileMetadata);
    }

    public byte[] readFile(String fileId) throws IOException {
        Optional<FileMetadata> metadataOptional = fileMetadataRepository.findById(fileId);
        if (metadataOptional.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "File not found");
        }

        Path filePath = Paths.get(metadataOptional.get().getPath());
        return Files.readAllBytes(filePath);
    }
}
