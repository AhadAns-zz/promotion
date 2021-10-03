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
                .filter(x -> x.productCode.equals(product.productCode))
                .findFirst()
                .ifPresent(value -> appliedPromotionDTO = value);

        if (appliedPromotionDTO != null && appliedPromotionDTO.type.equals(PromotionUtil.SINGLE)) {
            product.isValidated = true;
            return true;
        }

        return false;
    }

    public double calculateProductPrice(List<ProductCheckoutDTO> productCheckoutDTOList) {
        double finalPrice = 0;
        try {
            int totalEligibleItems = productCheckoutDTO.qty / appliedPromotionDTO.qty;
            int remainingItems = productCheckoutDTO.qty % appliedPromotionDTO.qty;
            finalPrice = appliedPromotionDTO.price * totalEligibleItems + remainingItems * (productCheckoutDTO.defaultPrice);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return finalPrice;
    }
}
