package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.Size;

@Value
@Schema(name = "UpdatePageRequest", description = "Request to update an existing page")
public class UpdatePageRequest {
    
    @Size(max = 255, message = "Page title must be 255 characters or less")
    @Schema(example = "Home")
    String title;
    
    @Schema(example = "# Welcome to Baltaragis", description = "Page content in Markdown")
    String contentMd;
    
    @Schema(example = "true")
    Boolean isPublished;
}
