package com.krillinator.demo_5;

import com.krillinator.demo_5.product.Product;
import com.krillinator.demo_5.product.ProductRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/* @Import(Example.class)
*   Loads an isolated environment
*   What's happening:
*       Spins up a Postgres Docker container automatically
*       Injects the connection details (host, port, user, password, DB name)
*           into the Spring environment
*       Overrides the 'spring.r2dbc.url' and related props
*           (Does no longer go from app.properties)
* */

@Import(TestcontainersConfiguration.class)

/* @SpringBootTest
*   Starts:
*       Flyway migrations (runs)
*       Testcontainers config (runs)
*       The Postgres container (starts)
*       Injects beans with @Autowired
* */

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    /* Clear before Tests
    *   for better performance, clear intention & simplicity
    * */

    @BeforeEach
    public void clearDatabase() {
        productRepository.deleteAll().block(); // .block() ensures that the data is deleted before we move on
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void shouldSaveAndRetrieveProduct() {
        Product product_apples = new Product(
                null,
                "Apples",
                "Fresh apples from the North",
                BigDecimal.valueOf(19.9),
                false,
                null
        );

        /* Test Assertion using StepVerifier
        *   expectNextMatches() is like JUnit Assertions
        * */

        /* StepVerifier.create(
                productRepository.findById(product_apples.id())
        )
                .expectNextMatches(product -> product.name().equals("Apples"))
                .verifyComplete();
        */


        // Test Assertion using JUnit Methods
        StepVerifier.create(
                productRepository.findById(product_apples.id())
        )
                .assertNext(product -> Assertions.assertEquals("Apples", product.name()))
                .verifyComplete();
    }
}
