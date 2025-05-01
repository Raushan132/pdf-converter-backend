package com.pdftool.controller;

import com.pdftool.models.PageNoDTO;
import com.pdftool.services.PageNoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pdf")
public class PageNoController {

    @Autowired
    private PageNoServices pageNoServices;

    @PostMapping(value = "/addPageNo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> PdfNoGen(@ModelAttribute PageNoDTO pageNoDTO) {


        byte[] pdfWithPageNo = pageNoServices.genNumber(pageNoDTO);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pageNoDTO.getFile().getOriginalFilename())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfWithPageNo);
    }
}
