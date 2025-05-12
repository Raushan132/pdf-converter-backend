package com.pdftool.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfSplitService {

    public byte[] extractPages(MultipartFile file, int startPage, int endPage) {

        try (PDDocument originalDoc = PDDocument.load(file.getBytes());
             PDDocument newDoc = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int totalPages = originalDoc.getNumberOfPages();

            // Normalize start and end (PDFBox is 0-based internally)
            startPage = Math.max(1, startPage);
            endPage = Math.min(endPage, totalPages);

            for (int i = startPage - 1; i < endPage; i++) {
                PDPage page = originalDoc.getPage(i);
                newDoc.addPage(page);
            }

            newDoc.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract PDF pages: " + e.getMessage(), e);
        }
    }
}
