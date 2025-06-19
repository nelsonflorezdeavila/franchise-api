package com.nequi.franchise.presentation.controller;

import com.nequi.franchise.application.dto.BranchNameUpdateRequest;
import com.nequi.franchise.application.dto.BranchRequest;
import com.nequi.franchise.application.dto.BranchResponse;
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

@Tag(name = "api.branch.tag.name", description = "api.branch.tag.description")
public interface BranchController {

    /**
     * Get a branch by ID.
     *
     * @param id the branch ID
     * @return the branch response wrapped in ApiResponse
     */
    @LogExecution("Get branch by ID")
    @Operation(
            summary = "api.branch.get.summary",
            description = "api.branch.get.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
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
    Mono<ResponseEntity<ApiResponse<BranchResponse>>> getBranch(
            @Parameter(description = "api.branch.get.param.id", required = true)
            @PathVariable String id);

    /**
     * Get all branches for a specific franchise.
     *
     * @param franchiseId the franchise ID
     * @return list of branches for the franchise wrapped in ApiResponse
     */
    @LogExecution("Get branches by franchise ID")
    @Operation(
            summary = "api.branch.getByFranchise.summary",
            description = "api.branch.getByFranchise.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
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
    Mono<ResponseEntity<ApiResponse<List<BranchResponse>>>> getBranchesByFranchise(
            @Parameter(description = "api.branch.getByFranchise.param.franchiseId", required = true)
            @PathVariable String franchiseId);

    /**
     * Get all branches.
     *
     * @return list of all branches wrapped in ApiResponse
     */
    @LogExecution("Get all branches")
    @Operation(
            summary = "api.branch.getall.summary",
            description = "api.branch.getall.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<List<BranchResponse>>>> getAllBranches();

    /**
     * Create a new branch for a franchise.
     *
     * @param franchiseId the franchise ID
     * @param request the branch creation request
     * @return the created branch response wrapped in ApiResponse
     */
    @LogExecution(value = "Create new branch")
    @Operation(
            summary = "api.branch.create.summary",
            description = "api.branch.create.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "api.response.badrequest"
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
    Mono<ResponseEntity<ApiResponse<BranchResponse>>> createBranch(
            @Parameter(description = "api.branch.create.param.franchiseId", required = true)
            @PathVariable String franchiseId,
            @Valid @RequestBody BranchRequest request);

    /**
     * Update an existing branch.
     *
     * @param id the branch ID to update
     * @param request the branch update request
     * @return the updated branch response wrapped in ApiResponse
     */
    @LogExecution(value = "Update branch")
    @Operation(
            summary = "api.branch.update.summary",
            description = "api.branch.update.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
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
    Mono<ResponseEntity<ApiResponse<BranchResponse>>> updateBranch(
            @Parameter(description = "api.branch.update.param.id", required = true)
            @PathVariable String id,
            @Valid @RequestBody BranchRequest request);

    /**
     * Delete a branch.
     *
     * @param id the branch ID to delete
     * @return success response wrapped in ApiResponse
     */
    @LogExecution("Delete branch")
    @Operation(
            summary = "api.branch.delete.summary",
            description = "api.branch.delete.description"
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
    Mono<ResponseEntity<ApiResponse<Void>>> deleteBranch(
            @Parameter(description = "api.branch.delete.param.id", required = true)
            @PathVariable String id);

    /**
     * Update the name of an existing branch.
     *
     * @param id the branch ID to update
     * @param request the branch name update request
     * @return the updated branch response wrapped in ApiResponse
     */
    @LogExecution(value = "Update branch name")
    @Operation(
            summary = "api.branch.updatename.summary",
            description = "api.branch.updatename.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = BranchResponse.class))
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
    Mono<ResponseEntity<ApiResponse<BranchResponse>>> updateBranchName(
            @Parameter(description = "api.branch.updatename.param.id", required = true)
            @PathVariable String id,
            @Valid @RequestBody BranchNameUpdateRequest request);
}
