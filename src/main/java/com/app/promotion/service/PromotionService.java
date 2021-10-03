package com.app.promotion.service;

import com.app.promotion.dto.AppliedOfferDTO;
import com.app.promotion.dto.ProductDTO;
import com.app.promotion.dto.ProductCheckoutDTO;
import com.app.promotion.dto.PromotionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PromotionService {
    @Autowired
    private AdditionalItemOfferService additionalItemOfferService;

    @Autowired
    private ComboOfferService comboOfferService;

    @Autowired
    private ConfigService configService;

    private List<ProductCheckoutDTO> productCheckoutDTOList;
    private AppliedOfferDTO appliedOfferDTO;

    public void start() {
        productCheckoutDTOList = loadUserInput();

        appliedOfferDTO = applyPromotion(productCheckoutDTOList, getProductOffers());

        if (appliedOfferDTO.getCheckouts() != null) {
            displayTotalPrice(appliedOfferDTO);
        }
    }

    private List<PromotionDTO> getProductOffers() {
        try {
            return configService.getPromotions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public AppliedOfferDTO applyPromotion(List<ProductCheckoutDTO> checkoutList, List<PromotionDTO> promotionDTOS) {
        AppliedOfferDTO appliedOfferDTO = new AppliedOfferDTO();

        try {
            double price = 0;
            for(ProductCheckoutDTO productCheckoutDTO : checkoutList) {
                if (productCheckoutDTO.getQty() > 0) {
                    if (additionalItemOfferService.canExecute(productCheckoutDTO, promotionDTOS)) {
                        productCheckoutDTO.setHasOffer(true);
                        productCheckoutDTO.setFinalPrice(additionalItemOfferService.calculateProductPrice(checkoutList));
                        price += productCheckoutDTO.getFinalPrice();
                        appliedOfferDTO.setTotalPrice(price);
                    }

                    if (comboOfferService.canExecute(productCheckoutDTO, promotionDTOS)) {
                        productCheckoutDTO.setHasOffer(true);
                        productCheckoutDTO.setFinalPrice(comboOfferService.calculateProductPrice(checkoutList));
                        price += productCheckoutDTO.getFinalPrice();
                        appliedOfferDTO.setTotalPrice(price);
                    }
                }
            }

            appliedOfferDTO.setCheckouts(checkoutList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return appliedOfferDTO;
    }

    public List<ProductCheckoutDTO> loadUserInput() {
        List<ProductCheckoutDTO> checkoutList = new ArrayList<>();
        List<ProductDTO> listProductDTO = loadAvailableProducts();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter User Inputs");
        try {
            for (ProductDTO productDTO : listProductDTO) {
                System.out.println("Input quantity of " + productDTO.getCode());
                int quantity = scanner.nextInt();

                checkoutList.add(new ProductCheckoutDTO().productCode(productDTO.getCode()).qty(quantity).defaultPrice(productDTO.getPrice()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return checkoutList;
    }

    public void displayTotalPrice(AppliedOfferDTO appliedOfferDTO) {
        System.out.println("Calculating Final Price..........................");
        System.out.println("ProductCode" + "-" + "Quantity" + " - " + "FinalPrice" + " - " + "HasOffer");

        for (ProductCheckoutDTO productCheckoutDTO : appliedOfferDTO.getCheckouts()) {
            System.out.println(productCheckoutDTO.getProductCode() + "-" + productCheckoutDTO.getQty() + "-" + productCheckoutDTO.getFinalPrice() + "-" + productCheckoutDTO.hasOffer());
        }
        System.out.println("Total Price : " + appliedOfferDTO.getTotalPrice());
    }

    private List<ProductDTO> loadAvailableProducts() {
        return configService.getProducts();
    }
}
