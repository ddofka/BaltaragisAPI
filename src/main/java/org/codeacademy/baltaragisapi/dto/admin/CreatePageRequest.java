package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Value
@Schema(name = "CreatePageRequest", description = "Request to create a new page")
public class CreatePageRequest {
    
    @NotBlank(message = "Page title is required")
    @Size(max = 255, message = "Page title must be 255 characters or less")
    @Schema(example = "Home", required = true)
    String title;
    
    @NotBlank(message = "Page slug is required")
    @Size(max = 255, message = "Page slug must be 255 characters or less")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Schema(example = "home", required = true)
    String slug;
    
    @Schema(example = "# Welcome to Baltaragis", description = "Page content in Markdown")
    String contentMd;
    
    @Schema(example = "true", defaultValue = "false")
    Boolean isPublished = false;
}
