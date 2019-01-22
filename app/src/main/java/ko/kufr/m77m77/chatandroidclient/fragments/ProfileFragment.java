package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Set;

import ko.kufr.m77m77.chatandroidclient.R;
import ko.kufr.m77m77.chatandroidclient.models.enums.SettingType;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.profile_include).findViewById(R.id.appbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backFromProfile();
            }
        });

        generateLayout();
    }

    private void generateLayout() {
        LinearLayout lay = this.getView().findViewById(R.id.profile_linear);
        lay.addView(crtDivider(lay,"Preferences"));

        lay.addView(crtSetting(lay,"Username",SettingType.TEXT,null));
        lay.addView(crtSetting(lay,"Password",SettingType.TEXT,null));
        lay.addView(crtSetting(lay,"Visibility",SettingType.TEXT,null));
        lay.addView(crtSetting(lay,"Profile picture",SettingType.TEXT,null));

        lay.addView(crtDivider(lay,"Account"));

        lay.addView(crtSetting(lay, "Log out", SettingType.BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logout();
            }
        }));
        lay.addView(crtSetting(lay, "Delete profile", SettingType.BUTTON, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
    }

    private View crtSetting(LinearLayout lay, String label, SettingType type, View.OnClickListener onClick) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.fragment_setting,lay,false);

        view.findViewById(R.id.setting_accept).setVisibility(View.GONE);
        view.findViewById(R.id.setting_deny).setVisibility(View.GONE);
        view.findViewById(R.id.setting_field).setVisibility(View.GONE);

        ((TextView)view.findViewById(R.id.setting_label)).setText(label);

        if(type == SettingType.BUTTON) {
            view.findViewById(R.id.setting_edit).setVisibility(View.INVISIBLE);
            view.setOnClickListener(onClick);
        }else {
            view.setClickable(false);
        }

        return view;
    }

    private View crtDivider(LinearLayout lay,String label) {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.fragment_section_divider,lay,false);
        ((TextView)view.findViewById(R.id.section_divider_title)).setText(label);

        return view;
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
        void backFromProfile();
        void logout();
    }
}
