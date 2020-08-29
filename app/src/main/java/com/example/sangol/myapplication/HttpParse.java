package com.example.sangol.myapplication;
import android.app.AlertDialog;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juned on 3/3/2017.
 */

public class HttpParse {

    String FinalHttpData = "";
    String Result;
    BufferedWriter bufferedWriter;
    OutputStream outputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder = new StringBuilder();
    URL url;

    public String postRequest(HashMap<String, String> Data, String HttpUrlHolder) {

        try {
            url = new URL(HttpUrlHolder);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(14000);

            httpURLConnection.setConnectTimeout(14000);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            outputStream = httpURLConnection.getOutputStream();

            bufferedWriter = new BufferedWriter(

                    new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(FinalDataParse(Data));

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()
                        )
                );
                FinalHttpData = bufferedReader.readLine();
            } else {
                FinalHttpData = "Something Went Wrong";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FinalHttpData;
    }

    public String FinalDataParse(HashMap<String, String> hashMap2) throws UnsupportedEncodingException {

        for (Map.Entry<String, String> map_entry : hashMap2.entrySet()) {

            stringBuilder.append("&");

            stringBuilder.append(URLEncoder.encode(map_entry.getKey(), "UTF-8"));

            stringBuilder.append("=");

            stringBuilder.append(URLEncoder.encode(map_entry.getValue(), "UTF-8"));

        }

        Result = stringBuilder.toString();

        return Result;
    }

    public void showMessage(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static String getDeveloperInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Developer: " + "Fred Sangol Uche" + "\n");
        buffer.append("Email: " + "sangoluche@gmail.com" + "\n");
        buffer.append("Version: " + "v3.0" + "\n");
        buffer.append("Year: " + "2018" + "\n");
        return buffer.toString();
    }

    public boolean checkPasswordMatch(String pass,String retypePass){

        if (pass.equals(retypePass))
            return true;
        else
            return false;

    }


}