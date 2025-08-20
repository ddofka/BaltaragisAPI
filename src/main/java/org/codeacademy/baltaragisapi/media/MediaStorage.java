package org.codeacademy.baltaragisapi.media;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

/**
 * Interface for media storage operations.
 * Provides a clean abstraction for storing and retrieving media files.
 * Implementation can be swapped between local storage (dev) and cloud storage (prod).
 */
public interface MediaStorage {
    
    /**
     * Upload a file to storage and return the public URL.
     * 
     * @param file The file to upload
     * @param productId The product ID to associate with the file
     * @param filename Optional custom filename, if null will use original filename
     * @return The public URL where the file can be accessed
     * @throws IOException If file operations fail
     */
    String uploadFile(MultipartFile file, Long productId, String filename) throws IOException;
    
    /**
     * Get the public URL for a file.
     * 
     * @param productId The product ID
     * @param filename The filename
     * @return The public URL
     */
    String getFileUrl(Long productId, String filename);
    
    /**
     * Delete a file from storage.
     * 
     * @param productId The product ID
     * @param filename The filename
     * @return true if deletion was successful
     */
    boolean deleteFile(Long productId, String filename);
    
    /**
     * List all files for a product.
     * 
     * @param productId The product ID
     * @return List of filenames
     */
    List<String> listProductFiles(Long productId);
    
    /**
     * Check if a file exists.
     * 
     * @param productId The product ID
     * @param filename The filename
     * @return true if file exists
     */
    boolean fileExists(Long productId, String filename);
}
