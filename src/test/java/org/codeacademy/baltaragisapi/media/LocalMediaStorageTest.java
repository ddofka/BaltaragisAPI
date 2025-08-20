package org.codeacademy.baltaragisapi.media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for LocalMediaStorage implementation.
 */
@TestPropertySource(properties = {
    "app.media.base-path=target/test-media",
    "app.media.base-url=http://localhost:8080/media"
})
class LocalMediaStorageTest {
    
    @TempDir
    Path tempDir;
    
    private LocalMediaStorage mediaStorage;
    
    @BeforeEach
    void setUp() {
        mediaStorage = new LocalMediaStorage();
        // Use reflection to set the base path and base URL for testing
        try {
            var basePathField = LocalMediaStorage.class.getDeclaredField("basePath");
            basePathField.setAccessible(true);
            basePathField.set(mediaStorage, tempDir.toString());
            
            var baseUrlField = LocalMediaStorage.class.getDeclaredField("baseUrl");
            baseUrlField.setAccessible(true);
            baseUrlField.set(mediaStorage, "http://localhost:8080/media");
        } catch (Exception e) {
            fail("Failed to set fields for testing", e);
        }
    }
    
    @Test
    void testUploadFile() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "test.jpg", 
            "test.jpg", 
            "image/jpeg", 
            "test image content".getBytes()
        );
        Long productId = 1L;
        
        // When
        String url = mediaStorage.uploadFile(file, productId, null);
        
        // Then
        assertNotNull(url);
        assertTrue(url.contains("/media/1/"));
        assertTrue(url.contains("test.jpg"));
        assertTrue(mediaStorage.fileExists(productId, "test.jpg"));
    }
    
    @Test
    void testGetFileUrl() {
        // Given
        Long productId = 1L;
        String filename = "test.jpg";
        
        // When
        String url = mediaStorage.getFileUrl(productId, filename);
        
        // Then
        assertEquals("http://localhost:8080/media/1/test.jpg", url);
    }
    
    @Test
    void testListProductFiles() throws IOException {
        // Given
        Long productId = 1L;
        MockMultipartFile file1 = new MockMultipartFile("file1.jpg", "file1.jpg", "image/jpeg", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2.jpg", "file2.jpg", "image/jpeg", "content2".getBytes());
        
        mediaStorage.uploadFile(file1, productId, null);
        mediaStorage.uploadFile(file2, productId, null);
        
        // When
        List<String> files = mediaStorage.listProductFiles(productId);
        
        // Then
        assertEquals(2, files.size());
        assertTrue(files.contains("file1.jpg"));
        assertTrue(files.contains("file2.jpg"));
    }
    
    @Test
    void testDeleteFile() throws IOException {
        // Given
        Long productId = 1L;
        MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "content".getBytes());
        mediaStorage.uploadFile(file, productId, null);
        
        // When
        boolean deleted = mediaStorage.deleteFile(productId, "test.jpg");
        
        // Then
        assertTrue(deleted);
        assertFalse(mediaStorage.fileExists(productId, "test.jpg"));
    }
}
