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
 * ����� AppHelper ������������� ����������� ������ � �������� ��� ������ � ��������� � ����������� ����������.
 * �������� ������ ��� �������� ������� �� �����,
 * ������� � ������������, ��������� �������� � ������ ���������� ������������.
 */
public class AppHelper {

    /**
     * ������ �������������� ������ � ������������.
     */
    @Getter
    @Setter
    private static List<String> userInfo = new ArrayList<>();

    /**
     * ������ �������, ���������� �� �����.
     */
    private static Properties properties = new Properties();

    /**
     * ��������� �������� ���������� �� ����� application.properties.
     * ���� ������ ���������� � ��� �� ��������, ��� � JAR ���� ����������.
     * ������������ ��������� UTF-8 ��� ������ �����.
     * � ������ ������ �������� �������� ���������� ���� � ���������� �� ������.
     */
    public static void loadProperties() {
        try {
            URL jarLocation = AppHelper.class.getProtectionDomain().getCodeSource().getLocation();

            // ����������� URL � ���� � �����
            Path jarPath = Paths.get(jarLocation.toURI());

            // �������� ������������ �������, � ������� ��������� JAR ����
            Path jarDir = jarPath.getParent();

            // ���������� ���� � application.properties
            Path propertiesPath = jarDir.resolve("application.properties");

            // ��������� �������� �� ����� � ��������� ���������
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
     * ���������� ����������� ������� ������ ����������.
     *
     * @return ����������� ������� ������
     */
    public static Image getMainIcon(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/appIcon.png"));}

    /**
     * ���������� ����������� ������� �� ���������
     *
     * @return ����������� �� ��������� ��� �������
     */
    public static Image getDefaultAvatar(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/avatardefault.jpg"));}

    /**
     * ���������� ����������� "��� ����".
     *
     * @return ����������� "��� ����"
     */
    public static Image getNoPhoto(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/noPhoto.jpeg"));}

    /**
     * ���������� URL ����� API
     *
     * @return URL ����� API
     */
    public static String getCarWashAPI(){
        //return rb.getString("URL_CARWASH_API");
        return properties.getProperty("URL_CARWASH_API");
    }

    /**
     * ���������� ����� ��� ��������� �� ������
     *
     * @return ����� ��� ��������� �� ������
     */
    public static String getErrorText(){
        //return rb.getString("ERROR_TEXT");
        return properties.getProperty("ERROR_TEXT");
    }

    /**
     * ���������� ����� HTTP ������.
     *
     * @return ����� HTTP ������
     */
    public static String getHttpErrorText(){
        return properties.getProperty("ERROR_HTTP");
        //return rb.getString("ERROR_HTTP");
    }

    /**
     * ���������� ����� ��� �������� "��������".
     *
     * @return ����� ��� �������� "��������"
     */
    public static String getCut(){
        //return rb.getString("CUT");
        return properties.getProperty("CUT");
    }

    /**
     * ���������� ����� ��� �������� "����������".
     *
     * @return ����� ��� �������� "����������"
     */
    public static String getCopy(){
        //return rb.getString("COPY");
        return properties.getProperty("COPY");
    }

    /**
     * ���������� ����� ��� �������� "��������".
     *
     * @return ����� ��� �������� "��������"
     */
    public static String getPaste(){
        //return rb.getString("PASTE");
        return properties.getProperty("PASTE");
    }

    /**
     * ���������� ����� ��� ��������������� ���������
     *
     * @return ����� ��� ��������������� ���������
     */
    public static String getInfoText(){
        //return rb.getString("INFO_TEXT");
        return properties.getProperty("INFO_TEXT");
    }

    /**
     * ���������� ��� ������� ������ � �������.
     *
     * @return ��� ������� ������ � �������
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
     * ���������� ����� ������ �������� ���.
     *
     * @return ����� ������ �������� ���
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
     * ���������� ����� ��������� �������� ���.
     *
     * @return ����� ��������� �������� ���
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
     * ���������� ��������� ���� ��� ���������� AES.
     *
     * @return ��������� ���� ��� ���������� AES
     */
    public static String getSecretKeyForAes(){
        //return rb.getString("AES_SECRET_KEY");
        return properties.getProperty("AES_SECRET_KEY");
    }

    /**
     * ���������� ��������� � ������������� ������ � ���� ������
     *
     * @return ��������� � ������������� ������ � ���� ������
     */
    public static String getExistsEntityTextEnd(){
        //return rb.getString("EXISTS_ENTITY_TEXT_END");
        return properties.getProperty("EXISTS_ENTITY_TEXT_END");
    }

    /**
     * ���������� ����� � ������������� ������� � �����.
     *
     * @return ����� � ������������� ������� � �����
     */
    public static String getCannotAccessFormText(){
        //return rb.getString("CANNOT_ACCESS_FORM");
        return properties.getProperty("CANNOT_ACCESS_FORM");
    }

    /**
     * ���������� ����� � ������������� ������� � ��������.
     *
     * @return ����� � ������������� ������� � ��������
     */
    public static String getCannotAccessOperationText(){
        //return rb.getString("CANNOT_ACCESS_OPERATION");
        return properties.getProperty("CANNOT_ACCESS_OPERATION");
    }


}
