package com.zenflix.ott.service;

import jakarta.servlet.http.HttpServletRequest;

public interface RazorpayWebhookService {
    public void processWebhook(HttpServletRequest request);
}
