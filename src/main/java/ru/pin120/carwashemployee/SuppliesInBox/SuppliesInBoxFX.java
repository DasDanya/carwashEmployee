package ru.pin120.carwashemployee.SuppliesInBox;

import javafx.beans.property.*;

/**
 * FX представление расходных материалов в боксе
 */
public class SuppliesInBoxFX {

    private LongProperty sibId;
    private StringProperty supName;
    private StringProperty supCategory;
    private StringProperty supMeasure;
    private StringProperty supPhotoName;
    private IntegerProperty supCount;

    public long getSibId() {
        return sibId.get();
    }

    public LongProperty sibIdProperty() {
        return sibId;
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

    public SuppliesInBoxFX(Long sibId, String supName, String supCategory, String supMeasure, String supPhotoName, Integer supCount) {
        this.sibId = new SimpleLongProperty(sibId);
        this.supName = new SimpleStringProperty(supName);
        this.supCategory = new SimpleStringProperty(supCategory);
        this.supMeasure = new SimpleStringProperty(supMeasure);
        this.supPhotoName = new SimpleStringProperty(supPhotoName);
        this.supCount = new SimpleIntegerProperty(supCount);
    }
}
