package com.cauliflower.readerapp.dropbox;

import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by jlw8k_000 on 12/4/13.
 */
public class DropboxTask extends AsyncTask<Void, Void, Void> {

    public interface DropboxTaskInterface {
        public void loadDropboxContent(File localFile);
    }
    private File localFile;
    private String dropboxFile;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private DropboxTaskInterface m_Interface;

    public DropboxTask(DropboxTaskInterface i, File localFile, String dropboxFile, DropboxAPI<AndroidAuthSession> mDBApi) {
        this.localFile = localFile;
        this.dropboxFile = dropboxFile;
        this.mDBApi = mDBApi;
        m_Interface = i;
    }



    @Override
    protected Void doInBackground(Void... voids) {
        try {
            FileOutputStream stream = new FileOutputStream(localFile);
            DropboxAPI.DropboxFileInfo info = mDBApi.getFile(dropboxFile, null, stream, null);
            Log.i("DbExampleLog", "The file's rev is: " + info.getMetadata().rev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        m_Interface.loadDropboxContent(localFile);
    }
}
