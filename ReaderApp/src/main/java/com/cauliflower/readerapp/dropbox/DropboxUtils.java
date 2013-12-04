package com.cauliflower.readerapp.dropbox;

import android.content.Context;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;

/**
 * Created by jlw8k_000 on 11/25/13.
 */
public class DropboxUtils {

    public static boolean DropboxAuthenticate(DropboxAPI<AndroidAuthSession> dropboxAPI, Context context) {
        if(dropboxAPI == null)
            return false;

        dropboxAPI.getSession().startAuthentication(context);
        if (dropboxAPI.getSession().authenticationSuccessful()) {
            try {
                dropboxAPI.getSession().finishAuthentication();
                AccessTokenPair tokens = dropboxAPI.getSession().getAccessTokenPair();
                return true;
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
        return false;
    }

}
