package ko.kufr.m77m77.chatandroidclient.models.group;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupInfo implements Serializable {
    public long id;
    public String name;
    public long id_attachment;
    public Date ignoreExpire;

    public String lastMessageSender;
    public String lastMessageText;
    public Date lastMessageDate;

    public int newMessages;

    public GroupInfo() {

    }

    public GroupInfo(JSONObject json) throws JSONException,ParseException {
        this.id = json.getLong("Id");
        this.name = json.getString("Name");
        this.id_attachment = json.getLong("Id_Attachment");
        String ignoreExpireStr = json.getString("IgnoreExpire");
        if(ignoreExpireStr == null)
            this.ignoreExpire = null;
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.ignoreExpire = sdf.parse(ignoreExpireStr);
        }

        this.lastMessageSender = json.getString("LastMessageSender");
        this.lastMessageText = json.getString("LastMessageText");
        String lastMessageDateStr = json.getString("LastMessageDate");
        if(lastMessageDateStr == null)
            this.lastMessageDate = null;
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.lastMessageDate = sdf.parse(lastMessageDateStr);
        }

        this.newMessages = json.getInt("NewMessages");
    }
}
