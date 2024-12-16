package com.encryption_service.service.impl;

import com.encryption_service.dto.EncryptedRequest;
import com.encryption_service.dto.EncryptedResponse;
import com.encryption_service.dto.PayloadRequest;
import com.encryption_service.service.EncryptionService;
import com.encryption_service.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: EncryptionServiceImpl
 */
@Service
@RequiredArgsConstructor
public class EncryptionServiceImpl implements EncryptionService {

    private final EncryptionUtil encryptionUtil;

    @Override
    public EncryptedResponse encryptPayload(PayloadRequest payload) {
        return encryptionUtil.encrypt(payload);
    }

    @Override
    public PayloadRequest decryptPayload(EncryptedRequest encryptedRequest) {
        return encryptionUtil.decrypt(encryptedRequest);
    }
}
