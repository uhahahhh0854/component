package com.raizumi.component.common.handler.Impl;

import com.raizumi.component.common.enums.Gorithm;
import com.raizumi.component.common.enums.MCipher;
import com.raizumi.component.common.handler.DEHandleDescriptor;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESDefaultDEHandleProcessor implements DEHandleDescriptor {

    public SecretKeySpec SECRET_KEY;

    public Gorithm ALGORITHM;

    public SecureRandom RANDOM = new SecureRandom();

    public MCipher  MCIPHER = MCipher.NONE;

    public AESDefaultDEHandleProcessor(String key, String algorithm) {
        process(key, algorithm);
        audit();
    }

    public void process(String key, String algorithm){
        if ((key == null && algorithm != null) || (key != null && algorithm == null)){
            throw new RuntimeException("AES setting is in deficiency.");
        }

        if (key == null || (key.trim().isEmpty() || algorithm.trim().isEmpty())){
            return;
        }

        this.SECRET_KEY = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        Gorithm gorithm = Gorithm.getType(algorithm);
        if(gorithm == null){
            throw new RuntimeException("AES type is not defined.");
        }
        this.ALGORITHM = gorithm;
        this.MCIPHER = MCipher.IS;
    }

    public void audit(){
        if (MCIPHER == MCipher.IS && (SECRET_KEY == null || ALGORITHM == null)) {
            throw new RuntimeException(
                    "Data under the setting is need to be encrypted and decrypted, but no secret key and algorithm is provided");
        }
    }

    @Override
    public byte[] decrypt(byte[] data) {
        try {
            if (MCIPHER == MCipher.NONE) {
                return data;
            }
            Integer ivLength = ALGORITHM.getIv();

            // 1. 检查数据长度
            if (data.length < ivLength) {
                throw new IllegalArgumentException("Illegally, decrypt data is too short");
            }

            // 2. 提取IV（前x字节）
            byte[] iv = Arrays.copyOfRange(data, 0, ivLength);
            byte[] content = Arrays.copyOfRange(data, ivLength, data.length);

            // 3. 解密
            Cipher cipher = Cipher.getInstance(ALGORITHM.getAlgorithm());
            if (ALGORITHM == Gorithm.GCM) {
                cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, new GCMParameterSpec(ivLength, iv));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, new IvParameterSpec(iv));
            }
            return cipher.doFinal(content);
        } catch (Exception e){
            throw new RuntimeException("Decryption error. \n" + e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try{
            if (MCIPHER == MCipher.NONE) {
                return data;
            }

            Integer ivLength = ALGORITHM.getIv();

            byte[] iv = new byte[ivLength];
            RANDOM.nextBytes(iv);

            // 2. 初始化加密器
            Cipher cipher = Cipher.getInstance(ALGORITHM.getAlgorithm());
            if (ALGORITHM == Gorithm.GCM){
                cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, new GCMParameterSpec(ivLength, iv));
            }else{
                cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, new IvParameterSpec(iv));
            }

            // 3. 执行加密
            byte[] encryptedData = cipher.doFinal(data);

            // 4. 组合IV+密文 (IV不需要保密)
            byte[] result = new byte[ivLength + encryptedData.length];
            System.arraycopy(iv, 0, result, 0, ivLength);
            System.arraycopy(encryptedData, 0, result, ivLength, encryptedData.length);

            return result;
        }catch (Exception e){
            throw new RuntimeException("Encryption error, \n" + e);
        }
    }
}
