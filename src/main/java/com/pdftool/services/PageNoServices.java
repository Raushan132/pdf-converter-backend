package com.pdftool.services;

import com.pdftool.models.PageNoDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service

public class PageNoServices {
    public byte[] genNumber(PageNoDTO dto) {

        int fontSize= dto.getFontSize();
        int startingPage = dto.getStartingPage();
        int initialPageNo = dto.getInitialPageNo();
        int endPageNo = dto.getEndPageNo();
        MultipartFile file = dto.getFile();
        try (PDDocument doc = PDDocument.load(file.getInputStream())) {
            int startIndex = Math.max(0, initialPageNo - 1);
            int endIndex=0;
            if(endPageNo!=-1)
                endIndex = Math.min(doc.getNumberOfPages(), endPageNo);
            else endIndex = doc.getNumberOfPages();

            for (int i = startIndex; i < endIndex; i++) {
                PDPage page = doc.getPage(i);
                PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);

                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, fontSize);
                float[] dimension = getDimension(page, fontSize, dto.getPosition());
                contentStream.newLineAtOffset(dimension[0], dimension[1]);
                contentStream.showText(String.valueOf(startingPage++));
                contentStream.endText();
                contentStream.close();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to process PDF: " + e.getMessage(), e);
        }
    }


    public float[] getDimension(PDPage page,float fontSize,int position){

        var media= page.getMediaBox();

        return switch (position){
            case 1 -> {
                yield new float[]{10,media.getHeight()-(fontSize+1)};
            }
            case 2 -> {
                yield new float[]{media.getWidth()/2,media.getHeight()-(fontSize+1)};
            }
            case 3-> {
                yield new float[]{media.getWidth()-30,media.getHeight()-(fontSize+1)};
            }
            case 4 -> {
                yield new float[]{10,(fontSize+1)};
            }
            case 5 -> {
                yield new float[]{media.getWidth()/2,(fontSize+1)};
            }
            case 6-> {
                yield new float[]{media.getWidth()-30,(fontSize+1)};
            }
            default -> {yield new float[]{10,10};}

        };




    }


}
