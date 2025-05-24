package com.pdftool.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocxToPdfService {

    public String convertDocxToPdfFile(MultipartFile docxFile) throws IOException {
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String fileName = UUID.randomUUID().toString() + ".pdf";
        File outputFile = new File(tempDirPath, fileName);

        try (XWPFDocument docx = new XWPFDocument(docxFile.getInputStream());
             PDDocument pdfDoc = new PDDocument()) {

            PDRectangle pageSize = PDRectangle.LETTER;
            float margin = 50;
            float yStart = pageSize.getHeight() - margin;
            float width = pageSize.getWidth() - 2 * margin;
            float leading = 14.5f;

            PDPage page = new PDPage(pageSize);
            pdfDoc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yStart);
            float yPosition = yStart;

            List<XWPFParagraph> paragraphs = docx.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                String text = para.getText();

                if (text == null || text.trim().isEmpty()) {
                    // blank line -> move cursor down
                    yPosition -= leading;
                    if (yPosition <= margin) {
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage(pageSize);
                        pdfDoc.addPage(page);
                        contentStream = new PDPageContentStream(pdfDoc, page);
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.beginText();
                        yPosition = yStart;
                        contentStream.newLineAtOffset(margin, yPosition);
                    } else {
                        contentStream.newLineAtOffset(0, -leading);
                    }
                    continue;
                }

                // Replace tabs with spaces
                text = text.replace("\t", "    ");

                // Split text by line breaks inside paragraphs (if any)
                String[] lines = text.split("\\r?\\n");
                for (String line : lines) {
                    // Word wrap handling
                    String[] words = line.split(" ");
                    StringBuilder currentLine = new StringBuilder();

                    for (String word : words) {
                        String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                        float textWidth = (PDType1Font.HELVETICA.getStringWidth(testLine) / 1000) * 12;

                        if (textWidth > width) {
                            // Write currentLine, then start new line with word
                            contentStream.showText(currentLine.toString());
                            contentStream.newLineAtOffset(0, -leading);
                            yPosition -= leading;

                            if (yPosition <= margin) {
                                contentStream.endText();
                                contentStream.close();

                                page = new PDPage(pageSize);
                                pdfDoc.addPage(page);
                                contentStream = new PDPageContentStream(pdfDoc, page);
                                contentStream.setFont(PDType1Font.HELVETICA, 12);
                                contentStream.beginText();
                                yPosition = yStart;
                                contentStream.newLineAtOffset(margin, yPosition);
                            }

                            currentLine = new StringBuilder(word);
                        } else {
                            currentLine = new StringBuilder(testLine);
                        }
                    }

                    if (currentLine.length() > 0) {
                        contentStream.showText(currentLine.toString());
                        contentStream.newLineAtOffset(0, -leading);
                        yPosition -= leading;

                        if (yPosition <= margin) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage(pageSize);
                            pdfDoc.addPage(page);
                            contentStream = new PDPageContentStream(pdfDoc, page);
                            contentStream.setFont(PDType1Font.HELVETICA, 12);
                            contentStream.beginText();
                            yPosition = yStart;
                            contentStream.newLineAtOffset(margin, yPosition);
                        }
                    }
                }
            }

            contentStream.endText();
            contentStream.close();

            pdfDoc.save(outputFile);
        }

        return fileName;
    }
}
