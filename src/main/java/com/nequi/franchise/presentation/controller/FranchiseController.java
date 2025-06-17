package com.nequi.franchise.presentation.controller;

import com.nequi.franchise.application.dto.FranchiseRequest;
import com.nequi.franchise.application.dto.FranchiseResponse;
import com.nequi.franchise.infrastructure.aop.LogExecution;
import com.nequi.franchise.infrastructure.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "api.franchise.tag.name", description = "api.franchise.tag.description")
public interface FranchiseController {

    /**
     * Get a franchise by ID.
     *
     * @param id the franchise ID
     * @return the franchise response wrapped in ApiResponse
     */
    @LogExecution("Get franchise by ID")
    @Operation(
            summary = "api.franchise.get.summary",
            description = "api.franchise.get.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "api.response.notfound"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> getFranchise(
            @Parameter(description = "api.franchise.get.param.id", required = true)
            @PathVariable String id);

    /**
     * Get all franchises.
     *
     * @return list of all franchises wrapped in ApiResponse
     */
    @LogExecution("Get all franchises")
    @Operation(
            summary = "api.franchise.getall.summary",
            description = "api.franchise.getall.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<List<FranchiseResponse>>>> getAllFranchises();

    /**
     * Create a new franchise.
     *
     * @param request the franchise creation request
     * @return the created franchise response wrapped in ApiResponse
     */
    @LogExecution(value = "Create new franchise")
    @Operation(
            summary = "api.franchise.create.summary",
            description = "api.franchise.create.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "api.response.badrequest"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> createFranchise(@Valid @RequestBody FranchiseRequest request);

    /**
     * Update an existing franchise.
     *
     * @param id the franchise ID to update
     * @param request the franchise update request
     * @return the updated franchise response wrapped in ApiResponse
     */
    @LogExecution(value = "Update franchise")
    @Operation(
            summary = "api.franchise.update.summary",
            description = "api.franchise.update.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "api.response.notfound"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "api.response.badrequest"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> updateFranchise(
            @Parameter(description = "api.franchise.update.param.id", required = true)
            @PathVariable String id, @Valid @RequestBody FranchiseRequest request);

    /**
     * Delete a franchise.
     *
     * @param id the franchise ID to delete
     * @return success response wrapped in ApiResponse
     */
    @LogExecution("Delete franchise")
    @Operation(
            summary = "api.franchise.delete.summary",
            description = "api.franchise.delete.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "api.response.notfound"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<Void>>> deleteFranchise(
            @Parameter(description = "api.franchise.delete.param.id", required = true)
            @PathVariable String id);
}
