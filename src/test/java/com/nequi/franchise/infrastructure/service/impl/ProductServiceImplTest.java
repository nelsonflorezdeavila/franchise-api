package com.nequi.franchise.infrastructure.service.impl;

import com.nequi.franchise.application.dto.BranchProductRequest;
import com.nequi.franchise.application.dto.ProductStockRequest;
import com.nequi.franchise.application.dto.ProductStockResponse;
import com.nequi.franchise.application.dto.ProductRequest;
import com.nequi.franchise.application.dto.ProductResponse;
import com.nequi.franchise.domain.model.Branch;
import com.nequi.franchise.domain.model.Product;
import com.nequi.franchise.domain.model.ProductStock;
import com.nequi.franchise.domain.repository.BranchRepository;
import com.nequi.franchise.domain.repository.ProductRepository;
import com.nequi.franchise.domain.repository.ProductStockRepository;
import com.nequi.franchise.infrastructure.mapper.ProductMapper;
import com.nequi.franchise.infrastructure.mapper.ProductStockMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductStockRepository productStockRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductStockMapper productStockMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private Branch branch;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id("1")
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(100))
                .category("Test Category")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                true, // active
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

        productRequest = new ProductRequest(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                true // active
        );

        branch = Branch.builder()
                .id("1")
                .name("Test Branch")
                .address("Test Address")
                .city("Test City")
                .franchiseId("1")
                .build();
    }

    @Test
    void findById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        StepVerifier.create(productService.findById("1"))
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void findById_WhenProductDoesNotExist_ShouldReturnEmpty() {
        when(productRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.findById("1"))
                .verifyComplete();
    }

    @Test
    void create_WhenProductNameDoesNotExist_ShouldCreateProduct() {
        when(productRepository.existsByName(anyString())).thenReturn(Mono.just(Boolean.FALSE));
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        StepVerifier.create(productService.create(productRequest))
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void create_WhenProductNameExists_ShouldReturnError() {
        when(productRepository.existsByName(anyString())).thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(productService.create(productRequest))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void update_WhenProductExists_ShouldUpdateProduct() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(productMapper.updateEntity(any(Product.class), any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        StepVerifier.create(productService.update("1", productRequest))
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void update_WhenProductDoesNotExist_ShouldReturnEmpty() {
        when(productRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.update("1", productRequest))
                .verifyComplete();
    }

    @Test
    void delete_ShouldDeleteProduct() {
        when(productRepository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.delete("1"))
                .verifyComplete();
    }

    @Test
    void addProductToBranch_WhenBranchExistsAndProductCreated_ShouldAddProduct() {
        BranchProductRequest request = new BranchProductRequest(productRequest, 10, 5, 100);
        ProductStock productStock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(10)
                .minStock(5)
                .maxStock(100)
                .build();

        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productRepository.existsByName(anyString())).thenReturn(Mono.just(Boolean.FALSE));
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);
        when(productStockRepository.save(any(ProductStock.class))).thenReturn(Mono.just(productStock));

        StepVerifier.create(productService.addProductToBranch("1", request))
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.just(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        StepVerifier.create(productService.findAll())
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void findByCategory_WhenCategoryExists_ShouldReturnProducts() {
        when(productRepository.findAll()).thenReturn(Flux.just(product));
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        StepVerifier.create(productService.findByCategory("Test Category"))
                .expectNext(productResponse)
                .verifyComplete();
    }

    @Test
    void updateProductStock_WhenProductAndBranchExist_ShouldUpdateStock() {
        ProductStock existingStock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(5)
                .minStock(1)
                .maxStock(10)
                .build();

        ProductStockRequest stockRequest = new ProductStockRequest(8, 1, 10);

        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockRepository.findByProductIdAndBranchId(anyString(), anyString()))
                .thenReturn(Mono.just(existingStock));
        when(productStockMapper.updateEntity(any(), any())).thenReturn(existingStock);
        when(productStockRepository.save(any())).thenReturn(Mono.just(existingStock));
        when(productStockMapper.toResponse(any(), any(), any()))
                .thenReturn(new ProductStockResponse("1", "1", "Test Product", "1", "Test Branch", 8, 1, 10, LocalDateTime.now(), LocalDateTime.now()));

        StepVerifier.create(productService.updateProductStock("1", "1", stockRequest))
                .expectNextMatches(response -> 
                    response.stock() == 8 && 
                    response.productId().equals("1") && 
                    response.branchId().equals("1"))
                .verifyComplete();
    }

    @Test
    void getProductStocksByBranch_ShouldReturnStocks() {
        ProductStock stock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(5)
                .build();

        when(productStockRepository.findByBranchId(anyString())).thenReturn(Flux.just(stock));
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockMapper.toResponse(any(), any(), any()))
                .thenReturn(new ProductStockResponse("1", "1", "Test Product", "1", "Test Branch", 5, 0, 1000, LocalDateTime.now(), LocalDateTime.now()));

        StepVerifier.create(productService.getProductStocksByBranch("1"))
                .expectNextMatches(response -> 
                    response.stock() == 5 && 
                    response.productId().equals("1"))
                .verifyComplete();
    }

    @Test
    void removeProductFromBranch_WhenProductAndBranchExist_ShouldRemove() {
        ProductStock stock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(5)
                .build();

        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockRepository.findByProductIdAndBranchId(anyString(), anyString()))
                .thenReturn(Mono.just(stock));
        when(productStockRepository.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(productService.removeProductFromBranch("1", "1"))
                .verifyComplete();
    }

    @Test
    void updateProductName_WhenProductExists_ShouldUpdateName() {
        Product updatedProduct = Product.builder()
                .id(product.getId())
                .name("Updated Name")
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
        
        ProductResponse updatedResponse = new ProductResponse(
                updatedProduct.getId(),
                "Updated Name",
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getCategory(),
                true,
                updatedProduct.getCreatedAt(),
                updatedProduct.getUpdatedAt()
        );

        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(productRepository.save(any())).thenReturn(Mono.just(updatedProduct));
        when(productMapper.toResponse(any())).thenReturn(updatedResponse);

        StepVerifier.create(productService.updateProductName("1", "Updated Name"))
                .expectNextMatches(response -> response.name().equals("Updated Name"))
                .verifyComplete();
    }

    @Test
    void getProductStocks_ShouldReturnStocksForProduct() {
        ProductStock stock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(5)
                .build();

        when(productStockRepository.findByProductId(anyString())).thenReturn(Flux.just(stock));
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockMapper.toResponse(any(), any(), any()))
                .thenReturn(new ProductStockResponse("1", "1", "Test Product", "1", "Test Branch", 5, 0, 1000, LocalDateTime.now(), LocalDateTime.now()));

        StepVerifier.create(productService.getProductStocks("1"))
                .expectNextMatches(response -> 
                    response.stock() == 5 && 
                    response.productId().equals("1"))
                .verifyComplete();
    }

    @Test
    void getProductStockByBranch_WhenExists_ShouldReturnStock() {
        ProductStock stock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(5)
                .build();

        when(productStockRepository.findByProductIdAndBranchId(anyString(), anyString()))
                .thenReturn(Mono.just(stock));
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockMapper.toResponse(any(), any(), any()))
                .thenReturn(new ProductStockResponse("1", "1", "Test Product", "1", "Test Branch", 5, 0, 1000, LocalDateTime.now(), LocalDateTime.now()));

        StepVerifier.create(productService.getProductStockByBranch("1", "1"))
                .expectNextMatches(response -> 
                    response.stock() == 5 && 
                    response.productId().equals("1") &&
                    response.branchId().equals("1"))
                .verifyComplete();
    }

    @Test
    void getLowStockProducts_ShouldReturnLowStockProducts() {
        ProductStock stock = ProductStock.builder()
                .productId("1")
                .branchId("1")
                .stock(2)
                .minStock(5)
                .build();

        when(productStockRepository.findByStockLessThanMinStock()).thenReturn(Flux.just(stock));
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockMapper.toResponse(any(), any(), any()))
                .thenReturn(new ProductStockResponse("1", "1", "Test Product", "1", "Test Branch", 2, 5, 1000, LocalDateTime.now(), LocalDateTime.now()));

        StepVerifier.create(productService.getLowStockProducts())
                .expectNextMatches(response -> 
                    response.stock() == 2 && 
                    response.minStock() == 5)
                .verifyComplete();
    }

    @Test
    void updateProductStock_WhenStockNotFound_ShouldReturnError() {
        ProductStockRequest stockRequest = new ProductStockRequest(8, 1, 10);

        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(branchRepository.findById(anyString())).thenReturn(Mono.just(branch));
        when(productStockRepository.findByProductIdAndBranchId(anyString(), anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProductStock("1", "1", stockRequest))
                .expectError(RuntimeException.class)
                .verify();
    }
}
