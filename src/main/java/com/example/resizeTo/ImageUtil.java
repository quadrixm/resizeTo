package com.example.resizeTo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

    public static byte[] resize(byte[] image, Dimension toDimen, String format, Boolean crop) throws IOException {

        InputStream in = new ByteArrayInputStream(image);

        BufferedImage cropImage = resize(ImageIO.read(in), toDimen, crop);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( cropImage, format, baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }

    public static void resize(File image, Dimension toDimen, String format, Boolean crop) throws IOException {

        InputStream in = new FileInputStream(image);

        BufferedImage cropImage = resize(ImageIO.read(in), toDimen, crop);

        ImageIO.write(cropImage, format, image);
    }

    public static BufferedImage resize(BufferedImage bufferedImage, Dimension toDimen, Boolean zoom) {

        if (bufferedImage == null || toDimen == null) return null;

        Dimension imgDimen = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());

        Dimension resizedDimen = ImageUtil.getResizedDimension(imgDimen, toDimen, zoom);

        Point cropInitialPoint = getCropInitialPoint(resizedDimen, toDimen, zoom);

        if (zoom != null && zoom) {
            BufferedImage resizedImage = ImageUtil.resizeImage(bufferedImage, resizedDimen);
            return cropImage(resizedImage, cropInitialPoint, toDimen);
        } else {
            return resizeImageWithoutCrop(bufferedImage, toDimen, cropInitialPoint, resizedDimen);
        }
    }


    private static Dimension getResizedDimension (Dimension existing, Dimension proposed, Boolean zoom) {
        if (existing == null || proposed == null) return null;

        Dimension resized = new Dimension();
        if (zoom != null && zoom) {
            resized.width = proposed.width;
            resized.height = (int) (((double) existing.height / (double) existing.width) * resized.width);
            if (resized.height >= proposed.height) {
                return resized;
            } else {
                resized.height = proposed.height;
                resized.width = (int) (((double) existing.width / (double) existing.height) * resized.height);
                return resized;
            }
        } else {
            resized.width = proposed.width;
            resized.height = (int) (((double) existing.height / (double) existing.width) * resized.width);
            if (resized.height <= proposed.height) {
                return resized;
            } else {
                resized.height = proposed.height;
                resized.width = (int) (((double) existing.width / (double) existing.height) * resized.height);
                return resized;
            }
        }
    }

    private static Point getCropInitialPoint(Dimension existing, Dimension proposed, Boolean zoom) {

        Point cropInitialPoint = new Point();
        if (zoom != null && zoom) {
            if (existing.width == proposed.width) {
                cropInitialPoint.x = 0;
                cropInitialPoint.y = (int) ((double) ((existing.height - proposed.height) / 2));
            } else {
                cropInitialPoint.y = 0;
                cropInitialPoint.x = (int) ((double) ((existing.width - proposed.width) / 2));
            }
        } else {
            if (existing.width == proposed.width) {
                cropInitialPoint.x = 0;
                cropInitialPoint.y = (int) ((double) ((proposed.height - existing.height) / 2));
            } else {
                cropInitialPoint.y = 0;
                cropInitialPoint.x = (int) ((double) ((proposed.width - existing.width) / 2));
            }
        }
        return cropInitialPoint;
    }

    private static BufferedImage cropImage(BufferedImage bufferedImage, Point cropInitialPoint, Dimension cropDimen) {
        BufferedImage croppedImage = bufferedImage.getSubimage(cropInitialPoint.x, cropInitialPoint.y,
                cropDimen.width, cropDimen.height);
        return croppedImage;
    }

    private static BufferedImage resizeImage(BufferedImage image, Dimension dimension) {
        final BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height,
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
//        graphics2D.setBackground(Color.WHITE);
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
//        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, dimension.width, dimension.height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

    private static BufferedImage resizeImageWithoutCrop(BufferedImage image, Dimension dimension, Point initialPoint, Dimension imageDimen) {
        final BufferedImage bufferedImage = new BufferedImage(dimension.width, dimension.height,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
//        graphics2D.setBackground(Color.WHITE);
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
//        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, initialPoint.x, initialPoint.y, imageDimen.width, imageDimen.height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

}

