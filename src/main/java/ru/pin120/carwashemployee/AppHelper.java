package ru.pin120.carwashemployee;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppHelper {

    //private static File infoFile;
    //private static final String TEMP_FILE_PREFIX ="carwashAPI";

    @Getter
    @Setter
    private static List<String> userInfo = new ArrayList<>();

    private static final ResourceBundle rb = ResourceBundle.getBundle("ru.pin120.carwashemployee.Application");

    public static Image getMainIcon(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/appIcon.png"));}

    public static Image getDefaultAvatar(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/avatardefault.jpg"));}

    public static Image getNoPhoto(){return new Image(AppHelper.class.getResourceAsStream("/ru/pin120/carwashemployee/images/noPhoto.jpeg"));}

//    public static void createInfoFile() throws IOException {
////       File tempFile = new File(TEMP_FILE_NAME);
////       if(tempFile.exists()){
////           tempFile.delete();
////       }
//
//       infoFile = File.createTempFile("carwashAPI", ".txt");
//    }
//
//    public static void deleteTempInfoFiles(){
//        List<File> foundFiles = new ArrayList<>();
//        File tempDir = new File(System.getProperty("java.io.tmpdir"));
//        File[] files = tempDir.listFiles((dir, name) -> name.startsWith(TEMP_FILE_PREFIX));
//        int count=0;
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile()) {
//                    count++;
//                    file.delete();
//                }
//            }
//        }
//        FXHelper.showErrorAlert(count+"");
//    }
//
//
//    public static void writeInInfoFile(List<String> data) throws FileNotFoundException, UnsupportedEncodingException {
//        String currentDir = System.getProperty("user.dir");
//        File file = new File(currentDir, "info.txt");
//        //FXHelper.showInfoAlert(currentDir);
//        PrintWriter printWriter = new PrintWriter(infoFile);
//        for(String str: data){
//            printWriter.println(str);
//        }
//
//        printWriter.close();
//    }
//
//    public static List<String> getUserInfo(){
//        String currentDir = System.getProperty("user.dir");
//        File file = new File(currentDir, "info.txt");
//        List<String> lines = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(infoFile))) {
//            String line;
//            while ((line = br.readLine())!= null) {
//                lines.add(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return lines;
//    }



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

    public static String getCannotAccessFormText(){
        return rb.getString("CANNOT_ACCESS_FORM");
    }

    public static String getCannotAccessOperationText(){
        return rb.getString("CANNOT_ACCESS_OPERATION");
    }

}
