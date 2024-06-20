package ru.pin120.carwashemployee.CategoriesOfTransport;

import lombok.*;

/**
 * ������ ��������� ����������
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryOfTransport {

    /**
     * id ���������
     */
    private Long catTrId;
    /**
     * �������� ���������
     */
    private String catTrName;
}
