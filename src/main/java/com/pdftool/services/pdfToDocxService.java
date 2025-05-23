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

		/**
		 * 1. Extract text from pdf 2. CREATE DOCX document 3. save to temp directory
		 */

		// 1. Extract text from pdf
		PDDocument document = PDDocument.load(pdfFile);
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(document);
		document.close();

		// 2. Create Docx document
		XWPFDocument docx = new XWPFDocument();
		XWPFParagraph paragraph = docx.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText(text);

		// 3. save to temp directory
		String tempDir = System.getProperty("java.io.tempdir");
		File docxFile = new File(tempDir, pdfFile.getName().replace(".pdf", ".docx"));
		try (FileOutputStream out = new FileOutputStream(docxFile)) {
			docx.write(out);
		}

		return docxFile.getAbsolutePath();

	}
}
