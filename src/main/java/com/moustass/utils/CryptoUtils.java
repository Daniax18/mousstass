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

/**
 * Cryptographic utility class.
 * <p>
 * This class provides helper methods for hashing, digital signature
 * generation and verification, key encoding/decoding, and secure
 * password processing.
 * </p>
 * <p>
 * It is designed as a static utility class and cannot be instantiated.
 * </p>
 */
public final class CryptoUtils {
    private CryptoUtils() {}

    private static final String SHA_256_ALG = "SHA-256";
    private static final String SHA_256_WITH_RSA_ALG = "SHA256withRSA";
    private static final String RSA_ALG = "RSA";

    /**
     * Computes the SHA-256 hash of a file.
     *
     * @param file the file to hash
     * @return the SHA-256 hash as a byte array
     * @throws IOException if an I/O error occurs while reading the file
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is unavailable
     */
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

    /**
     * Signs data using SHA-256 with RSA.
     *
     * @param data the data to sign
     * @param privateKey the private key used for signing
     * @return the generated digital signature
     * @throws NoSuchAlgorithmException if the algorithm is unavailable
     * @throws InvalidKeyException if the private key is invalid
     * @throws SignatureException if the signing operation fails
     */
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

    /**
     * Verifies a SHA-256 with RSA digital signature.
     *
     * @param data the original data
     * @param signature the digital signature to verify
     * @param publicKey the public key used for verification
     * @return {@code true} if the signature is valid, {@code false} otherwise
     * @throws NoSuchAlgorithmException if the algorithm is unavailable
     * @throws InvalidKeyException if the public key is invalid
     * @throws SignatureException if the verification fails
     */
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

    /**
     * Encodes a byte array into a Base64 string.
     *
     * @param bytes the byte array to encode
     * @return the Base64-encoded string
     */
    public static String b64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Decodes a Base64 string into a byte array.
     *
     * @param s the Base64-encoded string
     * @return the decoded byte array
     */
    public static byte[] fromB64(String s) {
        return Base64.getDecoder().decode(s);
    }

    /**
     * Reconstructs a private key from a Base64-encoded string.
     *
     * @param privateKeyStr the Base64-encoded private key
     * @return the reconstructed {@link PrivateKey}
     * @throws InvalidKeySpecException if the key specification is invalid
     * @throws NoSuchAlgorithmException if the algorithm is unavailable
     */
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

    /**
     * Reconstructs a public key from a Base64-encoded string.
     *
     * @param publicKeyStr the Base64-encoded public key
     * @return the reconstructed {@link PublicKey}
     * @throws InvalidKeySpecException if the key specification is invalid
     * @throws NoSuchAlgorithmException if the algorithm is unavailable
     */
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

    /**
     * Computes the SHA-256 hash of a string and returns it as a hexadecimal value.
     *
     * @param input the input string
     * @return the SHA-256 hash in hexadecimal format
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is unavailable
     */
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

    /**
     * Encodes a public key into a Base64 string.
     *
     * @param pk the public key to encode
     * @return the Base64-encoded public key
     */
    public static String encodePublicKey(PublicKey pk) {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    /**
     * Encodes a private key into a Base64 string.
     *
     * @param sk the private key to encode
     * @return the Base64-encoded private key
     */
    public static String encodePrivateKey(PrivateKey sk) {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(sk.getEncoded());
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    /**
     * Generates a random cryptographic salt.
     *
     * @param length the desired length of the salt
     * @return the generated salt
     */
    public static String generateSalt(int length) {
        byte[] b = new byte[length];
        new SecureRandom().nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }

    /**
     * Generates an RSA key pair.
     *
     * @return the generated {@link KeyPair}
     * @throws NoSuchAlgorithmException if the RSA algorithm is unavailable
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALG);
            kpg.initialize(2048);
            return kpg.generateKeyPair();
        }catch (NoSuchAlgorithmException ex){
            throw new SignatureRSAException("Error RSA :" + ex.getMessage());
        }
    }

    /**
     * Generates a secure password hash.
     * <p>
     * The hash is computed by combining the salt, password, and
     * cryptographic keys to strengthen resistance against attacks.
     * </p>
     *
     * @param salt the cryptographic salt
     * @param password the raw password
     * @param pk the public key
     * @param sk the private key
     * @return the generated password hash
     */
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
