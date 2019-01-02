package ko.kufr.m77m77.chatandroidclient;

import android.os.AsyncTask;
import android.os.Debug;
import android.util.JsonReader;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestManager extends AsyncTask<String, Integer, String> {

    private static final String restApiUrl = "http://10.0.2.2:49608";

    public RequestManager() {

    }

    private String sendRequest() throws IOException {
        URL url = new URL(restApiUrl + "/api/auth/register");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        conn.setRequestMethod("POST");

        OutputStreamWriter requestWriter = new OutputStreamWriter(conn.getOutputStream());

        requestWriter.write("Email=a@a.a&Password=123&Name=AndroidTest");
        requestWriter.flush();
        requestWriter.close();

        InputStreamReader inputReader = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        String line = "";

        StringBuilder sb = new StringBuilder();

        while((line = bufferedReader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        inputReader.close();
        bufferedReader.close();

        return sb.toString();
    }

    @Override
    protected String doInBackground(String... args){
        if(args.length < 3)
            return "";

        try {
            return this.sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
