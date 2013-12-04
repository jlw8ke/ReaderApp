package com.cauliflower.readerapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.cauliflower.readerapp.HttpUtils;
import com.cauliflower.readerapp.objects.User;

import java.util.ArrayList;

import com.google.gson.*;

/**
 * Created by jlw8k_000 on 10/29/13.
 */
public class GetUsersTask extends AsyncTask<String, Integer, ArrayList<User>> {

    private UsersTaskInterface m_Interface;

    public GetUsersTask(UsersTaskInterface uti){
        m_Interface = uti;
    }

    @Override
    protected ArrayList<User> doInBackground(String... params) {
        String url = params[0];
        ArrayList<User> userList = new ArrayList<User>();
        try {
            String webJSON = HttpUtils.GetDataAsJSON(url, null);
            Log.d("JSON", webJSON);
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(webJSON).getAsJsonArray();
            for (JsonElement obj : Jarray) {
                User user = gson.fromJson(obj, User.class);
                Log.d("USER", user.toString());
                userList.add(user);
            }
        } catch (Exception e) {
            Log.e("GetUsersTask", "Parse Error");
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    protected void onProgressUpdate(Integer... ints) {

    }

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        m_Interface.onUsersReceived(users);
    }
}
