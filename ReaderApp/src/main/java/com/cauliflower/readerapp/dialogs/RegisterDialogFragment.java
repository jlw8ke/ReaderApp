package com.cauliflower.readerapp.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cauliflower.readerapp.R;
import com.cauliflower.readerapp.ServerConstants;
import com.cauliflower.readerapp.asynctasks.RegisterTask;
import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;
import com.cauliflower.readerapp.encryption.MD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 12/2/13.
 */
public class RegisterDialogFragment extends DialogFragment {

    private EditText usernameCtrl;
    private EditText passwordCtrl;
    private EditText passwordCtrl2;
    private EditText firstnameCtrl;
    private EditText lastnameCtrl;
    private EditText emailCtrl;
    private TextView passwordWarning;

    private Button okButton;
    private Button cancelButton;

    private UsersTaskInterface m_Interface;

    private String serverURL;

    public static String TAG = "register_user_dialog";

    public static RegisterDialogFragment newInstance(String serverURL) {
        RegisterDialogFragment dialog = new RegisterDialogFragment();
        Bundle args = new Bundle();
        args.putString("serverURL", serverURL);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            m_Interface = (UsersTaskInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement UsersTaskInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_register, container, false);

        serverURL = getArguments().getString("serverURL");

        usernameCtrl = (EditText) rootView.findViewById(R.id.username);
        passwordCtrl = (EditText) rootView.findViewById(R.id.password1);
        passwordCtrl2 = (EditText) rootView.findViewById(R.id.password2);
        firstnameCtrl = (EditText) rootView.findViewById(R.id.first_name);
        lastnameCtrl = (EditText) rootView.findViewById(R.id.last_name);
        emailCtrl = (EditText) rootView.findViewById(R.id.email);
        okButton = (Button) rootView.findViewById(R.id.okButton);
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        passwordWarning = (TextView) rootView.findViewById(R.id.passwordWarning);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                okButton.setEnabled(checkEnableButton());
                if(!passwordCtrl.getText().toString().equals(passwordCtrl2.getText().toString()))
                    showWarning(getString(R.string.password_failure));
                else
                    showWarning("");
            }
        };

        usernameCtrl.addTextChangedListener(watcher);
        passwordCtrl.addTextChangedListener(watcher);
        passwordCtrl2.addTextChangedListener(watcher);
        firstnameCtrl.addTextChangedListener(watcher);
        lastnameCtrl.addTextChangedListener(watcher);
        emailCtrl.addTextChangedListener(watcher);

        showWarning("");
        okButton.setEnabled(checkEnableButton());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return rootView;
    }

    private boolean checkEnableButton() {
        boolean hasPassword = passwordCtrl.getText() != null && passwordCtrl.getText().toString().length() > 0
                && passwordCtrl2.getText() != null && passwordCtrl2.getText().toString().length() > 0;
        boolean hasUsername = usernameCtrl.getText() != null && usernameCtrl.getText().toString().length() > 0;
        boolean hasName = firstnameCtrl.getText() != null && lastnameCtrl.getText() != null
                && firstnameCtrl.getText().toString().length() > 0 && lastnameCtrl.getText().toString().length() > 0;
        boolean hasEmail = emailCtrl.getText() != null && emailCtrl.getText().toString().length() > 0;
        return hasPassword && hasUsername && hasName && hasEmail;
    }

    private void showWarning(String message)
    {
        if(message.length() <= 0)
            passwordWarning.setVisibility(View.INVISIBLE);
        else
        {
            passwordWarning.setText(message);
            passwordWarning.setVisibility(View.VISIBLE);
        }
    }

    private void register() {
        String username = usernameCtrl.getText().toString();
        String password = passwordCtrl.getText().toString();
        String firstname = firstnameCtrl.getText().toString();
        String lastname = lastnameCtrl.getText().toString();
        String email = emailCtrl.getText().toString();

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(ServerConstants.USERNAME, username));
        postParameters.add(new BasicNameValuePair(ServerConstants.PASSWORD, MD5.encrptMD5(password)));
        postParameters.add(new BasicNameValuePair(ServerConstants.FIRSTNAME, firstname));
        postParameters.add(new BasicNameValuePair(ServerConstants.LASTNAME, lastname));
        postParameters.add(new BasicNameValuePair(ServerConstants.EMAIL, email));

        new RegisterTask(m_Interface, postParameters).execute(serverURL);
    }


}
