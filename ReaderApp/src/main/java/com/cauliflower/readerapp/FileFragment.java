package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.cauliflower.readerapp.asynctasks.FileUploadTask;
import com.cauliflower.readerapp.constants.BundleConstants;
import com.cauliflower.readerapp.constants.ServerConstants;
import com.cauliflower.readerapp.objects.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 12/1/13.
 */


public class FileFragment extends Fragment implements FileUploadTask.FileUploadInterface {

    private final String GOOGLE_DOCS = "https://docs.google.com/viewer?url=";
    private File m_CurrentFile;
    private User m_CurrentUser;
    private String m_Content;

    public static final String TAG = "file_fragment";


    public interface FileInterface {
    }

    private WebView pdfView;
    private FileInterface m_Interface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            m_Interface = (FileInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FileInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        pdfView = (WebView) rootView.findViewById(R.id.web_view);
        pdfView.getSettings().setJavaScriptEnabled(true);

        updateArguments();
        setupPDFView();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateArguments(){
        m_CurrentFile = new File(getArguments().getString(BundleConstants.CURRENT_FILE_PATH));
        m_CurrentUser = new Gson().fromJson(getArguments().getString(BundleConstants.CURRENT_USER), User.class);
    }

    private void setupPDFView() {
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(ServerConstants.FILE_TITLE, m_CurrentFile.getName()));

        String user;
        if(m_CurrentUser != null)
            user = m_CurrentUser.getUsername();
        else
            user = ServerConstants.FILE_DEFAULT_OWNER;
        postParameters.add(new BasicNameValuePair(ServerConstants.FILE_OWNER, user));
        new FileUploadTask(this, m_CurrentFile, postParameters).execute(ServerConstants.SERVER_UPLOAD_DEBUG);
    }

    @Override
    public void fileUploaded(String filePath) {
        String path = GOOGLE_DOCS + filePath;
        pdfView.loadUrl(path);
        Log.d(TAG, "File path: " + path);
    }


}
