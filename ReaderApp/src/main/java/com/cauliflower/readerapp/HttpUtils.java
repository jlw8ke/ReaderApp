package com.cauliflower.readerapp;

import android.util.Log;

import com.cauliflower.readerapp.constants.ServerConstants;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jlw8k_000 on 10/29/13.
 */
public class HttpUtils {

    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;

    private static InputStream getData(String strURL, int connectionTimeout, int socketTimeout, ArrayList<NameValuePair> params) {

        HttpClient client;
        HttpResponse response;
        InputStream result;

        HttpParams httpParameters = new BasicHttpParams();

        // Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);

        // Set the timeout in milliseconds for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);

        client = new DefaultHttpClient(httpParameters);
        if (params != null)
            strURL += "?" + URLEncodedUtils.format(params, "utf-8");

        Log.i("HttpUtils", "The url for the GET: " + strURL);
        try {
            response = client.execute(new HttpGet(strURL));
            result = response.getEntity().getContent();

            //Check for redirect
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                result = null;

        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static String GetDataAsJSON(String strURL, ArrayList<NameValuePair> params) {

        InputStream incomingStream = getData(strURL, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, params);
        String result = "";

        if (incomingStream != null) {
            try {
                result = InputStreamToJson(incomingStream);
            } catch (Exception e) {
                Log.e("HttpUtils", "Error converting result " + e.toString());
            }
        }
        Log.d("HttpUtils", "Result: " + result);
        return result;
    }

    public static Object GetDataAsObject(String strURL, ArrayList<NameValuePair> params) {
        InputStream incomingStream;
        incomingStream = getData(strURL, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, params);
        return InputStreamToObject(incomingStream);
    }

    public static String PostData(String strUrl, ArrayList<NameValuePair> postParameters) {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(strUrl);

        InputStream incomingStream;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpResponse response = client.execute(httpPost);
            incomingStream = response.getEntity().getContent();
            return InputStreamToJson(incomingStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String UploadFile(String strURL, File file) {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(strURL);
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart(ServerConstants.FILE_CONTENT, new FileBody(file));

            InputStream incomingStream;
            System.out.println(entity.toString());
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            incomingStream = response.getEntity().getContent();
            return InputStreamToJson(incomingStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static boolean PostDataAsByteArray(String strURL, Object data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.close();

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpParameters);

            //Create a new POST call from the URL supplied.
            HttpPost httpPost = new HttpPost(strURL);
            httpPost.setEntity(new ByteArrayEntity(baos.toByteArray()));
            client.execute(httpPost);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Object InputStreamToObject(InputStream incomingStream) {
        ObjectInputStream objStream;
        Object result = null;

        if (incomingStream != null) {
            try {
                /*BufferedReader in = new BufferedReader(new InputStreamReader(incomingStream));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                String test = sb.toString();
                System.out.println("test: " + test);*/


                objStream = new ObjectInputStream(incomingStream);
                result = objStream.readObject();
            } catch (Exception e) {
                result = null;
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String InputStreamToJson(InputStream incomingStream) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(
                incomingStream, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = streamReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        streamReader.close();
        return sb.toString();
    }


}
