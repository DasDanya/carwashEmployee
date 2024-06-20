package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO ������
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceDTO {

    /**
     * �������� ������
     */
    private String servName;
    /**
     * �������� ��������� ������
     */
    private String catName;
}
