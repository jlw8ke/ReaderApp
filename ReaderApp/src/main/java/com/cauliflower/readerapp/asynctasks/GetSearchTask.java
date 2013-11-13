package com.cauliflower.readerapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.cauliflower.readerapp.PDF2SpeechFile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 11/12/13.
 */
public class GetSearchTask extends AsyncTask<String, Integer, ArrayList<PDF2SpeechFile>>  {

    private SearchTaskInterface m_Interface;
    private ArrayList<NameValuePair> m_Params;

    public GetSearchTask(SearchTaskInterface sti, ArrayList<NameValuePair> params) {
        m_Interface = sti;
        m_Params = params;
    }

    @Override
    protected ArrayList<PDF2SpeechFile> doInBackground(String... params) {
        String url = params[0];
        ArrayList<PDF2SpeechFile> searchResults = new ArrayList<PDF2SpeechFile>();

        try {
            String webJSON = HttpUtils.getDataAsJSON(url, m_Params);
            Log.d("JSON", webJSON);
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray Jarray = parser.parse(webJSON).getAsJsonArray();
            for (JsonElement obj : Jarray) {
                PDF2SpeechFile file = gson.fromJson(obj, PDF2SpeechFile.class);
                Log.d("FILE", file.toString());
                searchResults.add(file);
            }
        } catch (Exception e) {
            Log.e("GetSearchTask", "Parse Error");
            e.printStackTrace();
        }
        return searchResults;
    }


}
