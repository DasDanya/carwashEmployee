package ru.pin120.carwashemployee.CategoriesAndServices;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

/**
 * FX представление услуги
 */
@NoArgsConstructor
public class ServiceFX {

    public static final int MAX_LENGTH_SERVICE_NAME = 30;
    public static final String REGEX = "^[a-zA-Zа-яА-ЯёЁ0-9 -]+$";

    /**
     * Название услуги
     */
    private StringProperty name;
//    private StringProperty catName;
//
//    public String getCatName() {
//        return catName.get();
//    }
//
//    public StringProperty catNameProperty() {
//        return catName;
//    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Конструктор для создания объекта ServiceFX
     * @param name Название услуги
     */
    public ServiceFX(String name) {
        this.name = new SimpleStringProperty(name);
    }

//    public ServiceFX(String servName, String catName) {
//        this.servName = new SimpleStringProperty(servName);
//        this.catName = new SimpleStringProperty(catName);
//    }
}
