package ko.kufr.m77m77.chatandroidclient.models;

import org.json.JSONObject;

import ko.kufr.m77m77.chatandroidclient.models.enums.StatusCode;

public class Response {
    public StatusCode statusCode;
    public Object data;

    public Response() {

    }

    public Response(StatusCode code,Object data) {
        this.statusCode = code;
        this.data = data;
    }
}
