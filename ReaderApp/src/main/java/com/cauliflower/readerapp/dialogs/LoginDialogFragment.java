package com.cauliflower.readerapp.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cauliflower.readerapp.R;
import com.cauliflower.readerapp.ServerConstants;
import com.cauliflower.readerapp.asynctasks.HttpUtils;
import com.cauliflower.readerapp.asynctasks.LoginTask;
import com.cauliflower.readerapp.asynctasks.UsersTaskInterface;
import com.cauliflower.readerapp.encryption.MD5;
import com.cauliflower.readerapp.objects.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 11/1/13.
 */
public class LoginDialogFragment extends DialogFragment {

    private EditText usernameCtrl;
    private EditText passwordCtrl;
    private Button okButton;
    private Button cancelButton;
    private Button registerButton;
    private TextView loginWarning;

    private String serverURL;

    public static final String ERROR_MESSAGE_1 = "Invalid login information.  Please try again.";
    public static final String ERROR_MESSAGE_2 = "We're sorry... Something went wrong with the login.  Please try again.";
    public static final String TAG = "user_login_dialog";


    UsersTaskInterface m_Interface;

    public static LoginDialogFragment newInstance(String serverURL) {
        LoginDialogFragment dialog = new LoginDialogFragment();
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
        View rootView = inflater.inflate(R.layout.dialog_fragment_login, container, false);

        serverURL = getArguments().getString("serverURL");

        usernameCtrl = (EditText) rootView.findViewById(R.id.username);
        passwordCtrl = (EditText) rootView.findViewById(R.id.password);
        okButton = (Button) rootView.findViewById(R.id.okButton);
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        registerButton = (Button) rootView.findViewById(R.id.registerButton);
        loginWarning = (TextView) rootView.findViewById(R.id.loginWarning);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                okButton.setEnabled(checkEnableButton());
            }
        };
        usernameCtrl.addTextChangedListener(watcher);
        passwordCtrl.addTextChangedListener(watcher);
        showWarning("");

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private boolean checkEnableButton() {
        boolean hasPassword = passwordCtrl.getText() != null && passwordCtrl.getText().toString().length() > 0;
        boolean hasUsername = usernameCtrl.getText() != null && usernameCtrl.getText().toString().length() > 0;
        return hasPassword && hasUsername;
    }

    private void showWarning(String message)
    {
        if(message.length() <= 0)
            loginWarning.setVisibility(View.INVISIBLE);
        else
        {
            loginWarning.setText(message);
            loginWarning.setVisibility(View.VISIBLE);
        }
    }

    private void login() {
        String username = usernameCtrl.getText().toString();
        String password = passwordCtrl.getText().toString();
        showWarning("");

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair(ServerConstants.USERNAME, username));
        postParameters.add(new BasicNameValuePair(ServerConstants.PASSWORD, MD5.encrptMD5(password)));

        new LoginTask(m_Interface, postParameters).execute(serverURL);
    }
}
