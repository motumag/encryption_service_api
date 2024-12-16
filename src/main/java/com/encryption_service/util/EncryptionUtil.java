package com.encryption_service.util;

import com.encryption_service.dto.EncryptedRequest;
import com.encryption_service.dto.EncryptedResponse;
import com.encryption_service.dto.PayloadRequest;
import com.encryption_service.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Author: Motuma Gishu, Senior Software Engineer
 * Date: 12/16/24
 * Description: EncryptionUtil
 */
public class EncryptionUtil {

    private static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EncryptionUtil(String publicKeyPath, String privateKeyPath) {
        try {
            this.publicKey = loadPublicKey(publicKeyPath);
            this.privateKey = loadPrivateKey(privateKeyPath);
        } catch (Exception e) {
            throw new CustomException("Error loading RSA keys", e);
        }
    }

    public EncryptedResponse encrypt(PayloadRequest payload) {
        try {
            // Generate AES key and IV
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE);
            SecretKey aesKey = keyGenerator.generateKey();
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom.getInstanceStrong().nextBytes(iv);

            // Encrypt the payload
            Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);
            byte[] encryptedPayload = aesCipher.doFinal(objectMapper.writeValueAsBytes(payload));

            // Encrypt the AES key with RSA
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedKey = rsaCipher.doFinal(aesKey.getEncoded());

            // Build response
            return new EncryptedResponse(
                    Base64.getEncoder().encodeToString(encryptedPayload),
                    Base64.getEncoder().encodeToString(encryptedKey),
                    Base64.getEncoder().encodeToString(iv)
            );
        } catch (Exception e) {
            throw new CustomException("Error during encryption", e);
        }
    }

    public PayloadRequest decrypt(EncryptedRequest encryptedRequest) {
        try {
            // Decrypt AES key with RSA
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] aesKeyBytes = rsaCipher.doFinal(Base64.getDecoder().decode(encryptedRequest.getEncryptedKey()));
            SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

            // Decrypt payload with AES
            Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM);
            byte[] iv = Base64.getDecoder().decode(encryptedRequest.getIv());
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec);
            byte[] decryptedPayload = aesCipher.doFinal(Base64.getDecoder().decode(encryptedRequest.getEncryptedValue()));

            return objectMapper.readValue(decryptedPayload, PayloadRequest.class);
        } catch (Exception e) {
            throw new CustomException("Error during decryption", e);
        }
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(path).toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(path).toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }
}
