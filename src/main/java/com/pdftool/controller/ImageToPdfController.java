package com.pdftool.controller;

import com.pdftool.services.ImageToPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class ImageToPdfController {

    @Autowired
    private ImageToPdfService imageToPdfService;

    @PostMapping("/image-to-pdf")
    public ResponseEntity<byte[]> convertImagesToPdf(@RequestParam("files") MultipartFile[] files) {
        try {
            byte[] pdf = imageToPdfService.convertImagesToPdf(files);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
