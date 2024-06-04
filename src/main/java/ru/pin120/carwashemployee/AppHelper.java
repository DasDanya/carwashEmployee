package ru.pin120.carwashemployee;

import javafx.scene.image.Image;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppHelper {

    private static final ResourceBundle rb = ResourceBundle.getBundle("ru.pin120.carwashemployee.Application");

    public static Image getMainIcon(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/appIcon.png"));}

    public static Image getDefaultAvatar(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/avatardefault.jpg"));}

    public static Image getNoPhoto(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/noPhoto.jpeg"));}

    public static String getCarWashAPI(){
        return rb.getString("URL_CARWASH_API");
    }

    public static String getErrorText(){return rb.getString("ERROR_TEXT");}

    public static String getHttpErrorText(){return rb.getString("ERROR_HTTP");}

    public static String getCut(){return rb.getString("CUT");}
    public static String getCopy(){return rb.getString("COPY");}
    public static String getPaste(){return rb.getString("PASTE");}

    public static String getInfoText(){return rb.getString("INFO_TEXT");}


    public static Integer getStepServiceTime(){
        try {
            return Integer.parseInt(rb.getString("STEP_SERVICE_TIME"));
        }catch (Exception e){
            return null;
        }
    }

    public static LocalTime getStartWorkTime(){
        try{
            return LocalTime.parse(rb.getString("START_WORK_TIME"));
        }catch (Exception e){
            return null;
        }
    }

    public static LocalTime getEndWorkTime(){
        try{
            return LocalTime.parse(rb.getString("END_WORK_TIME"));
        }catch (Exception e){
            return null;
        }
    }

    public static String getSecretKeyForAes(){
        return rb.getString("AES_SECRET_KEY");
    }

    public static String getExistsEntityTextEnd(){return rb.getString("EXISTS_ENTITY_TEXT_END");}

    public static String getJwtToken(){
        return rb.getString("JWT_TOKEN");
    }

    public static String getUserName(){
        return rb.getString("username");
    }

    public static String getUserRole(){
        return rb.getString("USER_ROLE");
    }

}
