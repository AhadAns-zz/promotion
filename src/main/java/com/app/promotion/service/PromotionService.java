package com.app.promotion.service;

import com.app.promotion.dto.AppliedOfferDTO;
import com.app.promotion.dto.ProductDTO;
import com.app.promotion.dto.ProductCheckoutDTO;
import com.app.promotion.dto.PromotionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PromotionService {
    private final AdditionalItemOfferService additionalItemOfferService;
    private final ComboOfferService comboOfferService;
    private final ConfigService configService;

    private List<ProductCheckoutDTO> productCheckoutDTOList;
    private AppliedOfferDTO appliedOfferDTO;

    PromotionService(AdditionalItemOfferService additionalItemOfferService, ComboOfferService comboOfferService, ConfigService configService) {
        this.additionalItemOfferService = additionalItemOfferService;
        this.comboOfferService = comboOfferService;
        this.configService = configService;
    }

    public void start() {
        productCheckoutDTOList = loadUserInput();

        appliedOfferDTO = applyPromotion(productCheckoutDTOList, getProductOffers());

        if (appliedOfferDTO.checkouts != null) {
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
            for(ProductCheckoutDTO productCheckoutDTO : checkoutList) {
                if (productCheckoutDTO.qty > 0) {
                    if (additionalItemOfferService.canExecute(productCheckoutDTO, promotionDTOS)) {
                        productCheckoutDTO.hasOffer = true;
                        productCheckoutDTO.finalPrice = additionalItemOfferService.calculateProductPrice(checkoutList);
                        appliedOfferDTO.totalPrice += productCheckoutDTO.finalPrice;
                    }

                    if (comboOfferService.canExecute(productCheckoutDTO, promotionDTOS)) {
                        productCheckoutDTO.hasOffer = true;
                        productCheckoutDTO.finalPrice = comboOfferService.calculateProductPrice(checkoutList);
                        appliedOfferDTO.totalPrice += productCheckoutDTO.finalPrice;
                    }
                }
            }

            appliedOfferDTO.checkouts = checkoutList;
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
                System.out.println("Input quantity of " + productDTO.code);
                int quantity = scanner.nextInt();

                checkoutList.add(new ProductCheckoutDTO().productCode(productDTO.code).qty(quantity).defaultPrice(productDTO.price));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return checkoutList;
    }

    public void displayTotalPrice(AppliedOfferDTO appliedOfferDTO) {
        System.out.println("Calculating Final Price..........................");
        System.out.println("ProductCode" + "-" + "Quantity" + " - " + "FinalPrice" + " - " + "HasOffer");

        for (ProductCheckoutDTO productCheckoutDTO : appliedOfferDTO.checkouts) {
            System.out.println(productCheckoutDTO.productCode + "-" + productCheckoutDTO.qty + "-" + productCheckoutDTO.finalPrice + "-" + productCheckoutDTO.hasOffer);
        }
        System.out.println("Total Price : " + appliedOfferDTO.totalPrice);
    }

    private List<ProductDTO> loadAvailableProducts() {
        return configService.getProducts();
    }
}
