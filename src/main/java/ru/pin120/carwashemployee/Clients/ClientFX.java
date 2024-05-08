package ru.pin120.carwashemployee.Clients;

import javafx.beans.property.*;


public class ClientFX {

    private LongProperty clId;
    private StringProperty clSurname;
    private StringProperty clName;
    private StringProperty clPhone;
    private IntegerProperty clSale;

    public long getClId() {
        return clId.get();
    }

    public LongProperty clIdProperty() {
        return clId;
    }

    public String getClSurname() {
        return clSurname.get();
    }

    public StringProperty clSurnameProperty() {
        return clSurname;
    }

    public String getClName() {
        return clName.get();
    }

    public StringProperty clNameProperty() {
        return clName;
    }

    public String getClPhone() {
        return clPhone.get();
    }

    public StringProperty clPhoneProperty() {
        return clPhone;
    }

    public int getClSale() {
        return clSale.get();
    }

    public IntegerProperty clSaleProperty() {
        return clSale;
    }

    public ClientFX(Long clId, String clSurname, String clName, String clPhone, Integer clSale) {
        this.clId = new SimpleLongProperty(clId);
        this.clSurname = new SimpleStringProperty(clSurname);
        this.clName = new SimpleStringProperty(clName);
        this.clPhone = new SimpleStringProperty(clPhone);
        this.clSale = new SimpleIntegerProperty(clSale);
    }
}
