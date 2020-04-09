package com.initsysctrl.omnidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WalletDeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletDeApplication.class, args);
    }
}
