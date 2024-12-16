package com.encryption_service.controller;

import com.encryption_service.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: EncryptionController
 */
@RestController
@RequestMapping("/api/encryption")
@RequiredArgsConstructor
public class EncryptionController {

    private final EncryptionService encryptionService;

    @PostMapping("/encrypt")
    public EncryptedResponse encryptPayload(@RequestBody PayloadRequest payload) {
        return encryptionService.encryptPayload(payload);
    }

    @PostMapping("/decrypt")
    public PayloadRequest decryptPayload(@RequestBody EncryptedRequest encryptedRequest) {
        return encryptionService.decryptPayload(encryptedRequest);
    }
}
