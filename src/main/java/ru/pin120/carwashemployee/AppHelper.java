package ru.pin120.carwashemployee;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Класс AppHelper предоставляет статические методы и свойства для работы с ресурсами и настройками приложения.
 * Включает методы для загрузки свойств из файла,
 * доступа к изображениям, текстовым ресурсам и другим параметрам конфигурации.
 */
public class AppHelper {

    /**
     * Список информационных данных о пользователе.
     */
    @Getter
    @Setter
    private static List<String> userInfo = new ArrayList<>();

    /**
     * Объект свойств, получаемых из файла.
     */
    private static Properties properties = new Properties();

    /**
     * Загружает свойства приложения из файла application.properties.
     * Файл должен находиться в том же каталоге, что и JAR файл приложения.
     * Используется кодировка UTF-8 для чтения файла.
     * В случае ошибки загрузки вызывает диалоговое окно с сообщением об ошибке.
     */
    public static void loadProperties() {
        try {
            URL jarLocation = AppHelper.class.getProtectionDomain().getCodeSource().getLocation();

            // Преобразуем URL в путь к файлу
            Path jarPath = Paths.get(jarLocation.toURI());

            // Получаем родительский каталог, в котором находится JAR файл
            Path jarDir = jarPath.getParent();

            // Определяем путь к application.properties
            Path propertiesPath = jarDir.resolve("application.properties");

            // Загружаем свойства из файла с указанием кодировки
            try (FileInputStream fileInputStream = new FileInputStream(propertiesPath.toFile());
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8")) {
                properties.load(inputStreamReader);

            }

        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }


    private static final ResourceBundle rb = ResourceBundle.getBundle("ru.pin120.carwashemployee.Application");

    /**
     * Возвращает изображение главной иконки приложения.
     *
     * @return изображение главной иконки
     */
    public static Image getMainIcon(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/appIcon.png"));}

    /**
     * Возвращает изображение мойщика по умолчанию
     *
     * @return изображение по умолчанию для мойщика
     */
    public static Image getDefaultAvatar(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/avatardefault.jpg"));}

    /**
     * Возвращает изображение "нет фото".
     *
     * @return изображение "нет фото"
     */
    public static Image getNoPhoto(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/noPhoto.jpeg"));}

    /**
     * Возвращает URL адрес API
     *
     * @return URL адрес API
     */
    public static String getCarWashAPI(){
        //return rb.getString("URL_CARWASH_API");
        return properties.getProperty("URL_CARWASH_API");
    }

    /**
     * Возвращает текст для сообщения об ошибке
     *
     * @return текст для сообщения об ошибке
     */
    public static String getErrorText(){
        //return rb.getString("ERROR_TEXT");
        return properties.getProperty("ERROR_TEXT");
    }

    /**
     * Возвращает текст HTTP ошибки.
     *
     * @return текст HTTP ошибки
     */
    public static String getHttpErrorText(){
        return properties.getProperty("ERROR_HTTP");
        //return rb.getString("ERROR_HTTP");
    }

    /**
     * Возвращает текст для операции "Вырезать".
     *
     * @return текст для операции "Вырезать"
     */
    public static String getCut(){
        //return rb.getString("CUT");
        return properties.getProperty("CUT");
    }

    /**
     * Возвращает текст для операции "Копировать".
     *
     * @return текст для операции "Копировать"
     */
    public static String getCopy(){
        //return rb.getString("COPY");
        return properties.getProperty("COPY");
    }

    /**
     * Возвращает текст для операции "Вставить".
     *
     * @return текст для операции "Вставить"
     */
    public static String getPaste(){
        //return rb.getString("PASTE");
        return properties.getProperty("PASTE");
    }

    /**
     * Возвращает текст для информационного сообщения
     *
     * @return текст для информационного сообщения
     */
    public static String getInfoText(){
        //return rb.getString("INFO_TEXT");
        return properties.getProperty("INFO_TEXT");
    }

    /**
     * Возвращает шаг времени услуги в минутах.
     *
     * @return шаг времени услуги в минутах
     */
    public static Integer getStepServiceTime(){
        try {
            //return Integer.parseInt(rb.getString("STEP_SERVICE_TIME"));
            return Integer.parseInt(properties.getProperty("STEP_SERVICE_TIME"));
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Возвращает время начала рабочего дня.
     *
     * @return время начала рабочего дня
     */
    public static LocalTime getStartWorkTime(){
        try{
            //return LocalTime.parse(rb.getString("START_WORK_TIME"));
            return LocalTime.parse(properties.getProperty("START_WORK_TIME"));
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Возвращает время окончания рабочего дня.
     *
     * @return время окончания рабочего дня
     */
    public static LocalTime getEndWorkTime(){
        try{
            //return LocalTime.parse(rb.getString("END_WORK_TIME"));
            return LocalTime.parse(properties.getProperty("END_WORK_TIME"));
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Возвращает секретный ключ для шифрования AES.
     *
     * @return секретный ключ для шифрования AES
     */
    public static String getSecretKeyForAes(){
        //return rb.getString("AES_SECRET_KEY");
        return properties.getProperty("AES_SECRET_KEY");
    }

    /**
     * Возвращает сообщение о существовании записи в базе данных
     *
     * @return сообщение о существовании записи в базе данных
     */
    public static String getExistsEntityTextEnd(){
        //return rb.getString("EXISTS_ENTITY_TEXT_END");
        return properties.getProperty("EXISTS_ENTITY_TEXT_END");
    }

    /**
     * Возвращает текст о невозможности доступа к форме.
     *
     * @return текст о невозможности доступа к форме
     */
    public static String getCannotAccessFormText(){
        //return rb.getString("CANNOT_ACCESS_FORM");
        return properties.getProperty("CANNOT_ACCESS_FORM");
    }

    /**
     * Возвращает текст о невозможности доступа к операции.
     *
     * @return текст о невозможности доступа к операции
     */
    public static String getCannotAccessOperationText(){
        //return rb.getString("CANNOT_ACCESS_OPERATION");
        return properties.getProperty("CANNOT_ACCESS_OPERATION");
    }


}
