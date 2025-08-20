package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.admin.UpdateArtistProfileRequest;
import org.codeacademy.baltaragisapi.entity.ArtistProfile;
import org.codeacademy.baltaragisapi.service.AdminArtistService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/artist")
@Tag(name = "Admin: Artist", description = "Artist profile management endpoints")
public class AdminArtistController {

    private final AdminArtistService artistService;

    public AdminArtistController(AdminArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    @Operation(summary = "Get artist profile",
        description = "Get the current artist profile for admin management",
        responses = {
            @ApiResponse(responseCode = "200", description = "Artist profile found",
                content = @Content(schema = @Schema(implementation = ArtistProfile.class))),
            @ApiResponse(responseCode = "404", description = "Artist profile not found")
        })
    public ArtistProfile getArtistProfile() {
        return artistService.getArtistProfile();
    }

    @PutMapping
    @Operation(summary = "Update artist profile",
        description = "Update the artist profile information",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = UpdateArtistProfileRequest.class),
                examples = @ExampleObject(
                    name = "UpdateArtistProfileRequest",
                    value = "{\n  \"name\": \"Baltaragis\",\n  \"bio\": \"Contemporary artist blending traditional forms with modern techniques.\",\n  \"heroImageUrl\": \"https://example.com/hero-updated.jpg\",\n  \"socials\": \"{\\\"instagram\\\":\\\"https://instagram.com/baltaragis\\\",\\\"twitter\\\":\\\"https://x.com/baltaragis\\\",\\\"website\\\":\\\"https://baltaragis.com\\\"}\"\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Artist profile updated successfully",
                content = @Content(schema = @Schema(implementation = ArtistProfile.class)))
        })
    public ArtistProfile updateArtistProfile(@Valid @RequestBody UpdateArtistProfileRequest request) {
        return artistService.updateArtistProfile(request);
    }
}
