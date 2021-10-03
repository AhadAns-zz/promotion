package com.app.promotion.dto;

public class ProductCheckoutDTO {
    public String productCode;
    public int qty;
    public double finalPrice;
    public double defaultPrice;
    public boolean hasOffer;
    public boolean isValidated;


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
