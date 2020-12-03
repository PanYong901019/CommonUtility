package win.panyong.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PoiUtil {

    public static ByteArrayOutputStream doWriterExcel(LinkedHashMap<String, LinkedList<List>> map, List<Integer> unLockColumns, String fileType) throws IOException {
        Workbook wb;
        if (".xls".equals(fileType)) {
            wb = new HSSFWorkbook();
        } else if (".xlsx".equals(fileType)) {
            wb = new XSSFWorkbook();
        } else {
            System.out.println("您的文档格式不正确！");
            return null;
        }
        for (String key : map.keySet()) {
            Sheet sheet = wb.createSheet(key);
            LinkedList<List> lists = map.get(key);
            for (int i = 0; i < lists.size(); i++) {
                Row row = sheet.createRow(i);
                List list = lists.get(i);
                for (int j = 0; j < list.size(); j++) {
                    Cell cell = row.createCell(j);
                    try {
                        cell.setCellValue(list.get(j).toString());
                    } catch (NullPointerException e) {
                        cell.setCellValue("");
                    }
                    if (unLockColumns.contains(j)) {
                        CellStyle cellStyle = wb.createCellStyle();
                        cellStyle.setLocked(false);
                        cell.setCellStyle(cellStyle);
                    }
                }
            }
            sheet.setColumnWidth(0, 0 * 256);
            sheet.protectSheet("");
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        wb.write(stream);
        stream.close();
        return stream;
    }

    public static ByteArrayOutputStream doWriterExcel(LinkedHashMap<String, LinkedList<List>> map) throws IOException {
        Workbook wb = new XSSFWorkbook();
        for (String key : map.keySet()) {
            Sheet sheet = wb.createSheet(key);
            LinkedList<List> lists = map.get(key);
            for (int i = 0; i < lists.size(); i++) {
                Row row = sheet.createRow(i);
                List list = lists.get(i);
                for (int j = 0; j < list.size(); j++) {
                    Cell cell = row.createCell(j);
                    try {
                        cell.setCellValue(list.get(j).toString());
                    } catch (NullPointerException e) {
                        cell.setCellValue("");
                    }
                }
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        wb.write(stream);
        stream.close();
        return stream;
    }

    public static ByteArrayOutputStream doWriterExcel(Map<String, List<List<String>>> map) throws IOException {
        Workbook wb = new XSSFWorkbook();
        for (String key : map.keySet()) {
            Sheet sheet = wb.createSheet(key);
            List<List<String>> lists = map.get(key);
            for (int i = 0; i < lists.size(); i++) {
                Row row = sheet.createRow(i);
                List list = lists.get(i);
                for (int j = 0; j < list.size(); j++) {
                    Cell cell = row.createCell(j);
                    try {
                        cell.setCellValue(list.get(j).toString());
                    } catch (NullPointerException e) {
                        cell.setCellValue("");
                    }
                }
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        wb.write(stream);
        stream.close();
        return stream;
    }

    public static LinkedHashMap<String, LinkedList<List>> doReadExcel(File file, String fileType) throws Exception {
        FileInputStream excel = new FileInputStream(file);
        LinkedHashMap<String, LinkedList<List>> map = new LinkedHashMap<>();
        Workbook sheets = null;
        if (".xlsx".equals(fileType)) {
            sheets = new XSSFWorkbook(excel);
        } else if (".xls".equals(fileType)) {
            sheets = new HSSFWorkbook(excel);
        }
        for (int numSheet = 0; numSheet < sheets.getNumberOfSheets(); numSheet++) {
            LinkedList<List> lists = new LinkedList();
            Sheet sheet = sheets.getSheetAt(numSheet);
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                List<String> list = new LinkedList<String>();
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    for (int i = 0; i <= row.getLastCellNum(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            list.add(cell.getStringCellValue());
                        } else {
                            list.add(null);
                        }
                    }
                }
                lists.add(list);
            }
            map.put(sheet.getSheetName(), lists);
        }
        return map;
    }

    public static Date getExcelDateString(String date) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        try {
            d = df1.parse("1900-01-01");
        } catch (ParseException e) {
            return d;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, new Integer(date));
        return calendar.getTime();
    }
}
