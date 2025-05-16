package com.pdftool.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfExpandService {
    public byte[] compressPdf(InputStream file)throws IOException {
       PDDocument document= PDDocument.load(file);
       //Remove unused object and compress
        ByteArrayOutputStream output=  new ByteArrayOutputStream();
        document.setAllSecurityToBeRemoved(true);
        document.save(output);
        document.close();
    return output.toByteArray();
    }

}
