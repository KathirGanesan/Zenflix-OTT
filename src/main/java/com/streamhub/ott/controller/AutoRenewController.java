package com.streamhub.ott.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamhub.ott.service.impl.AutoRenewService;

@RestController
@RequestMapping("/api/autorenew")
public class AutoRenewController {

    private final AutoRenewService autoRenewService;

    public AutoRenewController(AutoRenewService autoRenewService) {
        this.autoRenewService = autoRenewService;
    }

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerAutoRenew() {
        autoRenewService.processAutoRenewals();
        return ResponseEntity.ok("Auto-renew process triggered successfully.");
    }
}
