package com.example.gss_mac.photofilter.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gss_mac.photofilter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FramesFragment extends Fragment {


    public FramesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frames, container, false);
    }

}
