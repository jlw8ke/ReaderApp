package com.cauliflower.readerapp.asynctasks;

import com.cauliflower.readerapp.objects.AppFile;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 11/12/13.
 */
public interface SearchTaskInterface {
    public void onSearchReceived(ArrayList<AppFile> files);
}
