package com.pdftool.services;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class MergeService {

    public byte[] mergePdfs(List<MultipartFile> files) {
        PDFMergerUtility merger = new PDFMergerUtility();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            merger.setDestinationStream(outputStream);

            for (MultipartFile pdf : files) {
                merger.addSource(pdf.getInputStream());
            }

            // Use TempFileOnly setting because you want to handle large files (up to 2GB)
            merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly(-1));
            System.out.println("Documents merged successfully");
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error merging PDFs", e);
        }
    }
}
