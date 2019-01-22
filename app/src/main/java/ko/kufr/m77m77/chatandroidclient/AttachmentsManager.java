package ko.kufr.m77m77.chatandroidclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import ko.kufr.m77m77.chatandroidclient.models.Request;
import ko.kufr.m77m77.chatandroidclient.models.Response;
import ko.kufr.m77m77.chatandroidclient.models.attachments.AttachmentMessage;
import ko.kufr.m77m77.chatandroidclient.models.enums.StatusCode;

public class AttachmentsManager {

    public class BitmapCallback {
        public void call(Bitmap image) {

        }
    }

    public void getImage(long id,String token,BitmapCallback callback) {
        new RequestManager().execute(new Request("api/file/loadattachment", "GET",token, null, new RequestCallback() {
            public void call(Response response) {
                if(response.statusCode == StatusCode.OK) {
                    //Log.d()
                }else {
                    Log.d("Debug","Respone code not ok: " + response.statusCode.toString());

                    /*switch (response.statusCode) {
                        case INVALID_REQUEST:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(getString(R.string.error_invalid_request));
                            break;
                        case DATABASE_ERROR:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_database_error);
                            break;
                        case INVALID_EMAIL:
                            errorEmail.setVisibility(View.VISIBLE);
                            errorEmail.setText(R.string.error_invalid_email);
                            break;
                        case INVALID_PASSWORD:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_invalid_password);
                            break;
                        case EMAIL_ALREADY_EXISTS:
                            errorEmail.setVisibility(View.VISIBLE);
                            errorEmail.setText(R.string.error_email_already_exists);
                            break;
                        case EMPTY_EMAIL:
                            errorEmail.setVisibility(View.VISIBLE);
                            errorEmail.setText(R.string.error_empty_email);
                            break;
                        case EMPTY_PASSWORD:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_empty_password);
                            break;
                        case EMPTY_NAME:
                            errorName.setVisibility(View.VISIBLE);
                            errorName.setText(R.string.error_empty_name);
                            break;
                        case NETWORK_ERROR:
                            errorPassword.setVisibility(View.VISIBLE);
                            errorPassword.setText(R.string.error_network);
                            break;
                    }*/
                }
            }
        }));
    }


}
