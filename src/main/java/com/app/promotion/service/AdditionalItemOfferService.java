package com.app.promotion.service;

import com.app.promotion.dto.ProductCheckoutDTO;
import com.app.promotion.dto.PromotionDTO;
import com.app.promotion.util.PromotionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalItemOfferService {
    private PromotionDTO appliedPromotionDTO;
    private ProductCheckoutDTO productCheckoutDTO;

    public boolean canExecute(ProductCheckoutDTO product, List<PromotionDTO> promotionDTOS) {
        productCheckoutDTO = product;
        promotionDTOS
                .stream()
                .filter(x -> x.getProductCode().equals(product.getProductCode()))
                .findFirst()
                .ifPresent(value -> appliedPromotionDTO = value);

        if (appliedPromotionDTO != null && appliedPromotionDTO.getType().equals(PromotionUtil.SINGLE)) {
            product.setIsValidated(true);
            return true;
        }

        return false;
    }

    public double calculateProductPrice(List<ProductCheckoutDTO> productCheckoutDTOList) {
        double finalPrice = 0;
        try {
            int totalEligibleItems = productCheckoutDTO.getQty() / appliedPromotionDTO.getQty();
            int remainingItems = productCheckoutDTO.getQty() % appliedPromotionDTO.getQty();
            finalPrice = appliedPromotionDTO.getPrice() * totalEligibleItems + remainingItems * (productCheckoutDTO.getDefaultPrice());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return finalPrice;
    }
}
