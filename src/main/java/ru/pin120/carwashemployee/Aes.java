package ru.pin120.carwashemployee;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


/**
 * ����� Aes ������������� ����������� ������ ��� ���������� � ������������ ������ � �������������� ��������� AES.
 * ��� ������ � ������� ������������ ����, ���������� �� ���������������� ������ AppHelper.
 */
public class Aes {

    /**
     * �������� ���������� AES.
     */
    private static final String ALGORITHM = "AES";

    /**
     * ������� ��������� ������ � �������������� ����� AES.
     *
     * @param data ������ ��� ����������
     * @return ������������� ������ � ���� ������ Base64
     * @throws NoSuchAlgorithmException ���� �� �������������� ��������� �������� ����������
     * @throws InvalidKeyException ���� ������ ������������ ���� ����������
     * @throws NoSuchPaddingException ���� ������ ������������ ��� ���������� � ��������� ����������
     * @throws IllegalBlockSizeException ���� �������� ������ ��� ��������� ����� ������ � ��������� ����������
     * @throws BadPaddingException ���� �������� ������ ��� ��������� �������������� ���������� � ��������� ����������
     */
    public static String encrypt(String data) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AppHelper.getSecretKeyForAes().getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * ��������� ��������� ������������� ������ � �������������� ����� AES.
     *
     * @param encryptedData ������������� ������ � ���� ������ Base64
     * @return �������� ������ ����� ������������
     * @throws NoSuchAlgorithmException ���� �� �������������� ��������� �������� ����������
     * @throws InvalidKeyException ���� ������ ������������ ���� ����������
     * @throws NoSuchPaddingException ���� ������ ������������ ��� ���������� � ��������� ����������
     * @throws IllegalBlockSizeException ���� �������� ������ ��� ��������� ����� ������ � ��������� ����������
     * @throws BadPaddingException ���� �������� ������ ��� ��������� �������������� ���������� � ��������� ����������
     */
    public static String decrypt(String encryptedData) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AppHelper.getSecretKeyForAes().getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedBytes);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}
