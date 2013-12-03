package com.cauliflower.readerapp.objects;

import android.net.Uri;

import java.io.File;
import java.util.Date;

/**
 * Created by jlw8k_000 on 11/12/13.
 */
public class AppFile extends File{

    private String content;

    public AppFile(String path) {
        super(path);
    }

    public AppFile(String path, String content) {
        super(path);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
