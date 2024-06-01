package ru.pin120.carwashemployee.PriceListPosition;

import javafx.beans.property.*;
import javafx.scene.control.CheckBox;


public class ServiceWithPriceListFX {

    private ObjectProperty<CheckBox> select;
    private StringProperty catName;
    private StringProperty servName;
    private IntegerProperty plPrice;
    private IntegerProperty plTime;

    public CheckBox getSelect() {
        return select.get();
    }

    public ObjectProperty<CheckBox> selectProperty() {
        return select;
    }

    public String getCatName() {
        return catName.get();
    }

    public StringProperty catNameProperty() {
        return catName;
    }

    public String getServName() {
        return servName.get();
    }

    public StringProperty servNameProperty() {
        return servName;
    }

    public int getPlPrice() {
        return plPrice.get();
    }

    public IntegerProperty plPriceProperty() {
        return plPrice;
    }

    public int getPlTime() {
        return plTime.get();
    }

    public IntegerProperty plTimeProperty() {
        return plTime;
    }

    public ServiceWithPriceListFX(CheckBox select, String catName, String servName, Integer plPrice, Integer plTime, ServiceInPriceListSelectable selectable) {
        this.select = new SimpleObjectProperty<>(select);
        this.catName = new SimpleStringProperty(catName);
        this.servName = new SimpleStringProperty(servName);
        this.plPrice = new SimpleIntegerProperty(plPrice);
        this.plTime = new SimpleIntegerProperty(plTime);

        this.select.get().selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            selectable.onCheckBoxChanged(this,isNowSelected);
        });
    }
}
