package org.zhenchao.oauth.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.CodecException;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密算法工具类
 *
 * @author zhenchao.wang 2017-01-02 13:49
 * @version 1.0.0
 */
public abstract class CipherUtils {

    private static final String AES = "AES";

    private static final String AES_KEY = "b8ixjPV1z608PETq+lRfRQ==";

    /**
     * 加解密算法/模式/填充方式
     * ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
     */
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 生成AES密钥
     *
     * @return
     */
    public static byte[] genAesKey() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
        return new byte[0];
    }

    /**
     * AES 加密
     * 返回结果进行base64编码
     *
     * @param data
     * @return
     * @throws CodecException
     */
    public static byte[] aesEncrypt(byte[] data) throws CodecException {

        if (ArrayUtils.isEmpty(data)) {
            throw new CodecException("Aes encrypt error, the input data is empty!", ErrorCode.PARAMETER_ERROR);
        }

        // AES加密
        try {
            Key key = new SecretKeySpec(Base64.decodeBase64(AES_KEY), AES);
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            //设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, key, generateIV());
            return Base64.encodeBase64(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException e) {
            throw new CodecException("Aes encrypt error!", e, ErrorCode.AES_ENCRYPT_ERROR);
        }

    }

    /**
     * AES 加密
     *
     * @param data
     * @return
     * @throws CodecException
     */
    public static byte[] aesEncrypt(String data) throws CodecException {

        if (StringUtils.isBlank(data)) {
            throw new CodecException("Aes encrypt error, the input data is empty!", ErrorCode.PARAMETER_ERROR);
        }

        // AES加密
        try {
            return aesEncrypt(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // never happen
        }
        return new byte[0];

    }

    /**
     * AES 解密
     *
     * @param encryptedData
     * @return
     * @throws CodecException
     */
    public static byte[] aesDecrypt(byte[] encryptedData) throws CodecException {

        if (ArrayUtils.isEmpty(encryptedData)) {
            throw new CodecException("Aes decrypt error, the input data is empty!", ErrorCode.PARAMETER_ERROR);
        }

        // AES解密
        try {
            Key key = new SecretKeySpec(Base64.decodeBase64(AES_KEY), AES);
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            //设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, key, generateIV());
            return cipher.doFinal(Base64.decodeBase64(encryptedData));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException e) {
            throw new CodecException("Aes decrypt error!", e, ErrorCode.AES_DECRYPT_ERROR);
        }

    }

    /**
     * AES 解密
     *
     * @param encryptedData
     * @return
     * @throws CodecException
     */
    public static byte[] aesDecrypt(String encryptedData) throws CodecException {

        if (StringUtils.isBlank(encryptedData)) {
            throw new CodecException("Aes decrypt error, the input data is empty!", ErrorCode.PARAMETER_ERROR);
        }

        // AES解密
        try {
            return aesDecrypt(encryptedData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // never happen
        }
        return new byte[0];

    }

    /**
     * PBKDF2加密
     *
     * @param text 加密字符串
     * @param salt 盐
     * @param iterationCount 迭代次数
     * @param length 密钥长度
     * @return
     * @throws CodecException
     */
    public static String pbkdf2(String text, String salt, int iterationCount, int length) throws CodecException {
        KeySpec keySpec = new PBEKeySpec(text.toCharArray(), salt.getBytes(), iterationCount, length);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Base64.encodeBase64String(secretKeyFactory.generateSecret(keySpec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CodecException("PDKDF2 Error", e, ErrorCode.PBKDF2_ENCRYPT_ERROR);
        }
    }

    /**
     * PBKDF2加密
     * 默认迭代9次，密钥长度32个字符
     *
     * @param text 加密字符串
     * @param salt 盐
     * @return
     * @throws CodecException
     */
    public static String pbkdf2(String text, String salt) throws CodecException {
        return pbkdf2(text, salt, 8, 128);
    }

    /**
     * 生成IV
     * IV为一个16字节的数组，这里数据全为0
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidParameterSpecException
     */
    private static AlgorithmParameters generateIV() throws NoSuchAlgorithmException, InvalidParameterSpecException {
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(AES);
        params.init(new IvParameterSpec(iv));
        return params;
    }

}
