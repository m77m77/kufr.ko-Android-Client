package ko.kufr.m77m77.chatandroidclient.models;

import java.util.concurrent.Callable;

import ko.kufr.m77m77.chatandroidclient.RequestCallback;

public class Request {
    public String url;
    public String httpMethod;
    public String token;
    public String bodyData;
    public RequestCallback finishMethod;

    public  Request(String url, String method,String token, String data,RequestCallback onFinish) {
        this.url = url;
        this.httpMethod = method;
        this.token = token;
        this.bodyData = data;
        this.finishMethod = onFinish;
    }
}
