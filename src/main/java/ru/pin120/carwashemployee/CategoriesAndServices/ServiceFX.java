package ru.pin120.carwashemployee.CategoriesAndServices;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceFX {

    private StringProperty name;
    private StringProperty catName;

    public String getCatName() {
        return catName.get();
    }

    public StringProperty catNameProperty() {
        return catName;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ServiceFX(String name) {
        this.name = new SimpleStringProperty(name);
    }

//    public ServiceFX(String servName, String catName) {
//        this.servName = new SimpleStringProperty(servName);
//        this.catName = new SimpleStringProperty(catName);
//    }
}
