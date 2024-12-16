package com.encryption_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: EncryptedResponse
 */
@Data
@AllArgsConstructor
public class EncryptedResponse {
    private String encryptedValue; // The encrypted JSON payload
    private String encryptedKey;   // The AES key encrypted with the RSA public key
    private String iv;             // The initialization vector
}
