package ru.pin120.carwashemployee.Bookings;

import javafx.beans.property.*;

public class BookingFX {

    private StringProperty bkId;
    private StringProperty startTime;
    private StringProperty endTime;
    private StringProperty status;
    private IntegerProperty price;
    private LongProperty box;
    private StringProperty cleaner;

    public String getBkId() {
        return bkId.get();
    }

    public StringProperty bkIdProperty() {
        return bkId;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public long getBox() {
        return box.get();
    }

    public LongProperty boxProperty() {
        return box;
    }

    public String getCleaner() {
        return cleaner.get();
    }

    public StringProperty cleanerProperty() {
        return cleaner;
    }

    public BookingFX(String bkId, String startTime, String endTime, String status, Integer price, Long box, String cleaner) {
        this.bkId = new SimpleStringProperty(bkId);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
        this.price =  new SimpleIntegerProperty(price);
        this.box = new SimpleLongProperty(box);
        this.cleaner = new SimpleStringProperty(cleaner);
    }
}
