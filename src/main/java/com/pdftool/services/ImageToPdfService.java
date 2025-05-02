package com.pdftool.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
@Service
public class ImageToPdfService {

    /**
     * Converts a list of image byte arrays to a single PDF file and returns it as a byte array.
     *
     * @param imageBytesList List of image data in byte[] format (e.g. JPEG, PNG)
     * @return PDF file as a byte array
     * @throws IOException If there's an error during PDF creation
     */
    public byte[] convertImagesToPdf(List<byte[]> imageBytesList) throws IOException {
        // Create a new PDF document in memory
        try (PDDocument document = new PDDocument()) {

            // Loop through each image in the list
            for (byte[] imageBytes : imageBytesList) {

                // Create a new page (A4 size) for each image
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                // Convert byte[] to PDImageXObject for PDFBox to handle image
                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "image");

                // Open a content stream to draw the image on the page
                try (PDPageContentStream contents = new PDPageContentStream(document, page)) {

                    // Calculate scale factor to fit the image within A4 size
                    float scale = Math.min(
                            PDRectangle.A4.getWidth() / pdImage.getWidth(),
                            PDRectangle.A4.getHeight() / pdImage.getHeight()
                    );

                    // Calculate scaled dimensions
                    float imageWidth = pdImage.getWidth() * scale;
                    float imageHeight = pdImage.getHeight() * scale;

                    // Center the image on the page
                    float x = (PDRectangle.A4.getWidth() - imageWidth) / 2;
                    float y = (PDRectangle.A4.getHeight() - imageHeight) / 2;

                    // Draw the image with calculated dimensions and position
                    contents.drawImage(pdImage, x, y, imageWidth, imageHeight);
                }
            }

            // Write the PDF document to a byte array output stream
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                document.save(baos);  // Save the PDF into the stream
                return baos.toByteArray();  // Return the byte array of the PDF
            }
        }
    }
}
