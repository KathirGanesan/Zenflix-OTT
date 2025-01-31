package com.zenflix.ott.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.zenflix.ott.config.RazorpayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayConfig razorpayConfig;

    @Autowired
    public RazorpayService(RazorpayConfig razorpayConfig) {
        this.razorpayConfig = razorpayConfig;
    }

    public RazorpayClient getClient() throws RazorpayException {
        return new RazorpayClient(razorpayConfig.getKey(), razorpayConfig.getSecret());
    }

}
