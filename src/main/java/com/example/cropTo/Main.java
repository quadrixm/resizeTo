package com.example.cropTo;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Dimension toDimen = new Dimension(300, 200);

        File imageFile = new File("100x200.png");

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File cropPathFile = new File("image_crop.png");
        try {
            BufferedImage cropImage = ImageUtil.process(bufferedImage, toDimen);
            ImageIO.write(cropImage,"png", cropPathFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
