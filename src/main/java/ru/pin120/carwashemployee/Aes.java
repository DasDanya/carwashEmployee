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
 * Класс Aes предоставляет статические методы для шифрования и дешифрования данных с использованием алгоритма AES.
 * Для работы с данными используется ключ, полученный из вспомогательного класса AppHelper.
 */
public class Aes {

    /**
     * Алгоритм шифрования AES.
     */
    private static final String ALGORITHM = "AES";

    /**
     * Шифрует указанные данные с использованием ключа AES.
     *
     * @param data данные для шифрования
     * @return зашифрованные данные в виде строки Base64
     * @throws NoSuchAlgorithmException если не поддерживается указанный алгоритм шифрования
     * @throws InvalidKeyException если указан недопустимый ключ шифрования
     * @throws NoSuchPaddingException если указан недопустимый тип заполнения в алгоритме шифрования
     * @throws IllegalBlockSizeException если возникла ошибка при обработке блока данных в алгоритме шифрования
     * @throws BadPaddingException если возникла ошибка при обработке дополнительной информации в алгоритме шифрования
     */
    public static String encrypt(String data) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AppHelper.getSecretKeyForAes().getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * Дешифрует указанные зашифрованные данные с использованием ключа AES.
     *
     * @param encryptedData зашифрованные данные в виде строки Base64
     * @return исходные данные после дешифрования
     * @throws NoSuchAlgorithmException если не поддерживается указанный алгоритм шифрования
     * @throws InvalidKeyException если указан недопустимый ключ шифрования
     * @throws NoSuchPaddingException если указан недопустимый тип заполнения в алгоритме шифрования
     * @throws IllegalBlockSizeException если возникла ошибка при обработке блока данных в алгоритме шифрования
     * @throws BadPaddingException если возникла ошибка при обработке дополнительной информации в алгоритме шифрования
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
