package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.RequestDataCallback;
import ko.kufr.m77m77.chatandroidclient.models.user.UserPublic;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    public enum Type {
        REQUEST,
        NORMAL,
        BLOCK,
        NEW
    }

    private static final String ARG_USER = "paramUser";
    private static final String ARG_TYPE = "paramType";


    private UserPublic userInfo;
    private Type type;

    private OnFragmentInteractionListener mListener;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @param type Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(UserPublic user, Type type) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER,user);
        args.putSerializable(ARG_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.userInfo = (UserPublic) getArguments().getSerializable(ARG_USER);
            this.type = (Type) getArguments().getSerializable(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(this.userInfo != null && this.type != null) {
            ((TextView)view.findViewById(R.id.friend_name)).setText(this.userInfo.name);

            if(this.type == Type.REQUEST) {
                view.findViewById(R.id.friend_more).setVisibility(View.GONE);
                view.findViewById(R.id.friend_add).setVisibility(View.GONE);
                view.findViewById(R.id.friend_container).setClickable(false);
                view.findViewById(R.id.friend_accept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptFriend();
                    }
                });
                view.findViewById(R.id.friend_deny).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFriend();
                    }
                });
                view.findViewById(R.id.friend_block).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blockFriend();
                    }
                });
            }else if(this.type == Type.NORMAL) {
                view.findViewById(R.id.friend_add).setVisibility(View.GONE);
                view.findViewById(R.id.friend_accept).setVisibility(View.GONE);
                view.findViewById(R.id.friend_deny).setVisibility(View.GONE);
                view.findViewById(R.id.friend_block).setVisibility(View.GONE);
                view.findViewById(R.id.friend_container).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.openChatGroup(userInfo.id_group);
                    }
                });
                view.findViewById(R.id.friend_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupMenu(v);
                    }
                });
            }else if(this.type == Type.NEW) {
                view.findViewById(R.id.friend_more).setVisibility(View.GONE);
                view.findViewById(R.id.friend_accept).setVisibility(View.GONE);
                view.findViewById(R.id.friend_deny).setVisibility(View.GONE);
                view.findViewById(R.id.friend_block).setVisibility(View.GONE);
                view.findViewById(R.id.friend_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriend();
                    }
                });
            }
        }
    }

    public String getUserName() {
        return this.userInfo.name;
    }

    private void showPopupMenu(View view) {
        PopupMenu menu = new PopupMenu(this.getContext(),view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return handleMenuClicks(item);
            }
        });
        MenuInflater inflater = menu.getMenuInflater();
        inflater.inflate(R.menu.menu_existing_friend,menu.getMenu());
        menu.show();

    }

    private boolean handleMenuClicks(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ex_fr_delete:
                removeFriend();
                return true;
            case R.id.menu_ex_fr_block:
                blockFriend();
                return true;
            default:
                return false;
        }
    }

    private void acceptFriend() {
        //mListener.sendRequest("api/friend/");
        this.mListener.sendRequest("api/friend/acceptfriend","PATCH","User_id=" + this.userInfo.id, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                mListener.refreshIndex();
            }
        });
    }

    private void removeFriend() {
        this.mListener.sendRequest("api/friend/removefriend","DELETE","User_id=" + this.userInfo.id, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                mListener.refreshIndex();
            }
        });
    }

    private void blockFriend() {
        this.mListener.sendRequest("api/friend/blockfriend","PATCH","User_id=" + this.userInfo.id, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                mListener.refreshIndex();
            }
        });
    }

    private void addFriend() {
        this.mListener.sendRequest("api/friend/createfriendrequest","POST","User_id=" + this.userInfo.id, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                mListener.refreshIndex();
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
        void openChatGroup(long id);
        void refreshIndex();
        void sendRequest(String url, String method, String data, final RequestDataCallback callback);
    }
}
