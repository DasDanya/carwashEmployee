package ru.pin120.carwashemployee.Bookings;

import javafx.beans.property.*;

/**
 * FX ������������� ������
 */
public class BookingFX {

    /**
     * id ������
     */
    private StringProperty bkId;

    /**
     * ����� ������
     */
    private StringProperty startTime;

    /**
     * ����� ���������
     */
    private StringProperty endTime;

    /**
     * ������
     */
    private StringProperty status;

    /**
     * ���������
     */
    private IntegerProperty price;

    /**
     * ����
     */
    private LongProperty box;

    /**
     * ������, ����������� �����
     */
    private StringProperty cleaner;

    /**
     * �������� id ������
     *
     * @return id ������
     */
    public String getBkId() {
        return bkId.get();
    }

    /**
     * �������� id ������
     *
     * @return �������� ����������� ��������������
     */
    public StringProperty bkIdProperty() {
        return bkId;
    }

    /**
     * �������� ����� ������
     *
     * @return ����� ������
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * �������� ������� ������
     *
     * @return �������� ������� ������
     */
    public StringProperty startTimeProperty() {
        return startTime;
    }

    /**
     * �������� ����� ���������
     *
     * @return ����� ���������
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * �������� ������� ���������
     *
     * @return �������� ������� ���������
     */
    public StringProperty endTimeProperty() {
        return endTime;
    }

    /**
     * �������� ������
     *
     * @return ������
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * �������� �������
     *
     * @return �������� �������
     */
    public StringProperty statusProperty() {
        return status;
    }

    /**
     * �������� ���������
     *
     * @return ���������
     */
    public int getPrice() {
        return price.get();
    }

    /**
     * �������� ���������
     *
     * @return �������� ���������
     */
    public IntegerProperty priceProperty() {
        return price;
    }

    /**
     * �������� ����, � ������� ����������� �����
     *
     * @return ����, � ������� ����������� �����
     */
    public long getBox() {
        return box.get();
    }

    /**
     * �������� �����
     *
     * @return �������� �����
     */
    public LongProperty boxProperty() {
        return box;
    }

    /**
     * �������� �������, ������������ �����
     *
     * @return ������
     */
    public String getCleaner() {
        return cleaner.get();
    }

    /**
     * �������� �������, ������������ �����
     *
     * @return �������� �������, ������������ �����
     */
    public StringProperty cleanerProperty() {
        return cleaner;
    }


    /**
     * ����������� ��� �������� ������� BookingFX
     *
     * @param bkId      id ������
     * @param startTime ����� ������
     * @param endTime   ����� ���������
     * @param status    ������
     * @param price     ���������
     * @param box       ����
     * @param cleaner   ������
     */
    public BookingFX(String bkId, String startTime, String endTime, String status, Integer price, Long box, String cleaner) {
        this.bkId = new SimpleStringProperty(bkId);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.status = new SimpleStringProperty(status);
        this.price = new SimpleIntegerProperty(price);
        this.box = new SimpleLongProperty(box);
        this.cleaner = new SimpleStringProperty(cleaner);
    }

}
