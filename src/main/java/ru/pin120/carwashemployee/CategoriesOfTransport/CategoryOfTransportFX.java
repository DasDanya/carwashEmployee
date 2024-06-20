package ru.pin120.carwashemployee.CategoriesOfTransport;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

/**
 * FX представление категории транспорта
 */
@NoArgsConstructor
public class CategoryOfTransportFX {

    public static final int MAX_LENGTH_CATEGORY_NAME = 50;
    public static final String REGEX = "^[a-zA-Zа-яА-ЯёЁ0-9 -]+$";

    /**
     * id категории
     */
    private LongProperty catTrId;

    /**
     * Название категории
     */
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

    /**
     * Конструктор для создания объекта CategoryOfTransportFX
     * @param catTrName Название категории
     * @param catTrId id категории
     */
    public CategoryOfTransportFX(String catTrName, Long catTrId) {
        this.catTrName = new SimpleStringProperty(catTrName);
        this.catTrId = new SimpleLongProperty(catTrId);
    }

}
