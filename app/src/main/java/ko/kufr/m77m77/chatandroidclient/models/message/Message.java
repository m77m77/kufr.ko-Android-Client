package ko.kufr.m77m77.chatandroidclient.models.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.models.attachments.AttachmentMessage;

public class Message implements Serializable {
    public long id;
    public boolean edited;
    public String text;
    public AttachmentMessage[] attachments;
    public long id_group;

    public long sender_id;
    public String sender_name;
    public long sender_id_attachment;
    public  String sender_nickname;

    public Date sent;
    public boolean userIsSender;


    public View view;

    public Message() {

    }

    public Message(JSONObject json) throws JSONException,ParseException {
        this.id = json.getLong("Id");
        this.edited = json.getBoolean("Edited");
        this.text = json.getString("Text");
        JSONArray attachments = json.getJSONArray("Id_Attachment");
        this.attachments = new AttachmentMessage[attachments.length()];
        for (int i = 0; i < attachments.length(); i++) {
            this.attachments[i] = new AttachmentMessage(attachments.getJSONObject(i));
        }
        this.id_group = json.getLong("Id_Group");
        JSONObject uInfo = json.getJSONObject("UserInfo");
        this.sender_id = uInfo.getLong("Id");
        this.sender_name = uInfo.getString("Name");
        this.sender_id_attachment = uInfo.optLong("Id_Attachment",0);
        this.sender_nickname = uInfo.optString("Nickname",null);

        String sentStr = json.optString("Sent");
        if(sentStr == null || sentStr == "null")
            this.sent = null;
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.sent = sdf.parse(sentStr);
        }
        this.userIsSender = json.getBoolean("UserIsSender");
    }

    public View getView(Context context,ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(this.userIsSender ? R.layout.fragment_message_sender : R.layout.fragment_message_normal,parent,false);

        ((TextView) view.findViewById(R.id.message_sender)).setText(this.sender_nickname == null || this.sender_nickname == "null" ? this.sender_name : this.sender_nickname);
        ((TextView) view.findViewById(R.id.message_text)).setText(this.text);

        Calendar now = Calendar.getInstance();
        Calendar lmDate = Calendar.getInstance();
        lmDate.setTime(this.sent);

         String pattern = "";

        if(now.get(Calendar.YEAR) != lmDate.get(Calendar.YEAR)) {
            pattern = "dd.MM.yyyy  ";

        }else if(now.get(Calendar.DAY_OF_YEAR) != lmDate.get(Calendar.DAY_OF_YEAR)) {
            pattern = "dd.MM.  ";
        }
            pattern += "HH:mm";


        SimpleDateFormat format =  new SimpleDateFormat(pattern);

        ((TextView) view.findViewById(R.id.message_date)).setText(format.format(lmDate.getTime()));

        this.view = view;

        return view;
    }

    public void setLast(boolean last) {
        if(this.view == null)
            return;

        if(last) {
            this.view.findViewById(R.id.message_user_image).setVisibility(View.VISIBLE);
        }else {
            this.view.findViewById(R.id.message_user_image).setVisibility(View.INVISIBLE);
        }

    }

    public void setFirst(boolean first) {
        if(this.view == null)
            return;

        if(first) {
            this.view.findViewById(R.id.message_sender).setVisibility(View.VISIBLE);
        }else {
            this.view.findViewById(R.id.message_sender).setVisibility(View.GONE);
        }
    }

    public void setDate(boolean visible) {
        if(this.view == null)
            return;

        if(visible) {
            this.view.findViewById(R.id.message_date).setVisibility(View.VISIBLE);
            //this.view.findViewById(R.id.message_user_image).setVisibility(View.VISIBLE);
        }else {
            this.view.findViewById(R.id.message_date).setVisibility(View.GONE);
        }
    }
}
