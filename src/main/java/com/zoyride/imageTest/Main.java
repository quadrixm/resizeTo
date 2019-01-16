package com.zoyride.imageTest;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    private static class PointNDimension {

        public int x;
        public int y;

        public int width;
        public int height;

        public PointNDimension() {}

        public PointNDimension(Point p, Dimension d) {
            this.width = d.width;
            this.height = d.height;
            this.x = p.x;
            this.y = p.y;
        }
        public PointNDimension(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

    }
    public static void main(String[] args) {

        int width = 300;
        int height = 200;
        int imgResizeWidth = 0;
        int imgResizeHeight = 0;

        System.out.println("Hello");

        File imageFile = new File("140x100.png");

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int imgWidth = bufferedImage.getWidth();
        int imgHeight = bufferedImage.getHeight();

        Dimension imgResize = getResizedDimension(new Dimension(imgWidth, imgHeight),
                new Dimension(300, 200));
        BufferedImage resizedImage = resizeImage(bufferedImage, imgResize.width, imgResize.height);
        File resizePathFile = new File("image_resized.png");
        try {
            ImageIO.write(resizedImage,"png", resizePathFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        BufferedImage cropImage = cropImage(bufferedImage, 100, 100, 300, 300);
        PointNDimension imgCrop = getCroppedDimension(resizedImage,
                new Dimension(resizedImage.getWidth(), resizedImage.getHeight()),
                new Dimension(300, 200));
        BufferedImage cropImage = cropImage(resizedImage, imgCrop.x, imgCrop.y, imgCrop.width, imgCrop.height);

        File cropPathFile = new File("image_crop.png");
        try {
            ImageIO.write(cropImage,"png", cropPathFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dimension getResizedDimension (Dimension existing, Dimension proposed) {
        Dimension resized = new Dimension();
        double existingRation = (double) existing.width / (double) existing.height;
        resized.width = proposed.width;
        resized.height = (int) (((double) existing.height / (double) existing.width) * resized.width);
        if (resized.height >= proposed.height) {
            return resized;
        } else {
            resized.height = proposed.height;
            resized.width = (int) (((double) existing.width / (double) existing.height) * resized.height);
            return resized;
        }
    }

    public static PointNDimension getCroppedDimension (BufferedImage bufferedImage, Dimension existing, Dimension proposed) {
        Point croppedPoint = new Point();
        Dimension cropped = proposed;
        if (existing.width == proposed.width) {
            croppedPoint.x = 0;
            croppedPoint.y = (int) ((double) (existing.height / 2) - (double) (proposed.height / 2));
        } else {
            croppedPoint.y = 0;
            croppedPoint.x = (int) ((double) (existing.width / 2) - (double) (proposed.width / 2));
        }
        return new PointNDimension(croppedPoint, cropped);
//        return cropImage(bufferedImage, croppedPoint.x, croppedPoint.y, cropped.width, cropped.height);
    }

    public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
        return croppedImage;
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }
}
