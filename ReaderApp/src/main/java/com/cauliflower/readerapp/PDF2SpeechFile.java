package com.cauliflower.readerapp;

import java.util.Date;

/**
 * Created by jlw8k_000 on 11/12/13.
 */
public class PDF2SpeechFile {

    String author;
    String content;
    Date submitDate;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String toString(){
        return "author: " + author + " \ncontent: " + content + "\nsubmit date: " + submitDate;
    }
}
