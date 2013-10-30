package com.cauliflower.readerapp.asynctasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URI;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jlw8k_000 on 10/29/13.
 */
public class HttpUtils {

    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;

    private static InputStream getData(String strURL, int connectionTimeout, int socketTimeout) {
        HttpClient client;
        HttpResponse response;
        InputStream result = null;

        HttpParams httpParameters = new BasicHttpParams();

        // Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);

        // Set the timeout in milliseconds for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);

        client = new DefaultHttpClient(httpParameters);
        try {
            response = client.execute(new HttpGet(strURL));
            result = response.getEntity().getContent();

            //Check for redirect
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                result = null;

        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static String getDataAsJSON(String strURL) {

        InputStream incomingStream = getData(strURL, CONNECTION_TIMEOUT, SOCKET_TIMEOUT);
        String result = "";

        if (incomingStream != null) {
            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(
                        incomingStream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = streamReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                streamReader.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("HttpUtils", "Error converting result " + e.toString());
            }
        }
        return result;
    }

    public static Object getDataAsObject(String strURL) {
        InputStream incomingStream;
        ObjectInputStream objStream;
        Object result = null;

        incomingStream = getData(strURL, CONNECTION_TIMEOUT, SOCKET_TIMEOUT);
        if (incomingStream != null) {
            try {
                objStream = new ObjectInputStream(incomingStream);
                result = objStream.readObject();
            } catch (Exception e) {
                result = null;
            }
        }
        return result;
    }

}
