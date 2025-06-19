package com.nequi.franchise.presentation.controller;

import com.nequi.franchise.application.dto.*;
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
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "api.product.tag.name", description = "api.product.tag.description")
public interface ProductController {

    @LogExecution("Get product by ID")
    @Operation(
            summary = "api.product.get.summary",
            description = "api.product.get.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
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
    Mono<ResponseEntity<ApiResponse<ProductResponse>>> getProduct(
            @Parameter(description = "api.product.get.param.id", required = true)
            @PathVariable String id);

    @LogExecution("Get all products")
    @Operation(
            summary = "api.product.getall.summary",
            description = "api.product.getall.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<List<ProductResponse>>>> getAllProducts(
            @Parameter(description = "api.product.getall.param.category")
            @RequestParam(required = false) String category);

    @LogExecution("Create new product")
    @Operation(
            summary = "api.product.create.summary",
            description = "api.product.create.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
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
    Mono<ResponseEntity<ApiResponse<ProductResponse>>> createProduct(
            @Valid @RequestBody ProductRequest request);

    @LogExecution("Add product to branch")
    @Operation(
            summary = "api.product.addToBranch.summary",
            description = "api.product.addToBranch.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
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
    Mono<ResponseEntity<ApiResponse<ProductResponse>>> addProductToBranch(
            @Parameter(description = "api.product.addToBranch.param.branchId", required = true)
            @PathVariable String branchId,
            @Valid @RequestBody BranchProductRequest request);

    @LogExecution("Update product stock")
    @Operation(
            summary = "api.product.updateStock.summary",
            description = "api.product.updateStock.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductStockResponse.class))
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
    Mono<ResponseEntity<ApiResponse<ProductStockResponse>>> updateProductStock(
            @Parameter(description = "api.product.updateStock.param.productId", required = true)
            @PathVariable String productId,
            @Parameter(description = "api.product.updateStock.param.branchId", required = true)
            @PathVariable String branchId,
            @Valid @RequestBody ProductStockRequest request);

    @LogExecution("Get product stocks by branch")
    @Operation(
            summary = "api.product.getStocksByBranch.summary",
            description = "api.product.getStocksByBranch.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductStockResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<List<ProductStockResponse>>>> getProductStocksByBranch(
            @Parameter(description = "api.product.getStocksByBranch.param.branchId", required = true)
            @PathVariable String branchId);

    @LogExecution("Get product stock")
    @Operation(
            summary = "api.product.getStock.summary",
            description = "api.product.getStock.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductStockResponse.class))
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
    Mono<ResponseEntity<ApiResponse<ProductStockResponse>>> getProductStock(
            @Parameter(description = "api.product.getStock.param.productId", required = true)
            @PathVariable String productId,
            @Parameter(description = "api.product.getStock.param.branchId", required = true)
            @PathVariable String branchId);

    @LogExecution("Remove product from branch")
    @Operation(
            summary = "api.product.removeFromBranch.summary",
            description = "api.product.removeFromBranch.description"
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
    Mono<ResponseEntity<ApiResponse<Void>>> removeProductFromBranch(
            @Parameter(description = "api.product.removeFromBranch.param.branchId", required = true)
            @PathVariable String branchId,
            @Parameter(description = "api.product.removeFromBranch.param.productId", required = true)
            @PathVariable String productId);

    @LogExecution("Get top stock products by franchise")
    @Operation(
            summary = "api.product.getTopStockByFranchise.summary",
            description = "api.product.getTopStockByFranchise.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = TopStockProductByBranchResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "api.response.servererror"
            )
    })
    Mono<ResponseEntity<ApiResponse<List<TopStockProductByBranchResponse>>>> getTopStockProductsByFranchise(
            @Parameter(description = "api.product.getTopStockByFranchise.param.franchiseId", required = true)
            @PathVariable String franchiseId);

    @LogExecution("Update product name")
    @Operation(
            summary = "api.product.updateName.summary",
            description = "api.product.updateName.description"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "api.response.success",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))
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
    Mono<ResponseEntity<ApiResponse<ProductResponse>>> updateProductName(
            @Parameter(description = "api.product.updateName.param.id", required = true)
            @PathVariable String id,
            @Valid @RequestBody ProductNameUpdateRequest request);
}
