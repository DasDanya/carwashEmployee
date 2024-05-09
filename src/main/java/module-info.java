module ru.pin120.carwashemployee {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires okhttp3;
    requires com.google.gson;


    opens ru.pin120.carwashemployee to javafx.fxml;
    exports ru.pin120.carwashemployee;
    opens ru.pin120.carwashemployee.CategoriesAndServices to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.CategoriesAndServices;

    opens ru.pin120.carwashemployee.CategoriesOfTransport to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.CategoriesOfTransport;
    exports ru.pin120.carwashemployee.PriceListPosition;
    opens ru.pin120.carwashemployee.PriceListPosition to com.google.gson, javafx.fxml;

    opens ru.pin120.carwashemployee.Transport to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.Transport;

}