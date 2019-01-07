package ko.kufr.m77m77.chatandroidclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ko.kufr.m77m77.chatandroidclient.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionDividerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionDividerFragment extends Fragment {

    private static final String ARG_TITLE = "paramTitle";

    private String title;

    public SectionDividerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @return A new instance of fragment SectionDividerFragment.
     */

    public static SectionDividerFragment newInstance(String title) {
        SectionDividerFragment fragment = new SectionDividerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_section_divider, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(this.title != null) {
            ((TextView)view.findViewById(R.id.section_divider_title)).setText(this.title);
        }
    }
}
