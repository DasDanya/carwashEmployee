package ru.pin120.carwashemployee;

import org.apache.poi.ss.usermodel.*;

public class StyleHelper {

    public static CellStyle createStyleBoldText(Workbook workbook, boolean needBorder, short fontHeight){
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
        style.setFont(font);
        return style;
    }



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
