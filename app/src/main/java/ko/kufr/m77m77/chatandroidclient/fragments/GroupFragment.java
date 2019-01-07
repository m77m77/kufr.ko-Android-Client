package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.models.group.GroupInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GROUP = "paramGroup";

    // TODO: Rename and change types of parameters
    private GroupInfo info;

    private OnFragmentInteractionListener mListener;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param info Parameter 1.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(GroupInfo info) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP,info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            info = (GroupInfo) getArguments().getSerializable(ARG_GROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(this.info != null) {
            TextView groupName = view.findViewById(R.id.group_name);
            groupName.setText(this.info.name);

            TextView groupLastMessage = (TextView) view.findViewById(R.id.group_lastMessage);
            groupLastMessage.setText((this.info.lastMessageSender != null ? this.info.lastMessageSender + ": " : "") + (this.info.lastMessageText != null ? this.info.lastMessageText : ""));

            String date = "";

            if(this.info.lastMessageDate != null) {
                Calendar now = Calendar.getInstance();
                Calendar lmDate = Calendar.getInstance();
                lmDate.setTime(this.info.lastMessageDate);

                SimpleDateFormat format;

                if(now.get(Calendar.YEAR) != lmDate.get(Calendar.YEAR)) {
                    format = new SimpleDateFormat("dd.MM.yyyy");

                }else if(now.get(Calendar.DAY_OF_YEAR) != lmDate.get(Calendar.DAY_OF_YEAR)) {
                    format = new SimpleDateFormat("dd.MM");
                }else {
                    format = new SimpleDateFormat("HH:mm");
                }

                date = format.format(lmDate.getTime());
            }

            ((TextView) view.findViewById(R.id.group_lastMessageDate)).setText(date);

            if(this.info.newMessages > 0) {
                TextView groupNewMessages = (TextView) view.findViewById(R.id.group_newMessages);
                groupNewMessages.setText(this.info.newMessages + "");
                groupNewMessages.setVisibility(View.VISIBLE);
                groupName.setTypeface(null,Typeface.BOLD);
                groupLastMessage.setTypeface(null,Typeface.BOLD);
                groupLastMessage.setTextColor(ContextCompat.getColor(this.getContext(),R.color.buttonBackground));
            }

            view.findViewById(R.id.group_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(v);
                }
            });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(view);
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
        void onFragmentInteraction(View view);
    }
}
