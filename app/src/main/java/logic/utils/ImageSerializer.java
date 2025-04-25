package logic.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageSerializer {
    public static byte[] imageToByteArray(Image fxImage) {
        if (fxImage == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }

        try {
            BufferedImage bufferedImage = getBufferedImage(fxImage);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert Image to byte array: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during image serialization: " + e.getMessage(), e);
        }
    }

    private static BufferedImage getBufferedImage(Image fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PixelReader pixelReader = fxImage.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color fxColor = pixelReader.getColor(x, y);
                int argb = ((int)(fxColor.getOpacity() * 255) << 24) |
                        ((int)(fxColor.getRed() * 255) << 16) |
                        ((int)(fxColor.getGreen() * 255) << 8) |
                        ((int)(fxColor.getBlue() * 255));
                bufferedImage.setRGB(x, y, argb);
            }
        }
        return bufferedImage;
    }
}
