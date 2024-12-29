package de.agiehl.bg.card_extractor;

import org.apache.commons.imaging.Imaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.apache.commons.imaging.ImageFormats.PNG;

@Service
public class CardExtractorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardExtractorService.class);

    private static final int OFFSET_X = 99;
    private static final int OFFSET_Y = 100;
    private static final int GAP_X_BETWEEN_CARDS = 175;
    private static final int GAP_Y_BETWEEN_CARDS = 214;
    private static final int CARD_WIDTH = 321;
    private static final int CARD_HEIGHT = 553;
    private static final int CARDS_IN_ROW = 4;
    private static final int CARDS_IN_COLUMN = 2;

    @Autowired
    private ColorRemoveService colorRemoveService;

    @Autowired
    private RemoveCuttingLineService removeCuttingLineService;

    public void extractCards(File imageFile, String outputDirectory) throws IOException {

        BufferedImage image = Imaging.getBufferedImage(imageFile);

        for (int row = 0; row < CARDS_IN_COLUMN; row++) {
            for (int col = 0; col < CARDS_IN_ROW; col++) {
                int x = OFFSET_X + col * (CARD_WIDTH + GAP_X_BETWEEN_CARDS);
                int y = OFFSET_Y + row * (CARD_HEIGHT + GAP_Y_BETWEEN_CARDS);

                BufferedImage cardImage = image.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
                cardImage = colorRemoveService.removeColorsAndMakeTransparent(cardImage);
                cardImage = removeCuttingLineService.makeSingleColorColumnsTransparent(cardImage);

                String filename = imageFile.getName() + "card_" + row + "_" + col + ".png";
                LOGGER.info("Writing card to file: {}", filename);
                File outputFile = new File(outputDirectory, filename);
                Imaging.writeImage(cardImage, outputFile, PNG);
            }
        }

    }

}
