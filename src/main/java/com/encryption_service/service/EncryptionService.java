package com.encryption_service.service;

import com.encryption_service.dto.EncryptedRequest;
import com.encryption_service.dto.EncryptedResponse;
import com.encryption_service.dto.PayloadRequest;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: EncryptionService
 */
public interface EncryptionService {
    EncryptedResponse encryptPayload(PayloadRequest payload);
    PayloadRequest decryptPayload(EncryptedRequest encryptedRequest);
}