package org.codeacademy.baltaragisapi.mapper;

import org.codeacademy.baltaragisapi.dto.admin.PhotoUploadResponse;
import org.codeacademy.baltaragisapi.entity.ProductPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for ProductPhoto entities.
 */
@Mapper(componentModel = "spring")
public interface ProductPhotoMapper {
    
    /**
     * Convert ProductPhoto entity to PhotoUploadResponse DTO.
     * 
     * @param photo The ProductPhoto entity
     * @return The PhotoUploadResponse DTO
     */
    @Mapping(target = "filename", expression = "java(extractFilenameFromUrl(photo.getUrl()))")
    PhotoUploadResponse toPhotoUploadResponse(ProductPhoto photo);
    
    /**
     * Extract filename from URL.
     * 
     * @param url The URL
     * @return The filename
     */
    default String extractFilenameFromUrl(String url) {
        if (url == null) return null;
        int lastSlashIndex = url.lastIndexOf('/');
        return lastSlashIndex >= 0 ? url.substring(lastSlashIndex + 1) : url;
    }
}
