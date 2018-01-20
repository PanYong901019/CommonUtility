package com.easyond.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Pan on 2018/1/10.
 */
public class ImageUtil {

    public static InputStream imgToIps(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", bos);
        byte[] imageBts = bos.toByteArray();
        return new ByteArrayInputStream(imageBts);
    }

    public static BufferedImage genVerifyCodeImg(String code) {
        BufferedImage image = new BufferedImage(100, 30, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(170, 170, 170));
        g.fillRect(0, 0, 100, 30);
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font(null, Font.BOLD, 24));
        g.drawString(code, 5, 25);
        Random r = new Random();
        for (int i = 0; i < 20; i++) {
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.drawLine(r.nextInt(100), r.nextInt(30), r.nextInt(100), r.nextInt(30));
        }
        return image;
    }

    public static InputStream getVerifyCodeImg(String code) throws IOException {
        return imgToIps(genVerifyCodeImg(code));
    }

}
