package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
import ko.kufr.m77m77.chatandroidclient.models.group.GroupDetailInfo;
import ko.kufr.m77m77.chatandroidclient.models.group.GroupInfo;
import ko.kufr.m77m77.chatandroidclient.models.message.Message;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "paramIdGroup";

    private long id_group;
    private LinkedList<Message> messages;
    private GroupDetailInfo group_info;

    private LinearLayout lay;
    private ScrollView scroll;
    private SwipeRefreshLayout swipe;
    private EditText messageField;

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(long id) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id_group = getArguments().getLong(ARG_ID);
        }

        this.messages = new LinkedList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.chat_include).findViewById(R.id.appbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.backFromChat();
                }
            }
        });

        this.lay = view.findViewById(R.id.chat_messages);
        this.scroll = view.findViewById(R.id.chat_messages_scroll);
        this.swipe = view.findViewById(R.id.chat_swipe_container);
        this.messageField = view.findViewById(R.id.chat_message_field);

        this.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOld();
            }
        });

        view.findViewById(R.id.chat_uploadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(intent,"Select a file"), 2);
            }
        });

        view.findViewById(R.id.chat_sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });

        //this.debugCrtMessage();
        swipe.setRefreshing(true);
        this.loadInfo();
        this.loadOld();
    }

    private void loadInfo() {
        this.mListener.sendRequest("api/group/getdetail?id=" + this.id_group,"GET",null, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                try {
                    JSONObject obj = new JSONObject(data.toString());

                    group_info = new GroupDetailInfo(obj);

                    loadInfoDone();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadInfoDone() {
        ((TextView) this.getView().findViewById(R.id.chat_include).findViewById(R.id.appbar_name)).setText(this.group_info.displayName);
    }

    private void sendMessage() {
        String text = this.messageField.getText().toString();

        if(!text.isEmpty()) {
            this.mListener.sendRequest("api/message/sendmessage" ,"POST","Id_Group=" + this.id_group /*+ "&Id_Attachment[0]=1"*/ + "&Text=" + text, new RequestDataCallback() {
                @Override
                public void call(Object data) {
                    messageField.setText("");
                    loadNew();
                    if (mListener != null) {
                        mListener.refreshIndex();
                    }
                }
            });
        }
    }

    public void loadOld() {
        long id = 0;

        if(!this.messages.isEmpty()) {
            id = this.messages.getLast().id;
        }

        //TODO Add settings for amount
        this.loadMessages(id,20);
    }

    public void loadNew() {
        if(!this.messages.isEmpty())
        this.loadNewMessages(this.messages.getFirst().id);
    }

    private Message[] readToArray(JSONArray array) throws JSONException,ParseException {
        Message[] newMessages = new Message[array.length()];

        for(int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            newMessages[i] = new Message(obj);
        }

        return newMessages;
    }

    private void loadMessages(long lastId,int amount) {
        this.mListener.sendRequest("api/message/getmessages" ,"POST","Id_Group=" + this.id_group + "&StartId=" + lastId + "&Amount=" + amount, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                try {
                    JSONArray array = new JSONArray(data.toString());
                    addOldMessages(readToArray(array));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadNewMessages(long lastId) {
        this.mListener.sendRequest("api/message/getnewmessages" ,"POST","Groups=" + this.id_group + "&Id_Last=" + lastId, new RequestDataCallback() {
            @Override
            public void call(Object data) {
                try {
                    JSONArray array = new JSONArray(data.toString());
                    addNewMessages(readToArray(array));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addOldMessages(Message[] msgs) {
        boolean first = this.messages.isEmpty();
        final int prevHeight = this.lay.getHeight();
        for (Message msg : msgs) {
            View msgView = msg.getView(this.getContext(),this.lay);

            if(!this.messages.isEmpty()) {
                msg.setFirst(true);
                if(this.messages.getLast().sender_id == msg.sender_id) {
                    this.messages.getLast().setFirst(false);
                    msg.setLast(false);
                    if((this.messages.getLast().sent.getTime() - msg.sent.getTime()) / 1000l / 60l < 5l) {
                        msg.setDate(false);
                    }else {
                        msg.setDate(true);
                    }
                }else {
                    msg.setLast(true);
                }
            }

            this.lay.addView(msgView,0);
            this.messages.addLast(msg);
        }

        if(first)
            this.scrollToBottom();
        else
            this.lay.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    lay.removeOnLayoutChangeListener(this);
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.scrollTo(0,lay.getHeight() - prevHeight);
                        }
                    });
                }
            });

        swipe.setRefreshing(false);
    }

    private void addNewMessages(Message[] msgs) {
        for (Message msg : msgs) {
            View msgView = msg.getView(this.getContext(),this.lay);
            this.lay.addView(msgView);

            if(!this.messages.isEmpty()) {
                msg.setLast(true);
                if(this.messages.getFirst().sender_id == msg.sender_id) {
                    this.messages.getFirst().setLast(false);
                    msg.setFirst(false);
                    if((msg.sent.getTime() - this.messages.getFirst().sent.getTime()) / 1000l / 60l < 5l) {
                        this.messages.getFirst().setDate(false);
                    }else {
                        this.messages.getFirst().setDate(true);
                    }
                }else {
                    msg.setFirst(true);
                }
            }

            this.messages.addFirst(msg);
        }

        scrollToBottom();
    }

    private void scrollToBottom() {
        this.scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void debugCrtMessage() {
        Log.d("Debug","messages loaded.");
        //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        for(int i = 0; i < 10; i++){

            //ft.add(R.id.chat_messages, messageFrag);
            Message mess = new Message();
            mess.sender_nickname = i % 2 == 0 ? "Nickname #" + i : null;
            mess.sender_name = "Username #" + i;
            mess.text = "Example message. Example message. Example message. #" + i;
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH,-i);
            mess.sent = now.getTime();

            LinearLayout lin = this.getView().findViewById(R.id.chat_messages);

            lin.addView(mess.getView(this.getContext(),lin),0);

        }
        //ft.commit();
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
        void backFromChat();
        void refreshIndex();
        void sendRequest(String url, String method, String data, final RequestDataCallback callback);
    }
}
