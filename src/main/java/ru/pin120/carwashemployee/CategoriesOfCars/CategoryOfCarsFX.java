package ru.pin120.carwashemployee.CategoriesOfCars;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryOfCarsFX {

    private StringProperty catCarsName;

    public String getCatCarsName() {
        return catCarsName.get();
    }

    public StringProperty catCarsNameProperty() {
        return catCarsName;
    }

    public CategoryOfCarsFX(String catCarsName) {
        this.catCarsName = new SimpleStringProperty(catCarsName);
    }

}
