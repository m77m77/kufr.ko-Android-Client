package ko.kufr.m77m77.chatandroidclient;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import ko.kufr.m77m77.chatandroidclient.fragments.ChatFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.FriendFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.FriendsFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupsFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.IndexFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.MessageFragment;
import ko.kufr.m77m77.chatandroidclient.models.Request;
import ko.kufr.m77m77.chatandroidclient.models.Response;
import ko.kufr.m77m77.chatandroidclient.models.enums.StatusCode;
import ko.kufr.m77m77.chatandroidclient.models.user.UserPublic;

public class MainActivity extends AppCompatActivity implements GroupFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,IndexFragment.OnFragmentInteractionListener,FriendsFragment.OnFragmentInteractionListener,FriendFragment.OnFragmentInteractionListener,ChatFragment.OnFragmentInteractionListener,MessageFragment.OnFragmentInteractionListener {

    private IndexFragment indexFragment;

    private ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 121);
        }

        this.indexFragment = new IndexFragment();

        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainFragmentContainer,this.indexFragment);
        //ChatFragment chatFrag = new ChatFragment();
        //transaction.add(R.id.mainFragmentContainer,chatFrag);
        transaction.commit();

        //((TextView)this.findViewById(R.id.textView2)).setText(getSharedPreferences("global",Context.MODE_PRIVATE).getString("Token","None"));
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setNewRequests(int num) {
        this.indexFragment.setNewRequests(num);
    }

    @Override
    public void setNewMessages(int num) {
        this.indexFragment.setNewMessages(num);
    }

    @Override
    public void backFromChat() {
        FragmentTransaction trn = this.getSupportFragmentManager().beginTransaction();
        trn.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        trn.show(this.indexFragment);
        trn.remove(this.chatFragment);
        trn.commit();
    }

    @Override
    public void refreshIndex() {
        this.indexFragment.refresh();
    }

    @Override
    public void onFragmentInteraction(View view) {
        Log.d("A","X");
    }

    @Override
    public void openChatGroup(long id) {
        this.chatFragment = ChatFragment.newInstance(id);

        FragmentTransaction trn = this.getSupportFragmentManager().beginTransaction();
        trn.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        trn.hide(this.indexFragment);
        trn.add(R.id.mainFragmentContainer,this.chatFragment);
        trn.addToBackStack(null);
        trn.commit();
    }

    public void sendRequest(String url, String method, String data, final RequestDataCallback callback) {
        new RequestManager().execute(new Request(url, method,this.getSharedPreferences("global",Context.MODE_PRIVATE).getString("Token","None"), data, new RequestCallback() {
            public void call(Response response) {
                if(response.statusCode == StatusCode.OK) {
                    callback.call(response.data);
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
