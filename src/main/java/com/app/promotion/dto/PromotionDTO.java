package com.app.promotion.dto;

import lombok.Data;

@Data
public class PromotionDTO {
    private int qty;
    private String type;
    private String productCode;
    private double price;
}
