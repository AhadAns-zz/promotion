package com.app.promotion.service;

import com.app.promotion.PromotionApplication;
import com.app.promotion.dto.ProductDTO;
import com.app.promotion.dto.PromotionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class ConfigService {

    private List<ProductDTO> productDTOS;
    private List<PromotionDTO> promotionDTOS;
    ObjectMapper mapper = new ObjectMapper();

    public void loadData() {
        try {
            loadProducts();
            loadProductOffers();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadProducts() throws IOException, URISyntaxException {
        String RESULT_EXAMPLE =
                new String(
                        Files.readAllBytes(
                                Paths.get(
                                        Objects.requireNonNull(PromotionApplication.class
                                                .getClassLoader()
                                                .getResource("products.json"))
                                                .toURI())));

        // convert JSON file to map
        productDTOS = mapper.readValue(RESULT_EXAMPLE, new TypeReference<>() {
        });
    }

    private void loadProductOffers() throws IOException, URISyntaxException {
        String RESULT_EXAMPLE =
                new String(
                        Files.readAllBytes(
                                Paths.get(
                                        Objects.requireNonNull(PromotionApplication.class
                                                .getClassLoader()
                                                .getResource("promotions.json"))
                                                .toURI())));

        // convert JSON file to map
        promotionDTOS = mapper.readValue(RESULT_EXAMPLE, new TypeReference<>() {
        });
    }

    public List<ProductDTO> getProducts() {
        return productDTOS;
    }

    public List<PromotionDTO> getPromotions() {
        return promotionDTOS;
    }
}
