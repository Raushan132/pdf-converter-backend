package com.pdftool.controller;

import com.pdftool.services.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/pdf")
@CrossOrigin
public class MergeController {


    @Autowired
    private MergeService mergeService;

    @GetMapping("/")
    public String getTested(){
        return "I am working";
    }


    @PostMapping("/merge")
    public ResponseEntity<byte[]> mergePdfs(@RequestParam("files") List<MultipartFile> files) {
        byte[] mergedPdf = mergeService.mergePdfs(files);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=merged.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(mergedPdf);
    }

}
