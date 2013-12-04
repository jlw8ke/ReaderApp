package com.cauliflower.readerapp.asynctasks;

import android.os.AsyncTask;

import com.cauliflower.readerapp.HttpUtils;
import com.cauliflower.readerapp.constants.ServerConstants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 12/2/13.
 */
public class FileUploadTask extends AsyncTask<String, Void, String> {

    private ArrayList<NameValuePair> params;
    private File file;
    private FileUploadInterface m_Interface;

    public interface FileUploadInterface {
        public void fileUploaded(String filePath);
    }

    public FileUploadTask(FileUploadInterface ffi, File file, ArrayList<NameValuePair> params) {
        m_Interface = ffi;
        this.file = file;
        this.params = params;
    }

    @Override
    protected String doInBackground(String... serverList) {
        String serverUrl = serverList[0];
        String result = HttpUtils.UploadFile(serverUrl, file);
        return result;
    }

    @Override
    protected void onPostExecute(String filePath) {
        //Link file to user information
        params.add(new BasicNameValuePair(ServerConstants.FILE_LOCATION, filePath));
        HttpUtils.PostData(ServerConstants.SERVER_LINK_DEBUG, params);
        m_Interface.fileUploaded(filePath);
    }
}
