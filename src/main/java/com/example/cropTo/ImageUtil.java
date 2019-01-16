package com.example.cropTo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

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

    public static byte[] process(byte[] image, Dimension toDimen, String format) throws IOException {

        InputStream in = new ByteArrayInputStream(image);

        BufferedImage cropImage = process(ImageIO.read(in), toDimen);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( cropImage, format, baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }

    public static BufferedImage process(BufferedImage bufferedImage, Dimension toDimen) throws IOException {

        Dimension imgDimen = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());

        Dimension imgResizedDimen = ImageUtil.getResizedDimension(imgDimen, toDimen);

        BufferedImage resizedImage = ImageUtil.resizeImage(bufferedImage, imgResizedDimen);

        PointNDimension imgCroppedDimen = getCroppedDimension(imgResizedDimen, toDimen);

        return cropImage(resizedImage, imgCroppedDimen);
    }


    private static Dimension getResizedDimension (Dimension existing, Dimension proposed) {
        Dimension resized = new Dimension();
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

    private static PointNDimension getCroppedDimension (Dimension existing, Dimension proposed) {
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
    }

    private static BufferedImage cropImage(BufferedImage bufferedImage, PointNDimension nDimension) {
        BufferedImage croppedImage = bufferedImage.getSubimage(nDimension.x, nDimension.y,
                nDimension.width, nDimension.height);
        return croppedImage;
    }

    private static BufferedImage resizeImage(BufferedImage image, Dimension dimension) {
        final BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height,
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, dimension.width, dimension.height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

}
