package ru.pin120.carwashemployee;

import javafx.scene.image.Image;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AppHelper {

    private static final ResourceBundle rb = ResourceBundle.getBundle("ru.pin120.carwashemployee.Application");

    public static Image getMainIcon(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/appIcon.png"));}

    public static Image getDefaultAvatar(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/avatardefault.jpg"));}

    public static File getDefaultAvatarAsFile(){
        try (InputStream inputStream = AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/avatardefault.jpg")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Файл не найден: /ru/pin120/carwashemployee/images/avatardefault.jpg");
            }

            File tempFile = Files.createTempFile("avatardefault", ".jpg").toFile();
            tempFile.deleteOnExit();

            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (IOException e) {
            FXHelper.showErrorAlert(e.getMessage());
            return null;
        }
    }
    public static String getCarWashAPI(){
        return rb.getString("URL_CARWASH_API");
    }

    public static String getErrorText(){return rb.getString("ERROR_TEXT");}

    public static String getHttpErrorText(){return rb.getString("ERROR_HTTP");}

    public static String getCut(){return rb.getString("CUT");}
    public static String getCopy(){return rb.getString("COPY");}
    public static String getPaste(){return rb.getString("PASTE");}

    public static String getInfoText(){return rb.getString("INFO_TEXT");}

    public static String getExistsEntityTextEnd(){return rb.getString("EXISTS_ENTITY_TEXT_END");}

    private static String[] getDaysOfWeek(){
        return new String[]{rb.getString("MONDAY"), rb.getString("TUESDAY"), rb.getString("WEDNESDAY"), rb.getString("THURSDAY"),
                rb.getString("FRIDAY"), rb.getString("SATURDAY"), rb.getString("SUNDAY")};
    }
    public static String convertDayToString(int day){
        String[] daysOfWeek = getDaysOfWeek();
        return switch (day) {
            case 1 -> daysOfWeek[0];
            case 2 -> daysOfWeek[1];
            case 3 -> daysOfWeek[2];
            case 4 -> daysOfWeek[3];
            case 5 -> daysOfWeek[4];
            case 6 -> daysOfWeek[5];
            case 7 -> daysOfWeek[6];
            default -> null;
        };
    }
}
