package com.cauliflower.readerapp.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.cauliflower.readerapp.R;
import com.cauliflower.readerapp.asynctasks.GetSearchTask;
import com.cauliflower.readerapp.asynctasks.GetUsersTask;
import com.cauliflower.readerapp.asynctasks.SearchTaskInterface;
import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jlw8k_000 on 10/30/13.
 */
public class TestDialogFragment extends DialogFragment {

    private final String PDF = "http://pdfs2speech.appspot.com/search";
    private final String PLATO = "http://plato.cs.virginia.edu/~cs4720f13cauliflower/TextToSpeech/developers/users/";


    private UsersTaskInterface m_UsersInterface;
    private SearchTaskInterface m_SearchInterface;
    private Spinner userChoices;
    private Button goButton;
    private EditText searchBox;
    private ToggleButton serverChoice;

    int user_id = 1;

    public static TestDialogFragment newInstance() {
        TestDialogFragment dialog = new TestDialogFragment();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_dialog, container, false);
        m_UsersInterface = (UsersTaskInterface) getTargetFragment();
        m_SearchInterface = (SearchTaskInterface) getTargetFragment();

        userChoices = (Spinner) rootView.findViewById(R.id.userIdSelector);
        goButton = (Button) rootView.findViewById(R.id.goButton);
        searchBox = (EditText) rootView.findViewById(R.id.python_search);
        serverChoice = (ToggleButton) rootView.findViewById(R.id.serverChoice);

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(1,2));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userChoices.setAdapter(dataAdapter);
        userChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long id) {
                user_id = index+1;
                Log.i("TestDialogFragment", Integer.toString(user_id));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                user_id = 1;
                Log.i("TestDialogFragment", Integer.toString(user_id));
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!serverChoice.isChecked())
                    new GetUsersTask(m_UsersInterface, null).execute(PLATO + Integer.toString(user_id));
                else {
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("query", searchBox.getText().toString()));
                    new GetSearchTask(m_SearchInterface, params).execute(PDF);
                }
                dismiss();
            }
        });

        return rootView;
    }




}
