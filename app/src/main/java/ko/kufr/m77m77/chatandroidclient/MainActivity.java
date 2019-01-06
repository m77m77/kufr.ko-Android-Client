package ko.kufr.m77m77.chatandroidclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ko.kufr.m77m77.chatandroidclient.fragments.FriendFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.FriendsFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.GroupsFragment;
import ko.kufr.m77m77.chatandroidclient.fragments.IndexFragment;

public class MainActivity extends AppCompatActivity implements GroupFragment.OnFragmentInteractionListener,GroupsFragment.OnFragmentInteractionListener,IndexFragment.OnFragmentInteractionListener,FriendsFragment.OnFragmentInteractionListener,FriendFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        FragmentManager man = this.getSupportFragmentManager();
        FragmentTransaction transaction = man.beginTransaction();

        IndexFragment frag = new IndexFragment();
        transaction.add(R.id.mainFragmentContainer,frag);
        transaction.commit();

        //((TextView)this.findViewById(R.id.textView2)).setText(getSharedPreferences("global",Context.MODE_PRIVATE).getString("Token","None"));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(View view) {
        Log.d("A","X");
    }
}
