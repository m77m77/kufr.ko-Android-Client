package ko.kufr.m77m77.chatandroidclient.models.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserPublic implements Serializable {
    public long id;
    public String name;
    public long id_attachment;
    public long id_group;

    public UserPublic() {

    }

    public UserPublic(JSONObject json) throws JSONException {
        this.id = json.getLong("Id");
        this.name = json.getString("Name");
        this.id_attachment = json.optLong("Id_Attachment",0);
        this.id_group = json.optLong("Id_Group",0);
    }
}
