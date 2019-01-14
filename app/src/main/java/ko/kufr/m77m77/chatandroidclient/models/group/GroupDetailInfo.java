package ko.kufr.m77m77.chatandroidclient.models.group;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupDetailInfo {
    public long id;
    public String groupName;
    public boolean historyVisibility;
    public long id_attachment;
    public Date ignoreExpire;

    public String displayName;

    public GroupDetailInfo() {

    }

    public GroupDetailInfo(JSONObject json) throws JSONException,ParseException {
        this.id = json.getLong("Id");
        this.groupName = json.optString("GroupName",null);
        this.historyVisibility = json.getBoolean("HistoryVisibility");
        this.id_attachment = json.getLong("Id_Attachment");
        String ignoreExpireStr = json.optString("IgnoreExpire");
        if(ignoreExpireStr == null || ignoreExpireStr == "null")
            this.ignoreExpire = null;
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.ignoreExpire = sdf.parse(ignoreExpireStr);
        }
        this.displayName = json.getString("DisplayName");
    }
}
