package ru.pin120.carwashemployee.PriceListPosition;

/**
 * ��������� ��� ��������� ��������� ��������� ������ � ������ �����-����� �����
 * ���� ��������� ������ ���� ���������� ��������, ������� ����� �������� ����������� ��
 * ��������� ��������� ������� � �����-����� �����.
 */
public interface ServiceInPriceListSelectable {

    /**
     * ���������� ��� ��������� ��������� ������ ��� ������ � �����-�����.
     *
     * @param service ������ ���� {@link ServiceWithPriceListFX}, �������������� ������ �� ���������� � �������� ����������
     * @param isNowSelected ������� ��������� ������: {@code true}, ���� ������ ������ ����������; {@code false}, ���� ������ ����.
     */
    void onCheckBoxChanged(ServiceWithPriceListFX service, boolean isNowSelected);
}
