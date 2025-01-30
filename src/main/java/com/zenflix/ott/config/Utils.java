package com.zenflix.ott.config;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    public static boolean verifyWebhookSignature(String payload, String signature, String secret) {
        try {
            // Generate the expected signature in Base64
            String expectedSignature = HmacSHA256(payload, secret);

            // Convert the received HEX signature to Base64
            String receivedSignatureBase64 = convertHexToBase64(signature);

            System.out.println("🔹 Expected Signature: " + expectedSignature);
            System.out.println("🔹 Received Signature (Base64 Converted): " + receivedSignatureBase64);

            return expectedSignature.equals(receivedSignatureBase64);
        } catch (Exception e) {
            System.err.println("❌ Signature Verification Error: " + e.getMessage());
            return false;
        }
    }

    private static String HmacSHA256(String data, String secret) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac); //  Correct Base64 encoding
    }

    private static String convertHexToBase64(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
