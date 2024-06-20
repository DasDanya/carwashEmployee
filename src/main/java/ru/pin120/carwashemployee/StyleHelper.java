package ru.pin120.carwashemployee;

import org.apache.poi.ss.usermodel.*;

/**
 * Утилитарный класс StyleHelper предоставляет методы для создания стилей ячеек в Excel-документах.
 */
public class StyleHelper {


    /**
     * Создает стиль для ячеек с жирным текстом.
     *
     * @param workbook   Рабочая книга, для которой создается стиль.
     * @param needBorder Флаг, указывающий на необходимость добавления границы ячейки.
     * @param fontHeight Высота шрифта в пунктах.
     * @return Стиль ячейки с жирным текстом.
     */
    public static CellStyle createStyleBoldText(Workbook workbook, boolean needBorder, short fontHeight){
        // Создание стиля
        CellStyle style = workbook.createCellStyle();
        if(needBorder){
            // Создание рамки
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        Font font = workbook.createFont(); // Создание шрифта
        font.setBold(true); // Выделение текста
        font.setFontHeightInPoints(fontHeight); // Установка размера
        style.setFont(font);  // Привязка шрифта к стилю

        return style;
    }


    /**
     * Создает стиль для ячеек с жирным и курсивным текстом.
     *
     * @param workbook   Рабочая книга, для которой создается стиль.
     * @param needBorder Флаг, указывающий на необходимость добавления границы ячейки.
     * @param fontHeight Высота шрифта в пунктах.
     * @return Стиль ячейки с жирным и курсивным текстом.
     */
    public static CellStyle createStyleBoldItalicText(Workbook workbook, boolean needBorder, short fontHeight){
        CellStyle style = workbook.createCellStyle();
        if(needBorder){
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints(fontHeight);
        font.setItalic(true);
        style.setFont(font);

        return style;
    }

    /**
     * Создает стиль для ячеек с границами.
     *
     * @param workbook Рабочая книга, для которой создается стиль.
     * @return Стиль ячейки с границами.
     */
    public static CellStyle createWithBorder(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

}
