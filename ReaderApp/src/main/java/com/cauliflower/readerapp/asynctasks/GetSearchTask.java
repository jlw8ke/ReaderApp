package com.cauliflower.readerapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.cauliflower.readerapp.objects.AppFile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 11/12/13.
 */
public class GetSearchTask extends AsyncTask<String, Integer, ArrayList<AppFile>>  {

    private SearchTaskInterface m_Interface;
    private ArrayList<NameValuePair> m_Params;

    public GetSearchTask(SearchTaskInterface sti, ArrayList<NameValuePair> params) {
        m_Interface = sti;
        m_Params = params;
    }

    @Override
    protected ArrayList<AppFile> doInBackground(String... params) {
        String url = params[0];
        ArrayList<AppFile> searchResults = new ArrayList<AppFile>();

        try {
            String webJSON = HttpUtils.GetDataAsJSON(url, m_Params);
            Log.i("JSON", webJSON);
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(webJSON).getAsJsonArray();
            for (JsonElement obj : Jarray) {
                AppFile file = gson.fromJson(obj, AppFile.class);
                Log.i("FILE", file.toString());
                searchResults.add(file);
            }
        } catch (Exception e) {
            Log.e("GetSearchTask", "Parse Error");
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(ArrayList<AppFile> files) {
        m_Interface.onSearchReceived(files);
    }
}
