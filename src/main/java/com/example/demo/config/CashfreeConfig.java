package com.example.demo.config;

import com.cashfree.Cashfree;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashfreeConfig {

    @PostConstruct
    public void init() {
        // REPLACE THESE WITH YOUR ACTUAL API KEYS
        Cashfree.XClientId = "TEST10049078f46866fc90ca86e537c587094001";
        Cashfree.XClientSecret = "TESTe734ee5cf4ab290b5f1cf24b6fed0d008f3e7ff";

        // Switch to Cashfree.PRODUCTION when going live
        Cashfree.XEnvironment = Cashfree.SANDBOX;
    }
}