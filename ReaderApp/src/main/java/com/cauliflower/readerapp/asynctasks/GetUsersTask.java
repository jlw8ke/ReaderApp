package com.cauliflower.readerapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.cauliflower.readerapp.User;

import java.util.ArrayList;
import com.google.gson.*;

/**
 * Created by jlw8k_000 on 10/29/13.
 */
public class GetUsersTask extends AsyncTask<String, Integer, String> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        ArrayList<User> userList = new ArrayList<User>();

        /*try {
            String webJSON = HttpUtils.getDataAsJSON(url);
            Log.d("JSON", webJSON);
            Gson gson = new Gson();

            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(webJSON).getAsJsonArray();

            for (JsonElement obj : Jarray) {
                Course cse = gson.fromJson(obj, Course.class);
                Log.d("COURSE", cse.toString());
                lcs.add(cse);
            }

        } catch (Exception e) {
            Log.e("LousList", "JSONPARSE:" + e.toString());
        }

        values.clear();
        values.addAll(lcs);*/

        return "Done!";
    }

    @Override
    protected void onProgressUpdate(Integer... ints) {

    }

    @Override
    protected void onPostExecute(String result) {
        // tells the adapter that the underlying data has changed and it
        // needs to update the view
        //adapter.notifyDataSetChanged();
    }
}
