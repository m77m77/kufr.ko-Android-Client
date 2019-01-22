package ko.kufr.m77m77.chatandroidclient.models.attachments;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AttachmentMessage implements Serializable {
    public long id;
    public String filename;
    public String mime;

    public AttachmentMessage() {

    }

    public AttachmentMessage(JSONObject json) throws JSONException {
        this.id = json.getLong("Id_Attachment");
        this.filename = json.optString("Filename","");
        this.mime = json.optString("Mime","");
    }
}
