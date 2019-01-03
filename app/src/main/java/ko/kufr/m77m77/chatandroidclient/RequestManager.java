package ko.kufr.m77m77.chatandroidclient;

import android.os.AsyncTask;
import android.os.Debug;
import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ko.kufr.m77m77.chatandroidclient.models.Request;
import ko.kufr.m77m77.chatandroidclient.models.Response;
import ko.kufr.m77m77.chatandroidclient.models.enums.StatusCode;

public class RequestManager extends AsyncTask<Request, Void, Response> {

    private static final String restApiUrl = "http://10.0.2.2:49608/";

    private static RequestCallback callback;

    private JSONObject sendRequest(Request req) throws IOException,JSONException {
        URL url = new URL(restApiUrl + req.url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.setRequestMethod(req.httpMethod);

        if(req.bodyData != null) {
            OutputStreamWriter requestWriter = new OutputStreamWriter(conn.getOutputStream());

            requestWriter.write(req.bodyData);
            requestWriter.flush();
            requestWriter.close();
        }

        InputStreamReader inputReader = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        String line;

        StringBuilder sb = new StringBuilder();

        while((line = bufferedReader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }

        inputReader.close();
        bufferedReader.close();

        return new JSONObject(sb.toString());
    }

    @Override
    protected Response doInBackground(Request... args){

        try {
            RequestManager.callback = args[0].finishMethod;
            JSONObject res = this.sendRequest(args[0]);

            return new Response(StatusCode.values()[res.getInt("StatusCode")],res.get("Data"));

        } catch (IOException e) {
            e.printStackTrace();
            return new Response();
        } catch (JSONException e) {
            e.printStackTrace();
            return new Response();
        }
    }

    @Override
    protected void onPostExecute(Response result) {
        RequestManager.callback.call(result);
    }
}
