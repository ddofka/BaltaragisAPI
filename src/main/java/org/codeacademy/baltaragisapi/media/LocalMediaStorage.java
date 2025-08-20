package org.codeacademy.baltaragisapi.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Local file system implementation of MediaStorage for development.
 * Stores files under /media/{productId}/ directory structure.
 */
@Service
public class LocalMediaStorage implements MediaStorage {
    
    @Value("${app.media.base-path:media}")
    private String basePath;
    
    @Value("${app.media.base-url:http://localhost:8080/media}")
    private String baseUrl;
    
    @Override
    public String uploadFile(MultipartFile file, Long productId, String filename) throws IOException {
        if (filename == null) {
            filename = file.getOriginalFilename();
        }
        
        Path productDir = getProductDirectory(productId);
        Files.createDirectories(productDir);
        
        Path filePath = productDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return getFileUrl(productId, filename);
    }
    
    @Override
    public String getFileUrl(Long productId, String filename) {
        return baseUrl + "/" + productId + "/" + filename;
    }
    
    @Override
    public boolean deleteFile(Long productId, String filename) {
        try {
            Path filePath = getProductDirectory(productId).resolve(filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public List<String> listProductFiles(Long productId) {
        try {
            Path productDir = getProductDirectory(productId);
            if (!Files.exists(productDir)) {
                return List.of();
            }
            
            return Files.list(productDir)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }
    
    @Override
    public boolean fileExists(Long productId, String filename) {
        Path filePath = getProductDirectory(productId).resolve(filename);
        return Files.exists(filePath);
    }
    
    private Path getProductDirectory(Long productId) {
        return Paths.get(basePath, productId.toString());
    }
}
