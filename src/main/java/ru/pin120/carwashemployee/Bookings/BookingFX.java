package ru.pin120.carwashemployee.Bookings;

import javafx.beans.property.*;

/**
 * FX представление заказа
 */
public class BookingFX {

    /**
     * id заказа
     */
    private StringProperty bkId;

    /**
     * время начала
     */
    private StringProperty startTime;

    /**
     * время окончания
     */
    private StringProperty endTime;

    /**
     * статус
     */
    private StringProperty status;

    /**
     * стоимость
     */
    private IntegerProperty price;

    /**
     * бокс
     */
    private LongProperty box;

    /**
     * мойщик, выполняющий заказ
     */
    private StringProperty cleaner;

    /**
     * Получает id заказа
     *
     * @return id заказа
     */
    public String getBkId() {
        return bkId.get();
    }

    /**
     * Свойство id заказа
     *
     * @return Свойство уникального идентификатора
     */
    public StringProperty bkIdProperty() {
        return bkId;
    }

    /**
     * Получает время начала
     *
     * @return Время начала
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * Свойство времени начала
     *
     * @return Свойство времени начала
     */
    public StringProperty startTimeProperty() {
        return startTime;
    }

    /**
     * Получает время окончания
     *
     * @return Время окончания
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * Свойство времени окончания
     *
     * @return Свойство времени окончания
     */
    public StringProperty endTimeProperty() {
        return endTime;
    }

    /**
     * Получает статус
     *
     * @return Статус
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Свойство статуса
     *
     * @return Свойство статуса
     */
    public StringProperty statusProperty() {
        return status;
    }

    /**
     * Получает стоимость
     *
     * @return Стоимость
     */
    public int getPrice() {
        return price.get();
    }

    /**
     * Свойство стоимости
     *
     * @return Свойство стоимости
     */
    public IntegerProperty priceProperty() {
        return price;
    }

    /**
     * Получает бокс, в котором выполняется заказ
     *
     * @return Бокс, в котором выполняется заказ
     */
    public long getBox() {
        return box.get();
    }

    /**
     * Свойство бокса
     *
     * @return Свойство бокса
     */
    public LongProperty boxProperty() {
        return box;
    }

    /**
     * Получает мойщика, выполняющего заказ
     *
     * @return Мойщик
     */
    public String getCleaner() {
        return cleaner.get();
    }

    /**
     * Свойство мойщика, выполняющего заказ
     *
     * @return Свойство мойщика, выполняющего заказ
     */
    public StringProperty cleanerProperty() {
        return cleaner;
    }


    /**
     * Конструктор для создания объекта BookingFX
     *
     * @param bkId      id заказа
     * @param startTime Время начала
     * @param endTime   Время окончания
     * @param status    Статус
     * @param price     Стоимость
     * @param box       Бокс
     * @param cleaner   Мойщик
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
