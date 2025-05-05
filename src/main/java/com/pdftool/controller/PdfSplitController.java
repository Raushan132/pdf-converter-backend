package com.pdftool.controller;
import com.pdftool.services.PdfSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pdf")
public class PdfSplitController {

 @Autowired
    private  PdfSplitService pdfSplitService;


    @PostMapping("/split-pdf")
    public ResponseEntity<byte[]> extractPdfRange(
            @RequestParam("file") MultipartFile file,
            @RequestParam("startPage") int startPage,
            @RequestParam("endPage") int endPage) {

        byte[] extractedPdf = pdfSplitService.extractPages(file, startPage, endPage);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=extracted.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(extractedPdf);
    }
}
