package com.pdftool.services;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageToPdfService {

    public byte[] convertImagesToPdf(MultipartFile[] files) throws IOException {

        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            for (MultipartFile file : files) {
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                PDImageXObject img = PDImageXObject.createFromByteArray(doc, file.getBytes(), file.getOriginalFilename());

                float scale = Math.min(
                        PDRectangle.A4.getWidth() / img.getWidth(),
                        PDRectangle.A4.getHeight() / img.getHeight()
                );

                float width = img.getWidth() * scale;
                float height = img.getHeight() * scale;
                float x = (PDRectangle.A4.getWidth() - width) / 2;
                float y = (PDRectangle.A4.getHeight() - height) / 2;

                try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.drawImage(img, x, y, width, height);
                }
            }
            doc.save(baos);
            return baos.toByteArray();
        }
    }



    public String convertImagesToPdfFile(MultipartFile[] files) throws IOException {
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String fileName = UUID.randomUUID() + ".pdf";
        File outputFile = new File(tempDirPath, fileName);

        try (PDDocument doc = new PDDocument()) {

            for (MultipartFile file : files) {
                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                PDImageXObject img = PDImageXObject.createFromByteArray(doc, file.getBytes(), file.getOriginalFilename());

                float scale = Math.min(
                        PDRectangle.A4.getWidth() / img.getWidth(),
                        PDRectangle.A4.getHeight() / img.getHeight()
                );

                float width = img.getWidth() * scale;
                float height = img.getHeight() * scale;
                float x = (PDRectangle.A4.getWidth() - width) / 2;
                float y = (PDRectangle.A4.getHeight() - height) / 2;

                try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.drawImage(img, x, y, width, height);
                }
            }

            // Save the document to the temporary file
            doc.save(outputFile);
            return fileName; // Return the filename so client can download it later
        }
    }
}
