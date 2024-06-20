package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSupplies;

import java.util.List;

/**
 * Модель услуги
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    /**
     * Название услуги
     */
    private String servName;
    /**
     * Список категорий расходных материалов, необходимых для выполнения услуги
     */
    List<CategoryOfSupplies> categoriesOfSupplies;

    /**
     * Конструктор для создания объекта Service
     * @param servName Название услуги
     */
    public Service(String servName) {
        this.servName = servName;
    }
}
