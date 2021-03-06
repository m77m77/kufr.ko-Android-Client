package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.RequestDataCallback;
import ko.kufr.m77m77.chatandroidclient.models.group.GroupInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private  int newMessages;
    private  GroupFragment[] groups;
    private  GroupFragment[] newGroups;

    private OnFragmentInteractionListener mListener;

    public GroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
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

        //this.debugCrtGroup();
        //this.loadGroups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //this.debugCrtGroup();
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SwipeRefreshLayout refreshView = view.findViewById(R.id.groups_swipe_container);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        ((SearchView) view.findViewById(R.id.groups_search)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch();
                return false;
            }
        });

        view.findViewById(R.id.groups_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGroup();
            }
        });

        refreshView.setRefreshing(true);
        this.refresh();
    }

    private void debugCrtGroup() {
        Log.d("Debug","groups loaded.");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        for(int i = 0; i < 10; i++){
            GroupInfo info = new GroupInfo();
            info.name = "Group #" + i;
            info.lastMessageSender = "User #" + i;
            info.lastMessageText = "Message #" + i;
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH,-i);
            info.lastMessageDate = now.getTime();
            info.newMessages = i % 2 == 0 ? i * 7 : 0;

            GroupFragment newFragment = GroupFragment.newInstance(info);

            ft.add(R.id.groups, newFragment);

        }
        ft.commit();
    }

    private void onSearch() {
        String query = ((SearchView)this.getView().findViewById(R.id.groups_search)).getQuery().toString();

        if(query == null)
            return;

        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();

        for (GroupFragment frag : this.groups) {
            if(frag.getGroupName() == null)
                continue;

            if(frag.getGroupName().toLowerCase().contains(query.toLowerCase())) {
                ft.show(frag);
            }else {
                ft.hide(frag);
            }
        }

        ft.commit();
    }

    public void refresh() {
        this.newMessages = 0;

        this.loadGroups();
    }

    private void refreshDone() {
        FragmentTransaction ftRem = this.getActivity().getSupportFragmentManager().beginTransaction();

        if(this.groups != null)
        for (GroupFragment frag:this.groups) {
            ftRem.remove(frag);
        }

        ftRem.commit();

        this.groups = this.newGroups;

        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();

        for (GroupFragment frag : this.groups) {
            ft.add(R.id.groups, frag);
        }

        ft.commit();

        ((SearchView)this.getView().findViewById(R.id.groups_search)).setQuery("",false);
        ((SwipeRefreshLayout) getView().findViewById(R.id.groups_swipe_container)).setRefreshing(false);

        if(mListener != null) {
            mListener.setNewMessages(this.newMessages);
        }
    }

    private void loadGroups() {

        this.mListener.sendRequest("api/group/getall","GET",null, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                try {
                    JSONArray array = new JSONArray(data.toString());

                    newGroups = new GroupFragment[array.length()];

                    for(int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        GroupInfo info = new GroupInfo(obj);

                        newMessages+= info.newMessages;

                        GroupFragment newFragment = GroupFragment.newInstance(info);

                        newGroups[i] = newFragment;
                    }

                    refreshDone();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createNewGroup() {
        this.mListener.sendRequest("api/group/create","POST",null, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                mListener.openChatGroup(Long.parseLong(data.toString()));
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
        void setNewMessages(int num);
        void sendRequest(String url, String method, String data, final RequestDataCallback callback);
        void openChatGroup(long id);
        void refreshIndex();
    }
}
