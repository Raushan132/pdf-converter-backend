package com.pdftool.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;




public class PageNoDTO {

    private MultipartFile file;
    private int position;
    private int startingPage;
    private int fontSize;
    private int initialPageNo;
    private int endPageNo;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStartingPage() {
        return startingPage;
    }

    public void setStartingPage(int startingPage) {
        this.startingPage = startingPage;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getInitialPageNo() {
        return initialPageNo;
    }

    public void setInitialPageNo(int initialPageNo) {
        this.initialPageNo = initialPageNo;
    }

    public int getEndPageNo() {
        return endPageNo;
    }

    public void setEndPageNo(int endPageNo) {
        this.endPageNo = endPageNo;
    }






}
