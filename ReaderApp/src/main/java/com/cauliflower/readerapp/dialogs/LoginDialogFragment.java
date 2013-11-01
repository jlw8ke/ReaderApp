package com.cauliflower.readerapp.dialogs;

import android.app.DialogFragment;

/**
 * Created by jlw8k_000 on 11/1/13.
 */
public class LoginDialogFragment extends DialogFragment {

    private String username;
    private String password;

    public LoginDialogFragment newInstance() {
        LoginDialogFragment dialog = new LoginDialogFragment();
        return dialog;
    }

}
