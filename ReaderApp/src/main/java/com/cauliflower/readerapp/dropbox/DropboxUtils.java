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

    public static void DropboxAuthenticate(DropboxAPI<AndroidAuthSession> dropboxAPI, Context context) {
        if(dropboxAPI == null)
            return;

        dropboxAPI.getSession().startAuthentication(context);

    }

}
