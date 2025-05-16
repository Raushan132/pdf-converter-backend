package com.pdftool.controller;

import com.pdftool.models.PDFResponseDTO;
import com.pdftool.services.ImageToPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/pdf")
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


    @PostMapping("/image-to-pdf-file")
    public ResponseEntity<PDFResponseDTO> convertImagetoPdfFile(@RequestParam("files") MultipartFile[] files){

        try {
          String data=  imageToPdfService.convertImagesToPdfFile(files);
          PDFResponseDTO response=new PDFResponseDTO();
          response.setFileName(data);
          return ResponseEntity.ok()
                  .body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
