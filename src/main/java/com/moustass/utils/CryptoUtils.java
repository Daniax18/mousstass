package com.moustass.utils;

import com.moustass.exception.SignatureRSAException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class CryptoUtils {
    private CryptoUtils() {}

    private static final String SHA_256_ALG = "SHA-256";
    private static final String SHA_256_WITH_RSA_ALG = "SHA256withRSA";
    private static final String RSA_ALG = "RSA";

    public static byte[] sha256(File file) throws IOException, NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256_ALG);
            try (InputStream in = Files.newInputStream(file.toPath())) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    md.update(buffer, 0, read);
                }
            }
            return md.digest();
        }catch (IOException | NoSuchAlgorithmException ex){
            throw new SignatureRSAException("Error RSA :" + ex.getMessage());
        }
    }

    public static byte[] signSha256WithRsa(byte[] data, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            Signature sig = Signature.getInstance(SHA_256_WITH_RSA_ALG);
            sig.initSign(privateKey);
            sig.update(data);
            return sig.sign();
        }catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex){
            throw new SignatureRSAException("Error RSA :" + ex.getMessage());
        }
    }

    public static boolean verifySha256WithRsa(byte[] data, byte[] signature, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            Signature sig = Signature.getInstance(SHA_256_WITH_RSA_ALG);
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signature);
        }catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex){
            throw new SignatureRSAException("Error RSA :" + ex.getMessage());
        }
    }

    public static String b64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] fromB64(String s) {
        return Base64.getDecoder().decode(s);
    }

    public static PrivateKey privateKeyFromBase64(String privateKeyStr)
            throws InvalidKeySpecException, NoSuchAlgorithmException{
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALG);

            return keyFactory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SignatureRSAException("Invalid private key format :" + e.getMessage());
        }
    }

    public static PublicKey publicKeyFromBase64(String publicKeyStr)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALG);

            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SignatureRSAException("Invalid public key format :" + e.getMessage());
        }
    }

    public static String sha256Hex(String input) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256_ALG);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException ex){
            throw new SignatureRSAException("Error RSA : " + ex.getMessage());
        }
    }

    public static String encodePublicKey(PublicKey pk) {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    public static String encodePrivateKey(PrivateKey sk) {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(sk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    public static String generateSalt(int length) {
        byte[] b = new byte[length];
        new SecureRandom().nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALG);
            kpg.initialize(2048);
            return kpg.generateKeyPair();
        }catch (NoSuchAlgorithmException ex){
            throw new SignatureRSAException("Error RSA :" + ex.getMessage());
        }
    }

    public static String hashPassword(
            String salt,
            String password,
            String pk,
            String sk
    ) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256_ALG);
            String input = salt + password + pk + sk;
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new SignatureRSAException("Error RSA: ");
        }
    }


}
