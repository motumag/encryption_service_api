package com.encryption_service.dto;

import lombok.Data;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: EncryptedRequest
 */
@Data
public class EncryptedRequest {
    private String encryptedValue;
    private String encryptedKey;
    private String iv;
}
