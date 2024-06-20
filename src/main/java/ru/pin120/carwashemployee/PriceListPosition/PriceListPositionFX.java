package ru.pin120.carwashemployee.PriceListPosition;

import javafx.beans.property.*;

/**
 * FX представление позиции прайс-листа
 */
public class PriceListPositionFX {

    public static final int MAX_PRICE=50000;
    public static final int MAX_TIME=1440;

    /**
     * Название категории транспорта
     */
    private StringProperty catTrName;
    /**
     * Время выполнения
     */
    private IntegerProperty time;

    /**
     * Стоимость
     */
    private IntegerProperty price;

    /**
     * id позиции
     */
    private LongProperty id;

    public String getCatTrName() {
        return catTrName.get();
    }

    public StringProperty catTrNameProperty() {
        return catTrName;
    }

    public int getTime() {
        return time.get();
    }

    public IntegerProperty timeProperty() {
        return time;
    }

    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setTime(int time) {
        this.time.set(time);
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    /**
     * Конструктор для создания объекта PriceListPositionFX
     * @param catTrName Название категории транспорта
     * @param price Стоимость
     * @param time Время выполнения
     * @param id id позиции
     */
    public PriceListPositionFX(String catTrName, Integer price, Integer time, Long id){
        this.catTrName = new SimpleStringProperty(catTrName);
        this.price = new SimpleIntegerProperty(price);
        this.time = new SimpleIntegerProperty(time);
        this.id = new SimpleLongProperty(id);
    }

}
