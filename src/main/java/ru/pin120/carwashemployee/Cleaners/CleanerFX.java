package ru.pin120.carwashemployee.Cleaners;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * FX ������������� �������
 */
public class CleanerFX {
    public static final int MAX_PHONE_FILLING=10;
    public static final int MAX_SURNAME_LENGTH=50;
    public static final int MAX_NAME_LENGTH=50;
    public static final int MAX_PATRONYMIC_LENGTH=50;
    public static final String SURNAME_REGEX = "^[�-ߨ�-��-]+$";
    public static final String NAME_REGEX = "^[�-ߨ�-��-]+$";
    public static final String PATRONYMIC_REGEX = "^(|[�-ߨ�-��-]+)$";

    /**
     * id �������
     */
    private LongProperty clrId;

    /**
     * �������
     */
    private StringProperty clrSurname;
    /**
     * ���
     */
    private StringProperty clrName;
    /**
     * ��������
     */
    private StringProperty clrPatronymic;
    /**
     * ����� ��������
     */
    private StringProperty clrPhone;
    /**
     * ������
     */
    private StringProperty clrStatus;
    /**
     * �������� ����������
     */
    private StringProperty clrPhotoName;
    //private Long boxIdValue;

    public long getClrId() {
        return clrId.get();
    }

    public LongProperty clrIdProperty() {
        return clrId;
    }

    public String getClrSurname() {
        return clrSurname.get();
    }

    public StringProperty clrSurnameProperty() {
        return clrSurname;
    }

    public String getClrName() {
        return clrName.get();
    }

    public StringProperty clrNameProperty() {
        return clrName;
    }

    public String getClrPatronymic() {
        return clrPatronymic.get();
    }

    public StringProperty clrPatronymicProperty() {
        return clrPatronymic;
    }

    public String getClrPhone() {
        return clrPhone.get();
    }

    public StringProperty clrPhoneProperty() {
        return clrPhone;
    }

    public String getClrStatus() {
        return clrStatus.get();
    }

    public StringProperty clrStatusProperty() {
        return clrStatus;
    }

    public String getClrPhotoName() {
        return clrPhotoName.get();
    }

    public StringProperty clrPhotoNameProperty() {
        return clrPhotoName;
    }


    public void setClrSurname(String clrSurname) {
        this.clrSurname.set(clrSurname);
    }

    public void setClrName(String clrName) {
        this.clrName.set(clrName);
    }

    public void setClrPatronymic(String clrPatronymic) {
        this.clrPatronymic.set(clrPatronymic);
    }

    public void setClrPhone(String clrPhone) {
        this.clrPhone.set(clrPhone);
    }

    public void setClrStatus(String clrStatus) {
        this.clrStatus.set(clrStatus);
    }

    public void setClrPhotoName(String clrPhotoName) {
        this.clrPhotoName.set(clrPhotoName);
    }


    /**
     * ����������� ��� �������� ������� CleanerFX
     * @param clrId id �������
     * @param clrSurname �������
     * @param clrName ���
     * @param clrPatronymic ��������
     * @param clrPhone ����� ��������
     * @param clrPhotoName �������� ����������
     * @param clrStatus ������
     */
    public CleanerFX(Long clrId, String clrSurname, String clrName, String clrPatronymic, String clrPhone, String clrPhotoName, CleanerStatus clrStatus) {
        this.clrId = new SimpleLongProperty(clrId);
        this.clrSurname = new SimpleStringProperty(clrSurname);
        this.clrName = new SimpleStringProperty(clrName);
        this.clrPatronymic = new SimpleStringProperty(clrPatronymic);
        this.clrPhone = new SimpleStringProperty(clrPhone);
        this.clrPhotoName = new SimpleStringProperty(clrPhotoName);
        this.clrStatus = new SimpleStringProperty(clrStatus.getDisplayValue());
        //this.boxIdValue = boxId;
    }

}