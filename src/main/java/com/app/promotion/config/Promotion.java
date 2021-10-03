package com.app.promotion.config;

import com.app.promotion.service.ConfigService;
import com.app.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Promotion implements CommandLineRunner {
    @Autowired
    private ConfigService configService;

    @Autowired
    private PromotionService promotionService;

    @Override
    public void run(String... args) throws Exception {
        configService.loadData();
        promotionService.start();
    }
}
