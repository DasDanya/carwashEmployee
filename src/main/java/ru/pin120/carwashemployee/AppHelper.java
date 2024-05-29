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

    public static Image getNoPhoto(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/noPhoto.jpeg"));}

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

    public static String getYes(){return rb.getString("YES");}
    public static String getNo(){return rb.getString("NO");}
    public static String getExistsEntityTextEnd(){return rb.getString("EXISTS_ENTITY_TEXT_END");}

}
