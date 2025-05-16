package com.pdftool.controller;

import com.pdftool.services.PdfExpandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
@CrossOrigin
public class PdfCompressionController {



    @Autowired
    private PdfExpandService pdfExpandService;
    @PostMapping("/expand")
    public ResponseEntity<byte[]> compressPdf(@RequestParam("file")MultipartFile file){

       try {
        byte[] compressPdf=   pdfExpandService.compressPdf(file.getInputStream());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=compressed.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(compressPdf);
       }catch (IOException e){
           return ResponseEntity.status(500).body(null);
       }


    }

}
