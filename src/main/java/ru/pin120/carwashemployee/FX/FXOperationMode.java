package ru.pin120.carwashemployee.FX;

/**
 * Перечисление FXOperationMode определяет режимы операций.
 * Включает операции создания, удаления, изменения, показа и прочей операции.
 */
public enum FXOperationMode {
    /**
     * добавление
     */
    CREATE,
    /**
     * удаление
     */
    DELETE,
    /**
     * изменение
     */
    EDIT,
    /**
     * показ
     */
    SHOW,
    /**
     * прочее
     */
    OTHER
}
