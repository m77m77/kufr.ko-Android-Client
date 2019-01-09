package ko.kufr.m77m77.chatandroidclient.models.message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {
    public long id;
    public boolean edited;
    public String text;
    public long[] id_attachments;
    public long id_group;

    public long sender_id;
    public String sender_name;
    public long sender_id_attachment;
    public  String sender_nickname;

    public Date sent;

    public Message() {

    }

    public Message(JSONObject json) throws JSONException,ParseException {
        this.id = json.getLong("Id");
        this.edited = json.getBoolean("Edited");
        this.text = json.getString("Text");
        JSONArray attachments = json.getJSONArray("Id_Attachment");
        this.id_attachments = new long[attachments.length()];
        for (int i = 0; i < attachments.length(); i++) {
            this.id_attachments[i] = attachments.getLong(i);
        }
        this.id_group = json.getLong("Id_Group");
        JSONObject uInfo = json.getJSONObject("UserInfo");
        this.sender_id = uInfo.getLong("Id");
        this.sender_name = uInfo.getString("Name");
        this.sender_id_attachment = uInfo.getLong("Id_Attachment");
        this.sender_nickname = uInfo.getString("Nickname");

        String sentStr = json.optString("Sent");
        if(sentStr == null || sentStr == "null")
            this.sent = null;
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.sent = sdf.parse(sentStr);
        }
    }
}
