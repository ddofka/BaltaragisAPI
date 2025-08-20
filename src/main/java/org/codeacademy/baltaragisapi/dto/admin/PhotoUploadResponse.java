package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

/**
 * Response DTO for photo upload operations.
 */
@Value
@Builder
@Schema(name = "PhotoUploadResponse", description = "Response after uploading a product photo")
public class PhotoUploadResponse {
    
    @Schema(example = "1", description = "Photo ID")
    Long id;
    
    @Schema(example = "http://localhost:8080/media/1/sunset_print_1234567890.jpg", description = "Public URL of the uploaded photo")
    String url;
    
    @Schema(example = "Sunset print product image", description = "Alt text for accessibility")
    String alt;
    
    @Schema(example = "1920", description = "Image width in pixels")
    Integer width;
    
    @Schema(example = "1080", description = "Image height in pixels")
    Integer height;
    
    @Schema(example = "1", description = "Sort order for display")
    Integer sortOrder;
    
    @Schema(example = "sunset_print_1234567890.jpg", description = "Filename of the uploaded file")
    String filename;
}
