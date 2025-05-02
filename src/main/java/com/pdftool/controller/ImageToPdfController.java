package com.pdftool.controller;

import com.pdftool.services.ImageToPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class ImageToPdfController {
     @Autowired
    private  ImageToPdfService imageToPdfService;

    public ImageToPdfController(ImageToPdfService imageToPdfService) {
        this.imageToPdfService = imageToPdfService;
    }

    @PostMapping("/image-to-pdf")
    public ResponseEntity<byte[]> convertImagesToPdf(@RequestParam("files") MultipartFile[] files) {
        try {
            List<byte[]> imageBytesList = new ArrayList<>();
            for (MultipartFile file : files) {
                imageBytesList.add(file.getBytes());
            }

            byte[] pdfBytes = imageToPdfService.convertImagesToPdf(imageBytesList);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
