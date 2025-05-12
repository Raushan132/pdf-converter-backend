package com.pdftool.models;

public class PDFResponseDTO {

    private String fileName;
    private String fileType;

    public PDFResponseDTO(){
        super();
    }

    public PDFResponseDTO(String fileName){
        super();
        this.fileName= fileName;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
