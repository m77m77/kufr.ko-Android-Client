package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.RequestCallback;
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

        this.debugCrtFriend();
        //this.loadPendingFriends();
        //this.loadExistingFriends();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //this.loadExistingFriends();

    }

    private void debugCrtFriend() {
        Log.d("Debug","friends loaded.");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.friends,SectionDividerFragment.newInstance(this.getContext().getString(R.string.friends_requests_title)));
        for(int i = 0; i < 10; i++){
            if(i == 2)
                ft.add(R.id.friends,SectionDividerFragment.newInstance(this.getContext().getString(R.string.friends_normal_title)));

            UserPublic up = new UserPublic();
            up.name = "Jméno Příjmení #" + i;
            FriendFragment newFragment = FriendFragment.newInstance(up,i < 2 ? FriendFragment.Type.REQUEST : FriendFragment.Type.NORMAL);

            ft.add(R.id.friends, newFragment);

        }
        ft.commit();
    }

    private void loadExistingFriends() {
        new RequestManager().execute(new Request("api/friend/loadexistingfriends", "GET",this.getActivity().getSharedPreferences("global",Context.MODE_PRIVATE).getString("Token","None"), null, new RequestCallback() {
            public void call(Response response) {
                if(response.statusCode == StatusCode.OK) {
                    try {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.friends,SectionDividerFragment.newInstance(getContext().getString(R.string.friends_normal_title)));

                        JSONArray array = new JSONArray(response.data.toString());
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            UserPublic user = new UserPublic(obj);

                            FriendFragment newFragment = FriendFragment.newInstance(user,FriendFragment.Type.NORMAL);

                            ft.add(R.id.friends, newFragment);
                        }

                        ft.commit();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        //debugCrtGroup();
                    }
                }else {
                    //debugCrtGroup();
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

    private void loadPendingFriends() {
        new RequestManager().execute(new Request("api/friend/loadpending", "GET",this.getActivity().getSharedPreferences("global",Context.MODE_PRIVATE).getString("Token","None"), null, new RequestCallback() {
            public void call(Response response) {
                if(response.statusCode == StatusCode.OK) {
                    try {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.friends,SectionDividerFragment.newInstance(getContext().getString(R.string.friends_requests_title)));

                        JSONArray array = new JSONArray(response.data.toString());
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            UserPublic user = new UserPublic(obj);

                            FriendFragment newFragment = FriendFragment.newInstance(user,FriendFragment.Type.REQUEST);

                            ft.add(R.id.friends, newFragment);
                        }

                        ft.commit();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        //debugCrtGroup();
                    }
                }else {
                    //debugCrtGroup();
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
    }
}
