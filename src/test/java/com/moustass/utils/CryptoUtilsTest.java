package com.moustass.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {
    @Test
    void generateKeyPair_shouldReturnValidKeys() {
        KeyPair keyPair = assertDoesNotThrow(CryptoUtils::generateKeyPair);

        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());
    }

    @Test
    void sha256Hex_sameInput_shouldReturnSameHash() throws NoSuchAlgorithmException {
        String hash1 = CryptoUtils.sha256Hex("password");
        String hash2 = CryptoUtils.sha256Hex("password");

        assertEquals(hash1, hash2);
    }

    @Test
    void sha256Hex_differentInput_shouldReturnDifferentHash() throws NoSuchAlgorithmException {
        String hash1 = CryptoUtils.sha256Hex("password");
        String hash2 = CryptoUtils.sha256Hex("password123");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void sha256_file_shouldReturnHash() throws NoSuchAlgorithmException, IOException {
        File tempFile = File.createTempFile("test", ".txt");
        Files.writeString(tempFile.toPath(), "hello");

        byte[] hash = CryptoUtils.sha256(tempFile);

        assertNotNull(hash);
        assertTrue(hash.length > 0);

        tempFile.delete();
    }

    @Test
    void base64_encodeAndDecode_shouldReturnOriginalBytes() {
        byte[] data = "hello".getBytes();

        String encoded = CryptoUtils.b64(data);
        byte[] decoded = CryptoUtils.fromB64(encoded);

        assertArrayEquals(data, decoded);
    }

    @Test
    void signAndVerify_shouldReturnTrue() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPair keyPair = CryptoUtils.generateKeyPair();
        byte[] data = "important data".getBytes();

        byte[] signature = CryptoUtils.signSha256WithRsa(data, keyPair.getPrivate());
        boolean isValid = CryptoUtils.verifySha256WithRsa(data, signature, keyPair.getPublic());

        assertTrue(isValid);
    }

    @Test
    void verify_withDifferentData_shouldReturnFalse() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPair keyPair = CryptoUtils.generateKeyPair();
        byte[] data = "data".getBytes();
        byte[] otherData = "other".getBytes();

        byte[] signature = CryptoUtils.signSha256WithRsa(data, keyPair.getPrivate());

        boolean isValid = CryptoUtils.verifySha256WithRsa(otherData, signature, keyPair.getPublic());

        assertFalse(isValid);
    }

    @Test
    void encodeAndDecodePublicKey_shouldReturnSameKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = CryptoUtils.generateKeyPair();

        String encoded = CryptoUtils.encodePublicKey(keyPair.getPublic());
        var decoded = CryptoUtils.publicKeyFromBase64(encoded);

        assertEquals(keyPair.getPublic(), decoded);
    }

    @Test
    void encodeAndDecodePrivateKey_shouldReturnSameKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPair keyPair = CryptoUtils.generateKeyPair();

        String encoded = CryptoUtils.encodePrivateKey(keyPair.getPrivate());
        var decoded = CryptoUtils.privateKeyFromBase64(encoded);

        assertEquals(keyPair.getPrivate(), decoded);
    }




}