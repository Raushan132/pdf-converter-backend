package com.pdftool.services;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class MergeService {

    public void mergePdfsStream(List<MultipartFile> files, OutputStream outputStream) {
        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationStream(outputStream);

        try {
            for (MultipartFile pdf : files) {
                merger.addSource(pdf.getInputStream());
            }
            merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly(-1));
        } catch (IOException e) {
            throw new RuntimeException("Error merging PDFs", e);
        }
    }

    public String mergePdfsToTempFile(List<MultipartFile> files) {
        // Use system-defined temp directory (respects -Djava.io.tmpdir)
        String tempDirPath = System.getProperty("java.io.tmpdir");
        String fileName = UUID.randomUUID() + ".pdf";
        File outputFile = new File(tempDirPath, fileName);

        PDFMergerUtility merger = new PDFMergerUtility();
        merger.setDestinationFileName(outputFile.getAbsolutePath());

        try {
            for (MultipartFile pdf : files) {
                merger.addSource(pdf.getInputStream());
            }

            // Use disk-based temporary caching
            merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly(-1));


            return fileName; // return file name so client can download it later
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge PDFs", e);
        }
    }

}
