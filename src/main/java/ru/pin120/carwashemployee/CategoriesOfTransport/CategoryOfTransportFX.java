package ru.pin120.carwashemployee.CategoriesOfTransport;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryOfTransportFX {

    private LongProperty catTrId;


    private StringProperty catTrName;

    public long getCatTrId() {
        return catTrId.get();
    }

    public LongProperty catTrIdProperty() {
        return catTrId;
    }


    public void setCatTrId(long catTrId) {
        this.catTrId.set(catTrId);
    }

    public void setCatTrName(String catTrName) {
        this.catTrName.set(catTrName);
    }

    public String getCatTrName() {
        return catTrName.get();
    }

    public StringProperty catTrNameProperty() {
        return catTrName;
    }

    public CategoryOfTransportFX(String catTrName, Long catTrId) {
        this.catTrName = new SimpleStringProperty(catTrName);
        this.catTrId = new SimpleLongProperty(catTrId);
    }

}
