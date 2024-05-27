package ru.pin120.carwashemployee;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class StyleHelper {

    public static CellStyle createStyleBoldText(Workbook workbook, short fontHeight){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints(fontHeight);
        style.setFont(font);

        return style;
    }

    public static CellStyle createStyleBoldItalicText(Workbook workbook, short fontHeight){
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints(fontHeight);
        font.setItalic(true);
        style.setFont(font);

        return style;
    }

}
