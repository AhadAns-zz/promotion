package com.app.promotion.service;

import com.app.promotion.dto.ProductCheckoutDTO;
import com.app.promotion.dto.PromotionDTO;
import com.app.promotion.util.PromotionUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ComboOfferService {
    private PromotionDTO appliedPromotionDTO;
    private ProductCheckoutDTO recentProductCheckoutDTO;
    private List<ProductCheckoutDTO> productCheckoutDTOS;


    public boolean canExecute(ProductCheckoutDTO productCheckoutDTO, List<PromotionDTO> promotionDTOS) {
        recentProductCheckoutDTO = productCheckoutDTO;
        promotionDTOS
                .stream()
                .filter(x -> Arrays.stream(x.getProductCode().split(";"))
                        .anyMatch(productCheckoutDTO.getProductCode()::contains))
                .findFirst().
                ifPresent(value -> appliedPromotionDTO = value);

        return appliedPromotionDTO != null && !productCheckoutDTO.isValidated() && appliedPromotionDTO.getType().equals(PromotionUtil.COMBO);
    }


    public double calculateProductPrice(List<ProductCheckoutDTO> productCheckoutDTOList)
    {
        productCheckoutDTOS = new ArrayList<>();

        double finalPrice = 0;


        try {
            String[] str = appliedPromotionDTO.getProductCode().split(";");
            for(ProductCheckoutDTO productCheckoutDTO : productCheckoutDTOList) {
                if (Arrays.stream(str).anyMatch(productCheckoutDTO.getProductCode()::contains)) {
                    productCheckoutDTOS.add(productCheckoutDTO);
                    productCheckoutDTO.setIsValidated(true);
                }
            }

            int quantity_first = 0;
            int quantity_second = 0;
            if (productCheckoutDTOS.size() > 1) {
                quantity_first = productCheckoutDTOS.get(0).getQty();
                quantity_second = productCheckoutDTOS.get(1).getQty();
            }
            //if one of the product quatity is empty
            if (quantity_first == 0 || quantity_second == 0) {
                return recentProductCheckoutDTO.getDefaultPrice();

            }

            //if both of the products are equal is size
            if (quantity_first == quantity_second) {
                finalPrice = appliedPromotionDTO.getPrice() * quantity_first;
            } else if (quantity_first > quantity_second) {
                int additionalItems = quantity_first - quantity_second;
                finalPrice = (recentProductCheckoutDTO.getDefaultPrice() * additionalItems) + (appliedPromotionDTO.getPrice() * quantity_second);
            } else {
                int additionalItems = quantity_second - quantity_first;
                finalPrice = (recentProductCheckoutDTO.getDefaultPrice() * additionalItems) + (appliedPromotionDTO.getPrice() * quantity_first);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return finalPrice;
    }
}
