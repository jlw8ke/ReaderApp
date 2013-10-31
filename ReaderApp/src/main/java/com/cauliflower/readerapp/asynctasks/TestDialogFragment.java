package com.cauliflower.readerapp.asynctasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cauliflower.readerapp.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jlw8k_000 on 10/30/13.
 */
public class TestDialogFragment extends DialogFragment {

    private UsersTaskInterface m_Interface;
    private Spinner userChoices;
    private Button goButton;
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
        m_Interface = (UsersTaskInterface) getTargetFragment();
        userChoices = (Spinner) rootView.findViewById(R.id.userIdSelector);
        goButton = (Button) rootView.findViewById(R.id.goButton);

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
                new GetUsersTask(m_Interface).execute("http://plato.cs.virginia.edu/~cs4720f13cauliflower/TextToSpeech/developers/users");
                dismiss();
            }
        });

        return rootView;
    }
}
