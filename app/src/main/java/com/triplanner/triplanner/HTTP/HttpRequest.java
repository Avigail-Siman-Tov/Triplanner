package com.triplanner.triplanner.HTTP;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


//@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class HttpRequest extends AsyncTask<HttpCall, String, String> {
    private static final String UTF_8 = "UTF-8";

    @Override
    protected String doInBackground(HttpCall... params) {
        HttpURLConnection urlConnection = null;
        HttpCall httpCall = params[0];
        StringBuilder response = new StringBuilder();
        try{
            String dataParams = getDataString(httpCall.getParams(), httpCall.getMethodtype());
            URL url = new URL(httpCall.getMethodtype() == HttpCall.GET ? httpCall.getUrl() + dataParams : httpCall.getUrl()+ "?" + dataParams);
            Log.d("URL", url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpCall.getMethodtype() == HttpCall.GET ? "GET":"POST");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line ;
                BufferedReader br = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response.append(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(String response){

    }

    private String getDataString(HashMap<String,String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String,String> entry : params.entrySet()){
            if (isFirst){
                isFirst = false;
                if(methodType == HttpCall.GET){
                    result.append("?");
                }
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF_8));
        }
        return result.toString();
    }
}
