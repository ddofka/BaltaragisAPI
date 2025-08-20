package org.codeacademy.baltaragisapi.service;

import org.codeacademy.baltaragisapi.entity.Product;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.codeacademy.baltaragisapi.exception.ValidationException;
import org.codeacademy.baltaragisapi.media.MediaStorage;
import org.codeacademy.baltaragisapi.mapper.ProductPhotoMapper;
import org.codeacademy.baltaragisapi.repository.ProductPhotoRepository;
import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test for PhotoUploadService.
 */
@ExtendWith(MockitoExtension.class)
class PhotoUploadServiceTest {
    
    @Mock
    private MediaStorage mediaStorage;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private ProductPhotoRepository productPhotoRepository;
    
    @Mock
    private ProductPhotoMapper productPhotoMapper;
    
    private PhotoUploadService photoUploadService;
    
    @BeforeEach
    void setUp() {
        photoUploadService = new PhotoUploadService(
            mediaStorage, productRepository, productPhotoRepository, productPhotoMapper
        );
    }
    
    @Test
    void testUploadPhoto_Success() throws IOException {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        
        MockMultipartFile file = new MockMultipartFile(
            "test.jpg", 
            "test.jpg", 
            "image/jpeg", 
            "test image content".getBytes()
        );
        
        BufferedImage mockImage = mock(BufferedImage.class);
        when(mockImage.getWidth()).thenReturn(100);
        when(mockImage.getHeight()).thenReturn(100);
        
        ProductPhoto savedPhoto = new ProductPhoto();
        savedPhoto.setId(1L);
        savedPhoto.setUrl("http://localhost:8080/media/1/test.jpg");
        savedPhoto.setWidth(100);
        savedPhoto.setHeight(100);
        savedPhoto.setAlt("Test image");
        savedPhoto.setSortOrder(1);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mediaStorage.uploadFile(any(), eq(productId), any())).thenReturn("http://localhost:8080/media/1/test.jpg");
        when(productPhotoRepository.save(any(ProductPhoto.class))).thenReturn(savedPhoto);
        when(productPhotoRepository.findMaxSortOrderByProductId(productId)).thenReturn(0);
        
        // When
        try (MockedStatic<ImageIO> imageIOMock = mockStatic(ImageIO.class)) {
            imageIOMock.when(() -> ImageIO.read(any(InputStream.class))).thenReturn(mockImage);
            
            ProductPhoto result = photoUploadService.uploadPhoto(productId, file, "Test image", null);
            
            // Then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("http://localhost:8080/media/1/test.jpg", result.getUrl());
            assertEquals(100, result.getWidth());
            assertEquals(100, result.getHeight());
            assertEquals("Test image", result.getAlt());
            assertEquals(1, result.getSortOrder());
            
            verify(mediaStorage).uploadFile(eq(file), eq(productId), any());
            verify(productPhotoRepository).save(any(ProductPhoto.class));
        }
    }
    
    @Test
    void testUploadPhoto_ProductNotFound() throws IOException {
        // Given
        Long productId = 999L;
        MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "content".getBytes());
        
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(ValidationException.class, () -> 
            photoUploadService.uploadPhoto(productId, file, null, null)
        );
        
        verify(mediaStorage, never()).uploadFile(any(), any(), any());
    }
    
    @Test
    void testUploadPhoto_InvalidFileType() throws IOException {
        // Given
        Long productId = 1L;
        MockMultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", "content".getBytes());
        
        // When & Then
        assertThrows(ValidationException.class, () -> 
            photoUploadService.uploadPhoto(productId, file, null, null)
        );
        
        verify(mediaStorage, never()).uploadFile(any(), any(), any());
    }
    
    @Test
    void testUploadPhoto_FileTooLarge() throws IOException {
        // Given
        Long productId = 1L;
        byte[] largeContent = new byte[11 * 1024 * 1024]; // 11MB
        MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", largeContent);
        
        // When & Then
        assertThrows(ValidationException.class, () -> 
            photoUploadService.uploadPhoto(productId, file, null, null)
        );
        
        verify(mediaStorage, never()).uploadFile(any(), any(), any());
    }
    

}
