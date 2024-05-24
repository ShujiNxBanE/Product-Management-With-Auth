package com.rogersmarket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WSJson {

    public static JSONObject getJson(String strUrl) throws IOException, JSONException {
        String jsonStr;
        URL url = new URL(strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000); // 10 seconds
        conn.setReadTimeout(10000); // 10 seconds

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            jsonStr = sb.toString();
            return new JSONObject(jsonStr);
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }

    public static JSONObject sendJson(String strUrl, JSONObject jsonParam) throws IOException, JSONException {
        URL url = new URL(strUrl);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/json");
        urlConn.setRequestProperty("Accept", "application/json");
        urlConn.setConnectTimeout(10000); // 10 seconds
        urlConn.setReadTimeout(10000); // 10 seconds

        try (DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream())) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(printout, StandardCharsets.UTF_8));
            writer.write(jsonParam.toString());
            writer.flush();
        }

        int responseCode = urlConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } else {
            throw new IOException("HTTP error code: " + responseCode);
        }
    }
}
