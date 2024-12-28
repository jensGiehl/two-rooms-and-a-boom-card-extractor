package de.agiehl.bg.card_extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class CardExtractorApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardExtractorApplication.class);

    @Autowired
    private CardExtractorService cardExtractor;

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(CardExtractorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String input = environment.getProperty("input-folder");
        String output = environment.getProperty("output-folder");

        LOGGER.info("Input folder: {}", input);
        LOGGER.info("Output folder: {}", output);

        try {
            File inputDirectory = new File(input);
            File outputDirectory = new File(output);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            File[] imageFiles = inputDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    LOGGER.info("Extracting cards from file: {}", imageFile.getName());
                    cardExtractor.extractCards(imageFile, output);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error extracting cards", e);
        }
    }
}
