package ru.pin120.carwashemployee.Supplies;

import javafx.beans.property.*;

/**
 * FX представление расходного материала
 */
public class SupplyFX {

    public static final int MAX_LENGTH_NAME=50;
    /**
     * id расходного материала
     */
    private LongProperty supId;
    /**
     * Название
     */
    private StringProperty supName;
    /**
     * Категория
     */
    private StringProperty supCategory;
    /**
     * Количество/объём единицы
     */
    private StringProperty supMeasure;
    /**
     * Название фотографии
     */
    private StringProperty supPhotoName;
    /**
     * Общее количество
     */
    private IntegerProperty supCount;

    public long getSupId() {
        return supId.get();
    }

    public LongProperty supIdProperty() {
        return supId;
    }

    public String getSupName() {
        return supName.get();
    }

    public StringProperty supNameProperty() {
        return supName;
    }

    public String getSupCategory() {
        return supCategory.get();
    }

    public StringProperty supCategoryProperty() {
        return supCategory;
    }

    public String getSupMeasure() {
        return supMeasure.get();
    }


    public StringProperty supMeasureProperty() {
        return supMeasure;
    }

    public String getSupPhotoName() {
        return supPhotoName.get();
    }

    public StringProperty supPhotoNameProperty() {
        return supPhotoName;
    }

    public int getSupCount() {
        return supCount.get();
    }

    public IntegerProperty supCountProperty() {
        return supCount;
    }

    public void setSupCount(int supCount) {
        this.supCount.set(supCount);
    }

    /**
     * Конструктор для создания объекта SupplyFX
     * @param supId id расходного материала
     * @param supName название
     * @param supCategory категория
     * @param supMeasure количество/объём единицы
     * @param supPhotoName название фотографии
     * @param supCount общее количество
     */
    public SupplyFX(Long supId, String supName, String supCategory, String supMeasure, String supPhotoName, Integer supCount) {
        this.supId = new SimpleLongProperty(supId);
        this.supName = new SimpleStringProperty(supName);
        this.supCategory = new SimpleStringProperty(supCategory);
        this.supMeasure = new SimpleStringProperty(supMeasure);
        this.supPhotoName = new SimpleStringProperty(supPhotoName);
        this.supCount = new SimpleIntegerProperty(supCount);
    }

}



