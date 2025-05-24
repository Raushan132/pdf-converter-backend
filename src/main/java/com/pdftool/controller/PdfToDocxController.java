package com.pdftool.controller;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pdftool.models.PDFResponseDTO;
import com.pdftool.services.pdfToDocxService;

@RestController
@RequestMapping("/docx")
public class PdfToDocxController {

	
	@Autowired
	private pdfToDocxService pdfToDocxService;
	
	
	@PostMapping("/convert")
    public ResponseEntity<PDFResponseDTO> convertPdfToDocx(@RequestParam("file") MultipartFile file) {
        try {
        	 // Step 1: Save uploaded file using UUID and temp dir
            String tempDirPath = System.getProperty("java.io.tmpdir");
            String fileName = UUID.randomUUID() + ".pdf";
            File tempPdfFile = new File(tempDirPath, fileName);
            file.transferTo(tempPdfFile);

            // Step 2: Convert PDF to DOCX using the service
            String docxFilePath = pdfToDocxService.convertPdfToDocx(tempPdfFile);

            // Step 3: Create response DTO
            File docxFile = new File(docxFilePath);
            PDFResponseDTO responseDTO = new PDFResponseDTO();
            responseDTO.setFileName(docxFile.getName());
            responseDTO.setFileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}