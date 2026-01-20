package com.example.demo.webhook;

import com.cashfree.Cashfree;
// Remove the explicit PGWebhookEvent import if it exists, as it's an inner class
import com.example.demo.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class CashfreeWebhookController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public CashfreeWebhookController(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/cashfree")
    public ResponseEntity<String> handleCashfreeWebhook(
            @RequestHeader("x-webhook-signature") String signature,
            @RequestHeader("x-webhook-timestamp") String timestamp,
            @RequestBody String rawBody) {

        try {
            // 1. Create instance
            Cashfree cf = new Cashfree();

            // 2. Verify Signature
            // FIX: Use Cashfree.PGWebhookEvent instead of just PGWebhookEvent
            Cashfree.PGWebhookEvent event = cf.PGVerifyWebhookSignature(signature, rawBody, timestamp);

            // 3. Parse JSON manually to extract status
            Map<String, Object> payload = objectMapper.readValue(rawBody, Map.class);
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Map<String, Object> order = (Map<String, Object>) data.get("order");
            Map<String, Object> payment = (Map<String, Object>) data.get("payment");

            String orderId = (String) order.get("order_id");
            String paymentStatus = (String) payment.get("payment_status");

            // 4. Update Database
            paymentService.updateStatus(orderId, paymentStatus);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing webhook: " + e.getMessage());
        }
    }
}