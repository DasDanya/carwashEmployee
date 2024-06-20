package ru.pin120.carwashemployee.PriceListPosition;

/**
 * Интерфейс для обработки изменения состояния флажка в списке прайс-листа услуг
 * Этот интерфейс должен быть реализован классами, которые хотят получать уведомления об
 * изменении состояния флажков в прайс-листе услуг.
 */
public interface ServiceInPriceListSelectable {

    /**
     * Вызывается при изменении состояния флажка для услуги в прайс-листе.
     *
     * @param service объект типа {@link ServiceWithPriceListFX}, представляющий услугу со стоимостью и временем выполнения
     * @param isNowSelected текущее состояние флажка: {@code true}, если флажок сейчас установлен; {@code false}, если флажок снят.
     */
    void onCheckBoxChanged(ServiceWithPriceListFX service, boolean isNowSelected);
}
