module ru.pin120.carwashemployee {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires okhttp3;
    requires com.google.gson;
    requires jfxtras.agenda;
    requires jfxtras.controls;
    requires jfxtras.common;
    requires jfxtras.fxml;
    requires jfxtras.icalendaragenda;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;


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

    opens ru.pin120.carwashemployee.Clients to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.Clients;

    opens ru.pin120.carwashemployee.ClientsTransport to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.ClientsTransport;

    opens ru.pin120.carwashemployee.Boxes to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.Boxes;

    opens ru.pin120.carwashemployee.Cleaners to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.Cleaners;

    opens ru.pin120.carwashemployee.Main to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.Main;

    opens ru.pin120.carwashemployee.WorkSchedule to javafx.fxml, com.google.gson, org.apache.poi.poi, org.apache.poi.ooxml;
    exports ru.pin120.carwashemployee.WorkSchedule;

    opens ru.pin120.carwashemployee.Bookings to javafx.fxml, com.google.gson, jfxtras.agenda, jfxtras.controls;
    exports ru.pin120.carwashemployee.Bookings;

    opens ru.pin120.carwashemployee.CategoriesOfSupplies to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.CategoriesOfSupplies;

    opens ru.pin120.carwashemployee.Supplies to javafx.fxml, com.google.gson;
    exports ru.pin120.carwashemployee.Supplies;

}