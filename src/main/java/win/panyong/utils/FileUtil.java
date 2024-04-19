package win.panyong.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Pan on 2016/1/10.
 */
public class FileUtil {

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static void readFileByLines(String filePath, Consumer<? super String> action) {
        File file = new File(filePath);
        readFileByLines(file, action);
    }

    public static void readFileByLines(File file, Consumer<? super String> action) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                action.accept(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    public static void doWriterFile(byte[] bytes, File file) throws IOException {
        if (!file.getParentFile().exists() || !file.exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            boolean newFile = file.createNewFile();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file); BufferedOutputStream buff = new BufferedOutputStream(fileOutputStream)) {
            buff.write(bytes);
            buff.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doWriterFile(String content, String filePath) throws IOException {
        File file = new File(filePath);
        doWriterFile(content.getBytes(), file);
    }

    public static void doWriterFile(String content, File file) throws IOException {
        doWriterFile(content.getBytes(), file);
    }

    public static void doWriterFile(byte[] bytes, String filePath) throws IOException {
        File file = new File(filePath);
        doWriterFile(bytes, file);
    }

    public static void doAppendFile(String content, File file) throws IOException {
        if (!file.getParentFile().exists() || !file.exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            boolean newFile = file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<File> pdfToPng(File file, String savePath) {
        List<File> result = new ArrayList<>();
        try {
            PDDocument doc = Loader.loadPDF(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 192);
                savePath = (savePath.endsWith(File.separator) ? savePath : savePath + File.separator) + (file.getName().split("\\.")[0]) + "_" + i + ".png";
                File pngFile = new File(savePath);
                if (!pngFile.getParentFile().exists() || !pngFile.exists()) {
                    boolean mkdirs = pngFile.getParentFile().mkdirs();
                    boolean newFile = pngFile.createNewFile();
                }
                ImageIO.write(image, "png", pngFile);
                result.add(pngFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        doWriterFile("a\nb\nc\nd\n".getBytes(), "/Users/pan/deleteMe.txt");
    }
}
