package ru.pin120.carwashemployee.ClientsTransport;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientsTransportFX {

    public static final int MAX_STATE_NUMBER_LENGTH = 9;
    public static final String MOTO_AGR_REGEX="^\\d{4}(?<!0000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$";
    public static final String CAR_REGEX = "^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$";

    private LongProperty clTrId;
    private StringProperty clTrStateNumber;
    private StringProperty clTrMark;
    private StringProperty clTrModel;
    private StringProperty clTrCategory;

    public long getClTrId() {
        return clTrId.get();
    }

    public LongProperty clTrIdProperty() {
        return clTrId;
    }

    public String getClTrStateNumber() {
        return clTrStateNumber.get();
    }

    public StringProperty clTrStateNumberProperty() {
        return clTrStateNumber;
    }

    public String getClTrMark() {
        return clTrMark.get();
    }

    public StringProperty clTrMarkProperty() {
        return clTrMark;
    }

    public String getClTrModel() {
        return clTrModel.get();
    }

    public StringProperty clTrModelProperty() {
        return clTrModel;
    }

    public String getClTrCategory() {
        return clTrCategory.get();
    }

    public StringProperty clTrCategoryProperty() {
        return clTrCategory;
    }

    public void setClTrStateNumber(String clTrStateNumber) {
        this.clTrStateNumber.set(clTrStateNumber);
    }

    public void setClTrMark(String clTrMark) {
        this.clTrMark.set(clTrMark);
    }

    public void setClTrModel(String clTrModel) {
        this.clTrModel.set(clTrModel);
    }

    public void setClTrCategory(String clTrCategory) {
        this.clTrCategory.set(clTrCategory);
    }

    public ClientsTransportFX(Long clTrId, String clTrMark, String clTrModel, String clTrCategory, String clTrStateNumber) {
        this.clTrId = new SimpleLongProperty(clTrId);
        this.clTrStateNumber = new SimpleStringProperty(clTrStateNumber);
        this.clTrMark = new SimpleStringProperty(clTrMark);
        this.clTrModel = new SimpleStringProperty(clTrModel);
        this.clTrCategory = new SimpleStringProperty(clTrCategory);
    }
}
