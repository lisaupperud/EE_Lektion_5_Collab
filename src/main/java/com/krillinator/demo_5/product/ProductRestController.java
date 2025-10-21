package com.krillinator.demo_5.product;

import com.krillinator.demo_5.product.dto.ProductResponseDTO;
import com.krillinator.demo_5.product.dto.ProductValidatorDTO;
import com.krillinator.demo_5.product.mapper.ProductMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/api/v1/product")
@RestController
public class ProductRestController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductRestController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/debug")
    public Mono<ResponseEntity<List<Product>>> getAllProductsInDatabase() {

        return productService.getAllProducts()
                .collectList()
                .map(result -> ResponseEntity.ok().body(result)
                );
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<ProductResponseDTO>> createNewProduct(
            @Valid @RequestBody ProductValidatorDTO productValidatorDTO
    ) {

        return productService.createNewProduct(productValidatorDTO)
                .map(product -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(productMapper.toResponseDTO(product))
                );
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteProductById(@PathVariable @Positive Long id) {

        return productService.deleteProductById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
