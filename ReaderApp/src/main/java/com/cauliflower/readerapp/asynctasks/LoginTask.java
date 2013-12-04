package com.cauliflower.readerapp.asynctasks;

import android.os.AsyncTask;

import com.cauliflower.readerapp.HttpUtils;
import com.cauliflower.readerapp.objects.User;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 12/1/13.
 */
public class LoginTask extends AsyncTask<String, Void, User> {


    private ArrayList<NameValuePair> params;
    private UsersTaskInterface m_Interface;

    public LoginTask(UsersTaskInterface uti, ArrayList<NameValuePair> params) {
        m_Interface = uti;
        this.params = params;
    }

    @Override
    protected User doInBackground(String... serverList) {
        String url = serverList[0];
        String result =  HttpUtils.PostData(url, params);
        User theUser = null;
        try {
            theUser = new Gson().fromJson(result, User.class);
        } catch(Exception e) { e.printStackTrace(); }
        return theUser;
    }

    @Override
    protected void onPostExecute(User user) {
        m_Interface.login(user);
    }
}
