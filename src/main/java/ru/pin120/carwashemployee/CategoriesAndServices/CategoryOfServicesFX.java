package ru.pin120.carwashemployee.CategoriesAndServices;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryOfServicesFX {

    public static final int MAX_LENGTH_CATEGORY_NAME = 30;
    private StringProperty name;

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public CategoryOfServicesFX(String name){
        this.name = new SimpleStringProperty(name);
    }


}
