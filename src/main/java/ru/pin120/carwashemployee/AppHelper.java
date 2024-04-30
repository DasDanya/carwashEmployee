package ru.pin120.carwashemployee;

import javafx.scene.image.Image;

import java.util.ResourceBundle;

public class AppHelper {

    private static final ResourceBundle rb = ResourceBundle.getBundle("ru.pin120.carwashemployee.Application");

    public static Image getMainIcon(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/appIcon.png"));}
    public static String getCarWashAPI(){
        return rb.getString("URL_CARWASH_API");
    }

    public static String getErrorText(){return rb.getString("ERROR_TEXT");}

    public static String getHttpErrorText(){return rb.getString("ERROR_HTTP");}

    public static String getCut(){return rb.getString("CUT");}
    public static String getCopy(){return rb.getString("COPY");}
    public static String getPaste(){return rb.getString("PASTE");}

    public static String getExistsEntityTextEnd(){return rb.getString("EXISTS_ENTITY_TEXT_END");}
}
