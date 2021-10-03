package com.app.promotion.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppliedOfferDTO {
    private List<ProductCheckoutDTO> checkouts;
    private double totalPrice;
}
