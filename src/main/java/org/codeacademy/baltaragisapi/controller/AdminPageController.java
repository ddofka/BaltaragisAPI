package org.codeacademy.baltaragisapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeacademy.baltaragisapi.dto.admin.CreatePageRequest;
import org.codeacademy.baltaragisapi.dto.admin.UpdatePageRequest;
import org.codeacademy.baltaragisapi.entity.Page;
import org.codeacademy.baltaragisapi.service.AdminPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/pages")
@Tag(name = "Admin: Pages", description = "Page management endpoints")
public class AdminPageController {

    private final AdminPageService pageService;

    public AdminPageController(AdminPageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping
    @Operation(summary = "List all pages",
        description = "Get all pages (published and unpublished) for admin management")
    public List<Page> getAllPages() {
        return pageService.getAllPages();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get page by ID",
        description = "Get a specific page by its ID for admin management",
        responses = {
            @ApiResponse(responseCode = "200", description = "Page found"),
            @ApiResponse(responseCode = "404", description = "Page not found")
        })
    public Page getPageById(@PathVariable Long id) {
        return pageService.getPageById(id);
    }

    @PostMapping
    @Operation(summary = "Create new page",
        description = "Create a new page with validation for duplicate slugs",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = CreatePageRequest.class),
                examples = @ExampleObject(
                    name = "CreatePageRequest",
                    value = "{\n  \"title\": \"About Us\",\n  \"slug\": \"about\",\n  \"contentMd\": \"# About Our Studio\\n\\nWe are passionate about art...\",\n  \"isPublished\": true\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Page created successfully",
                content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Page with duplicate slug already exists",
                content = @Content(
                    schema = @Schema(implementation = org.codeacademy.baltaragisapi.web.ProblemSchema.class),
                    examples = @ExampleObject(
                        value = "{\n  \"type\": \"https://api.baltaragis.dev/problems/conflict\",\n  \"title\": \"Conflict\",\n  \"status\": 409,\n  \"detail\": \"Page with slug 'about' already exists\",\n  \"code\": \"DUPLICATE_SLUG\"\n}"
                    )
                ))
        })
    public ResponseEntity<Page> createPage(@Valid @RequestBody CreatePageRequest request) {
        Page page = pageService.createPage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update page",
        description = "Update an existing page by ID",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = UpdatePageRequest.class),
                examples = @ExampleObject(
                    name = "UpdatePageRequest",
                    value = "{\n  \"title\": \"Updated About Us\",\n  \"contentMd\": \"# Updated About Our Studio\\n\\nWe are very passionate about art...\",\n  \"isPublished\": false\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Page updated successfully"),
            @ApiResponse(responseCode = "404", description = "Page not found")
        })
    public Page updatePage(@PathVariable Long id, @Valid @RequestBody UpdatePageRequest request) {
        return pageService.updatePage(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete page",
        description = "Delete a page by ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Page deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Page not found")
        })
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
        return ResponseEntity.noContent().build();
    }
}
