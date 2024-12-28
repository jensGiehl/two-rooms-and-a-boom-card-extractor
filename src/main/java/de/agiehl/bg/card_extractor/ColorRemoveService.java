package de.agiehl.bg.card_extractor;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ColorRemoveService {
    private static final Color BLUE = new Color(96, 156, 211);
    private static final Color RED = new Color(238, 46, 56);
    private static final Color GRAY = new Color(135, 138, 139);
    private static final Color GREEN = new Color(24, 84, 45);
    private static final Color LIGHT_GREEN = new Color(107, 191, 88);
    private static final Color PURPLE = new Color(113, 98, 171);
    public static final int TRANSPARENT = 0x00000000;

    public BufferedImage removeColorsAndMakeTransparent(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage argbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = argbImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = argbImage.getRGB(x, y);

                if (isColorMatch(pixelColor, BLUE) || isColorMatch(pixelColor, RED) || isColorMatch(pixelColor, GRAY) || isColorMatch(pixelColor, GREEN) || isColorMatch(pixelColor, LIGHT_GREEN) || isColorMatch(pixelColor, PURPLE)) {
                    argbImage.setRGB(x, y, TRANSPARENT);
                }
            }
        }

        return argbImage;
    }

    private boolean isColorMatch(int pixelColor, Color targetColor) {
        int r = (pixelColor >> 16) & 0xFF;
        int g = (pixelColor >> 8) & 0xFF;
        int b = pixelColor & 0xFF;

        return r == targetColor.getRed() && g == targetColor.getGreen() && b == targetColor.getBlue();
    }
}
