package org.codeacademy.baltaragisapi.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Value
@Schema(name = "UpdateArtistProfileRequest", description = "Request to update artist profile")
public class UpdateArtistProfileRequest {
    
    @NotBlank(message = "Artist name is required")
    @Size(max = 255, message = "Artist name must be 255 characters or less")
    @Schema(example = "Baltaragis", required = true)
    String name;
    
    @Schema(example = "Contemporary artist blending traditional forms.", description = "Artist biography")
    String bio;
    
    @Schema(example = "https://example.com/hero.jpg", description = "Hero image URL")
    String heroImageUrl;
    
    @Schema(example = "{\"instagram\":\"https://instagram.com/baltaragis\",\"twitter\":\"https://x.com/baltaragis\"}", description = "Social media links as JSON")
    String socials;
}
