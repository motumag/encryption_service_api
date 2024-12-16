package com.encryption_service.dto;

import lombok.Data;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: PayloadRequest
 */

@Data
public class PayloadRequest {
    private String firstName;
    private String lastName;
    private PaymentCard paymentCard;

    @Data
    public static class PaymentCard {
        private String pan;
        private String expiryYear;
        private String expiryMonth;
    }
}
