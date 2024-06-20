package ru.pin120.carwashemployee.Clients;

import javafx.beans.property.*;

/**
 * FX представление клиента
 */
public class ClientFX {

    public static final int MAX_DISCOUNT=100;
    public static final int MAX_PHONE_FILLING=10;
    public static final int MAX_SURNAME_LENGTH=50;
    public static final int MAX_NAME_LENGTH=50;
    public static final String SURNAME_REGEX = "^[А-ЯЁа-яё-]+$";
    public static final String NAME_REGEX = "^[А-ЯЁа-яё-]+$";

    /**
     * id клиента
     */
    private LongProperty clId;
    /**
     * фамилия
     */
    private StringProperty clSurname;
    /**
     * имя
     */
    private StringProperty clName;
    /**
     * номер телефона
     */
    private StringProperty clPhone;
    /**
     * скидка
     */
    private IntegerProperty clDiscount;

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

    public Integer getClDiscount() {
        return clDiscount.get();
    }


    public IntegerProperty clDiscountProperty() {
        return clDiscount;
    }

    /**
     * Конструктор для создания объекта ClientFX
     * @param clId id клиента
     * @param clSurname фамилия
     * @param clName имя
     * @param clPhone номер телефона
     * @param clDiscount скидка
     */
    public ClientFX(Long clId, String clSurname, String clName, String clPhone, Integer clDiscount) {
        this.clId = new SimpleLongProperty(clId);
        this.clSurname = new SimpleStringProperty(clSurname);
        this.clName = new SimpleStringProperty(clName);
        this.clPhone = new SimpleStringProperty(clPhone);
        this.clDiscount = new SimpleIntegerProperty(clDiscount);
    }
}
