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

import ko.kufr.m77m77.chatandroidclient.fragments.ChatFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.FriendFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.FriendsFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupsFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.IndexFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.MessageFragment;

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
    public void backFromChat() {
        FragmentTransaction trn = this.getSupportFragmentManager().beginTransaction();
        trn.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        trn.show(this.indexFragment);
        trn.remove(this.chatFragment);
        trn.commit();
    }

    @Override
    public void onFragmentInteraction(View view) {
        Log.d("A","X");
    }

    @Override
    public void openChatGroup(long id) {
        this.chatFragment = new ChatFragment();

        FragmentTransaction trn = this.getSupportFragmentManager().beginTransaction();
        trn.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        trn.hide(this.indexFragment);
        trn.add(R.id.mainFragmentContainer,this.chatFragment);
        trn.addToBackStack(null);
        trn.commit();
    }
}
