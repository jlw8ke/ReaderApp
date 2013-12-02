package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by jlw8k_000 on 12/1/13.
 */


public class FileFragment extends Fragment{

    public interface FileFragmentInterface {
    }

    private WebView pdfView;
    private FileFragmentInterface m_Interface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            m_Interface = (FileFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FileFragmentInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        pdfView = (WebView) rootView.findViewById(R.id.web_view);
        return rootView;
    }
}
