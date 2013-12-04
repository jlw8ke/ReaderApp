package com.cauliflower.readerapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.cauliflower.readerapp.constants.BundleConstants;
import com.cauliflower.readerapp.dropbox.DropboxTask;
import com.cauliflower.readerapp.dropbox.DropboxUtils;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

/**
 * Created by jlw8k_000 on 12/4/13.
 */
public class DropboxFileFragment extends Fragment implements TextToSpeech.OnInitListener, DropboxTask.DropboxTaskInterface {

    @Override
    public void loadDropboxContent(File localFile) {
        m_Content = FileParser.parse(localFile.getAbsolutePath());
    }

    public interface DropboxFileInterface {
        public DropboxAPI<AndroidAuthSession> getApiSession();
    }

    final static private String APP_KEY = "s3voc9raoqvgdj6";
    final static private String APP_SECRET = "ez81gny641pdtjq";
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;


    private final String GOOGLE_DOCS = "https://docs.google.com/viewer?url=";
    private DropboxFileInterface m_Interface;
    private String m_FilePath;
    private String m_FileName;
    private String m_Content;

    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton fastForwardButton;
    private ImageButton rewindButton;

    private TextToSpeech speechManager;

    public static final String TAG = "dropbox_file_fragment";
    private String TEMP_FILENAME = "dropbox-temp.pdf";


    private WebView pdfView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            m_Interface = (DropboxFileInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement DropboxFileInterface");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speechManager = new TextToSpeech(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);
        m_FilePath = getArguments().getString(BundleConstants.CURRENT_FILE_PATH);
        m_FileName = getArguments().getString(BundleConstants.DROPBOX);

        pdfView = (WebView) rootView.findViewById(R.id.web_view);
        playButton = (ImageButton) rootView.findViewById(R.id.play_btn);
        pauseButton = (ImageButton) rootView.findViewById(R.id.pause_btn);
        fastForwardButton = (ImageButton) rootView.findViewById(R.id.fast_forward_btn);
        rewindButton = (ImageButton) rootView.findViewById(R.id.rewind_btn);

        fastForwardButton.setVisibility(View.GONE);
        rewindButton.setVisibility(View.GONE);

        pdfView.getSettings().setJavaScriptEnabled(true);

        String path = GOOGLE_DOCS + m_FilePath;
        pdfView.loadUrl(path);
        Log.d(TAG, "File path: " + path);


        downloadFile();
        setupAudioButtons();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (speechManager != null) {
            speechManager.stop();
            speechManager.shutdown();
        }
        super.onDestroy();
    }

    private void downloadFile() {
        File file = new File(getActivity().getFilesDir(), TEMP_FILENAME);

        try {
            if (!file.exists())
                file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new DropboxTask(this, file, m_FileName, m_Interface.getApiSession()).execute();

    }

    private void setupAudioButtons() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!speechManager.isSpeaking())
                    speechManager.speak(m_Content, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speechManager.isSpeaking())
                    speechManager.stop();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speechManager.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}
