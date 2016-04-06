package com.inorexstudio.gauravdeshmukh.foodex;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by gauravdeshmukh on 3/23/16.
 */

class NetworkActivity extends AsyncTask<String, Void, String> {
    String response;
    @Override
    protected String doInBackground(String... urls) {
        response = "";
        // String url = urls[0];
        for (String url : urls) { // take heed
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            Log.e("TAG",url);
            try {
                HttpResponse execute;
                execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;

                }

                if (buffer != null) {
                    buffer = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return response;

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
//        data.setText("");
    }

    @Override
    protected void onPostExecute(String result) {

        // super.onPostExecute(result);
//        data.setText(result);

    }

}