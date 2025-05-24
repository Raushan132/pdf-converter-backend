package com.pdftool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pdftool.models.PDFResponseDTO;
import com.pdftool.services.DocxToPdfService;

@RestController
@RequestMapping("/convert")
public class DocxToPdfController {
	
	
	@Autowired
	private DocxToPdfService docxToPdfService;
	
	@PostMapping("/docx-to-pdf")
	public ResponseEntity<PDFResponseDTO> convertDocxToPdf(@RequestParam("file") MultipartFile file){
		try {
			
			
			//call service 
			String pdfFile = docxToPdfService.convertDocxToPdfFile(file);
			
			//prepare response dto
			PDFResponseDTO responseDTO = new PDFResponseDTO();
			responseDTO.setFileName(pdfFile);
			responseDTO.setFileType("application/pdf");
			return ResponseEntity.ok(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	

}
