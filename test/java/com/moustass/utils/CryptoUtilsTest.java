package com.moustass.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
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
}
