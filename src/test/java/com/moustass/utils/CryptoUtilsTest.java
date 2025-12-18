package com.moustass.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptoUtilsTest {

    @Test
    public void b64_and_fromB64_roundtrip() {
        byte[] data = new byte[]{1,2,3,4,5};
        String s = CryptoUtils.b64(data);
        byte[] out = CryptoUtils.fromB64(s);
        Assertions.assertArrayEquals(data, out);
    }

    @Test
    public void sha256Hex_nonEmpty() throws Exception {
        String h = CryptoUtils.sha256Hex("hello");
        Assertions.assertNotNull(h);
        Assertions.assertTrue(h.length() > 0);
    }

    @Test
    public void generateSalt_lengthDifferent() {
        String s1 = CryptoUtils.generateSalt(8);
        String s2 = CryptoUtils.generateSalt(8);
        Assertions.assertNotNull(s1);
        Assertions.assertNotEquals(s1, s2);
    }

    @Test
    public void keyEncodeDecode_and_sign_verify() throws Exception {
        KeyPair kp = CryptoUtils.generateKeyPair();
        PublicKey pub = kp.getPublic();
        PrivateKey priv = kp.getPrivate();

        String pubEnc = CryptoUtils.encodePublicKey(pub);
        String privEnc = CryptoUtils.encodePrivateKey(priv);

        PublicKey pub2 = CryptoUtils.publicKeyFromBase64(pubEnc);
        PrivateKey priv2 = CryptoUtils.privateKeyFromBase64(privEnc);

        byte[] data = "payload".getBytes();
        byte[] sig = CryptoUtils.signSha256WithRsa(data, priv2);
        boolean ok = CryptoUtils.verifySha256WithRsa(data, sig, pub2);
        Assertions.assertTrue(ok);
    }

    @Test
    public void hashPassword_consistent() {
        String h1 = CryptoUtils.hashPassword("s","p","pk","sk");
        String h2 = CryptoUtils.hashPassword("s","p","pk","sk");
        Assertions.assertEquals(h1, h2);
    }

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
