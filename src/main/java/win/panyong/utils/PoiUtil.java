package win.panyong.utils;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
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

    public static List<String> powerPoint2Image(File file) {
        String fileType = file.getName().substring(file.getName().lastIndexOf("."));
        return fileType.equals(".ppt") ? doPPT2Image(file) : doPPTX2Image(file);
    }

    private static List<String> doPPTX2Image(File file) {
        List<String> imageList = new ArrayList<>();
        try (FileInputStream is = new FileInputStream(file); XMLSlideShow ppt = new XMLSlideShow(is)) {
            Dimension pgsize = ppt.getPageSize();// 获取幻灯片画板大小
            List<XSLFSlide> slides = ppt.getSlides();// 获取幻灯片
            for (int i = 0; i < slides.size(); i++) {
                // 解决乱码问题
                List<XSLFShape> shapes = slides.get(i).getShapes();
                for (XSLFShape shape : shapes) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape sh = (XSLFTextShape) shape;
                        List<XSLFTextParagraph> textParagraphs = sh.getTextParagraphs();
                        for (XSLFTextParagraph xslfTextParagraph : textParagraphs) {
                            List<XSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                            for (XSLFTextRun xslfTextRun : textRuns) {
                                xslfTextRun.setFontFamily("微软雅黑");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width * 2, pgsize.height * 2, BufferedImage.TYPE_INT_RGB);//根据幻灯片大小生成图片
                Graphics2D graphics = img.createGraphics();//获取画板
                graphics.setPaint(Color.white);
                graphics.scale(2, 2);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width * 2, pgsize.height * 2));
                slides.get(i).draw(graphics);//把幻灯片画到画板里
                String imagePath = file.getName().substring(0, file.getName().lastIndexOf(".")) + File.separator + "page-" + i + ".jpg";
                File imageFile = new File(file.getParent() + File.separator + imagePath);
                if (!imageFile.getParentFile().exists()) {
                    imageFile.getParentFile().mkdirs();
                }
                try (FileOutputStream out = new FileOutputStream(imageFile)) {
                    ImageIO.write(img, "jpg", out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageList.add(imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageList;
    }

    private static List<String> doPPT2Image(File file) {
        List<String> imageList = new ArrayList<>();
        try (FileInputStream is = new FileInputStream(file); HSLFSlideShow ppt = new HSLFSlideShow(is)) {
            Dimension pgsize = ppt.getPageSize();// 获取幻灯片画板大小
            List<HSLFSlide> slides = ppt.getSlides();// 获取幻灯片
            for (int i = 0; i < slides.size(); i++) {
                // 解决乱码问题
                List<HSLFShape> shapes = slides.get(i).getShapes();
                for (HSLFShape shape : shapes) {
                    if (shape instanceof HSLFTextShape) {
                        HSLFTextShape sh = (HSLFTextShape) shape;
                        List<HSLFTextParagraph> textParagraphs = sh.getTextParagraphs();
                        for (HSLFTextParagraph xslfTextParagraph : textParagraphs) {
                            List<HSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                            for (HSLFTextRun xslfTextRun : textRuns) {
                                xslfTextRun.setFontFamily("微软雅黑");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width * 2, pgsize.height * 2, BufferedImage.TYPE_INT_RGB);//根据幻灯片大小生成图片
                Graphics2D graphics = img.createGraphics();//获取画板
                graphics.setPaint(Color.white);
                graphics.scale(2, 2);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width * 2, pgsize.height * 2));
                slides.get(i).draw(graphics);//把幻灯片画到画板里
                String imagePath = file.getName().substring(0, file.getName().lastIndexOf(".")) + File.separator + "page-" + i + ".jpg";
                File imageFile = new File(file.getParent() + File.separator + imagePath);
                if (!imageFile.getParentFile().exists()) {
                    imageFile.getParentFile().mkdirs();
                }
                try (FileOutputStream out = new FileOutputStream(imageFile)) {
                    ImageIO.write(img, "jpg", out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageList.add(imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageList;
    }

}
