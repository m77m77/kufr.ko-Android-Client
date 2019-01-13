package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ko.kufr.m77m77.chatandroidclient.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GroupsFragment groups;
    private FriendsFragment friends;

    private boolean inChats = true;

    private OnFragmentInteractionListener mListener;

    public IndexFragment() {
        // Required empty public constructor
        this.groups = new GroupsFragment();
        this.friends = new FriendsFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final FragmentManager man = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = man.beginTransaction();

        transaction.add(R.id.index_fragmentContainer,this.groups);
        transaction.add(R.id.index_fragmentContainer,this.friends);
        transaction.hide(this.friends);
        transaction.commit();

        final View chats = view.findViewById(R.id.index_chats);
        final View people = view.findViewById(R.id.index_people);

        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inChats) {
                    FragmentTransaction trn = man.beginTransaction();
                    trn.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                    //trn.replace(R.id.index_fragmentContainer, groups);
                    trn.show(groups);
                    trn.hide(friends);
                    trn.commit();


                    chats.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selected));
                    people.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));

                    inChats = true;
                }
            }
        });

        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inChats) {
                    FragmentTransaction trn = man.beginTransaction();
                    trn.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                    //trn.replace(R.id.index_fragmentContainer, group);
                    trn.hide(groups);
                    trn.show(friends);
                    trn.commit();


                    chats.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                    people.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.selected));

                    inChats = false;
                }
            }
        });
    }


    public void refresh() {
        this.groups.refresh();
        this.friends.refresh();
    }

    public void setNewMessages(int num) {
        TextView view = this.getView().findViewById(R.id.index_chats_newMessages);

        if(num <= 0) {
            view.setVisibility(View.GONE);
        }else {
            view.setText(num + "");
            view.setVisibility(View.VISIBLE);
        }
    }

    public void setNewRequests(int num) {
        TextView view = this.getView().findViewById(R.id.index_people_newRequests);

        if(num <= 0) {
            view.setVisibility(View.GONE);
        }else {
            view.setText(num + "");
            view.setVisibility(View.VISIBLE);
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
