package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.RequestCallback;
import ko.kufr.m77m77.chatandroidclient.RequestDataCallback;
import ko.kufr.m77m77.chatandroidclient.RequestManager;
import ko.kufr.m77m77.chatandroidclient.models.Request;
import ko.kufr.m77m77.chatandroidclient.models.Response;
import ko.kufr.m77m77.chatandroidclient.models.enums.StatusCode;
import ko.kufr.m77m77.chatandroidclient.models.group.GroupInfo;
import ko.kufr.m77m77.chatandroidclient.models.user.UserPublic;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FriendFragment[] pending;
    private FriendFragment[] existing;

    private FriendFragment[] newPending;
    private FriendFragment[] newExisting;

    private boolean pendingDone;
    private boolean existingDone;


    private SectionDividerFragment pendingDivider;
    private SectionDividerFragment existingDivider;

    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        this.pendingDivider = SectionDividerFragment.newInstance(this.getContext().getString(R.string.friends_requests_title));
        this.existingDivider = SectionDividerFragment.newInstance(this.getContext().getString(R.string.friends_normal_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //this.loadPendingFriends();
        //this.loadExistingFriends();

        SwipeRefreshLayout refreshView = (SwipeRefreshLayout) view.findViewById(R.id.friends_swipe_container);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refreshView.setRefreshing(true);
        this.refresh();
    }

    private void debugCrtFriend() {
        Log.d("Debug","friends loaded.");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.friends,this.pendingDivider);
        for(int i = 0; i < 10; i++){
            if(i == 2)
                ft.add(R.id.friends,this.existingDivider);

            UserPublic up = new UserPublic();
            up.name = "Jméno Příjmení #" + i;
            FriendFragment newFragment = FriendFragment.newInstance(up,i < 2 ? FriendFragment.Type.REQUEST : FriendFragment.Type.NORMAL);

            ft.add(R.id.friends, newFragment);

        }
        ft.commit();
    }

    public void refresh() {

        this.pendingDone = false;
        this.existingDone = false;

        this.loadPendingFriends();
        this.loadExistingFriends();
    }

    private void refreshDone() {
        if(!(this.pendingDone && this.existingDone))
            return;

        FragmentTransaction ftRem = getActivity().getSupportFragmentManager().beginTransaction();

        ftRem.remove(this.pendingDivider);
        ftRem.remove(this.existingDivider);

        if(this.pending != null)
        for (FriendFragment frag: this.pending) {
            ftRem.remove(frag);
        }

        if(this.existing != null)
        for (FriendFragment frag: this.existing) {
            ftRem.remove(frag);
        }

        ftRem.commit();


        this.pending = this.newPending;
        this.existing = this.newExisting;

        this.pendingDivider = SectionDividerFragment.newInstance(this.getContext().getString(R.string.friends_requests_title));
        this.existingDivider = SectionDividerFragment.newInstance(this.getContext().getString(R.string.friends_normal_title));


        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        if(this.pending.length > 0)
        ft.add(R.id.friends,this.pendingDivider);

        for (FriendFragment frag: this.pending) {
            ft.add(R.id.friends,frag);
        }


        ft.add(R.id.friends,this.existingDivider);

        for (FriendFragment frag: this.existing) {
            ft.add(R.id.friends,frag);
        }

        ft.commit();

        ((SwipeRefreshLayout) this.getView().findViewById(R.id.friends_swipe_container)).setRefreshing(false);

        if(mListener != null) {
            mListener.setNewRequests(this.pending.length);
        }
    }

    private void loadExistingFriends() {
        this.mListener.sendRequest("api/friend/loadexistingfriends","GET",null, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                try {
                    JSONArray array = new JSONArray(data.toString());

                    newExisting = new FriendFragment[array.length()];

                    for(int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        UserPublic user = new UserPublic(obj);

                        FriendFragment newFragment = FriendFragment.newInstance(user,FriendFragment.Type.NORMAL);

                        newExisting[i] = newFragment;
                    }

                    existingDone = true;

                    refreshDone();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadPendingFriends() {
        this.mListener.sendRequest("api/friend/loadpending","GET",null, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                try {
                    JSONArray array = new JSONArray(data.toString());

                    newPending = new FriendFragment[array.length()];

                    for(int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        UserPublic user = new UserPublic(obj);

                        FriendFragment newFragment = FriendFragment.newInstance(user,FriendFragment.Type.REQUEST);

                        newPending[i] = newFragment;
                    }

                    pendingDone = true;

                    refreshDone();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void setNewRequests(int num);
        void sendRequest(String url, String method, String data, final RequestDataCallback callback);
    }
}
