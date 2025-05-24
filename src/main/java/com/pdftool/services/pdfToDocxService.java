package com.pdftool.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

@Service
public class pdfToDocxService {

	@SuppressWarnings("resource")
	public String convertPdfToDocx(File pdfFile) throws IOException {


	    // Step 1: Extract text from PDF
	    PDDocument document = PDDocument.load(pdfFile);
	    PDFTextStripper pdfStripper = new PDFTextStripper();
	    String text = pdfStripper.getText(document);
	    document.close();

	    // Step 2: Create DOCX using Apache POI
	    XWPFDocument docx = new XWPFDocument();

	    // Step 3: Split text by lines to preserve paragraph structure
	    String[] lines = text.split("\r\n|\r|\n");
	    for (String line : lines) {
	        XWPFParagraph paragraph = docx.createParagraph();
	        XWPFRun run = paragraph.createRun();
	        run.setFontFamily("Times New Roman");
	        run.setFontSize(12);
	        run.setText(line);
	    }

	    // Step 4: Save DOCX to temp directory
	    String tempDir = System.getProperty("java.io.tmpdir");
	    String docxFileName = pdfFile.getName().replace(".pdf", "") + "_" + System.currentTimeMillis() + ".docx";
	    File docxFile = new File(tempDir, docxFileName);

	    try (FileOutputStream out = new FileOutputStream(docxFile)) {
	        docx.write(out);
	    }

	    return docxFile.getAbsolutePath();
	}
}