package ko.kufr.m77m77.chatandroidclient.models;

import java.util.concurrent.Callable;

import ko.kufr.m77m77.chatandroidclient.RequestCallback;

public class Request {
    public String url;
    public String httpMethod;
    public String bodyData;
    public RequestCallback finishMethod;

    public  Request(String url, String method, String data,RequestCallback onFinish) {
        this.url = url;
        this.httpMethod = method;
        this.bodyData = data;
        this.finishMethod = onFinish;
    }
}
