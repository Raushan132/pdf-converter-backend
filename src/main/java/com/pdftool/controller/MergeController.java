package com.pdftool.controller;

import com.pdftool.models.PDFResponseDTO;
import com.pdftool.services.MergeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/pdf")
@CrossOrigin

public class MergeController {


    @Autowired
    private MergeService mergeService;

    @GetMapping("/")
    public String getTested(){
        return "I am working with new version 0.0.1";
    }


    @PostMapping("/merge-download")
    public void mergePdfs(
            @RequestParam("files") List<MultipartFile> files,
            HttpServletResponse response
    ) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=merged.pdf");

        mergeService.mergePdfsStream(files, response.getOutputStream());
    }

    @PostMapping("/merge")
    public ResponseEntity<PDFResponseDTO> mergePdfsToTempFileController(@RequestParam("files") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(new PDFResponseDTO("No files were provided for merging."));
        }

        try {
            // Merging the files
            String fileName = mergeService.mergePdfsToTempFile(files);

            PDFResponseDTO data= new PDFResponseDTO(fileName);
           // Returning the merged file info with download URL
            return ResponseEntity.ok(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new PDFResponseDTO("Error occurred during merging: " + e.getMessage()));
        }
    }

}
