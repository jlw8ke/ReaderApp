package com.cauliflower.readerapp.asynctasks;

import com.cauliflower.readerapp.PDF2SpeechFile;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 11/12/13.
 */
public interface SearchTaskInterface {
    public void onSearchReceived(ArrayList<PDF2SpeechFile> files);
}
