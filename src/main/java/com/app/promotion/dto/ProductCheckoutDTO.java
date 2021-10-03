package com.app.promotion.dto;

import lombok.Data;

@Data
public class ProductCheckoutDTO {
    private String productCode;
    private int qty;
    private double finalPrice;
    private double defaultPrice;
    private boolean hasOffer;
    private boolean isValidated;

    public void setIsValidated(boolean isValidated) {
        this.isValidated = isValidated;
    }

    public boolean hasOffer() {
        return this.hasOffer;
    }


    public ProductCheckoutDTO productCode(final String productCode) {
        this.productCode = productCode;
        return this;
    }

    public ProductCheckoutDTO qty(final int qty) {
        this.qty = qty;
        return this;
    }

    public ProductCheckoutDTO defaultPrice(final double defaultPrice) {
        this.defaultPrice = defaultPrice;
        return this;
    }
}
