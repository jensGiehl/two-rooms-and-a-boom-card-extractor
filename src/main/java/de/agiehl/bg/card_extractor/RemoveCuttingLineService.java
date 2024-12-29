package de.agiehl.bg.card_extractor;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class RemoveCuttingLineService {

    public static final int TRANSPARENT = 0x00000000;

    public BufferedImage makeSingleColorColumnsTransparent(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage argbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = argbImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int x = 0; x < width; x++) {
            boolean isColumnSingleColor = true;
            int firstPixelColor = argbImage.getRGB(x, 0);

            for (int y = 1; y < height; y++) {
                int pixelColor = argbImage.getRGB(x, y);
                if (pixelColor != firstPixelColor) {
                    isColumnSingleColor = false;
                    break;
                }
            }

            if (isColumnSingleColor && !isTransparent(firstPixelColor)) {
                for (int y = 0; y < height; y++) {
                    argbImage.setRGB(x, y, TRANSPARENT);
                }
            }
        }

        return argbImage;
    }

    private boolean isTransparent(int pixelColor) {
        return (pixelColor >> 24) == 0x00;
    }

}