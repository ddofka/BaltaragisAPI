package org.codeacademy.baltaragisapi.web;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "Problem", description = "RFC-7807 Problem Details")
public class ProblemSchema {
    @Schema(example = "https://api.baltaragis.dev/problems/insufficient_stock")
    public String type;

    @Schema(example = "Conflict")
    public String title;

    @Schema(example = "409")
    public Integer status;

    @Schema(example = "Insufficient stock")
    public String detail;

    @Schema(example = "/api/v1/orders")
    public String instance;

    @Schema(example = "INSUFFICIENT_STOCK")
    public String code;

    @Schema(example = "2025-01-01T00:00:00Z")
    public String timestamp;

    @ArraySchema(schema = @Schema(implementation = FieldError.class))
    public List<FieldError> errors;

    @Schema(name = "FieldError")
    public static class FieldError {
        @Schema(example = "qty")
        public String field;
        @Schema(example = "Quantity must be greater than 0")
        public String message;
    }
}


