package com.example.gss_mac.photofilter.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.gss_mac.photofilter.R;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private  SeekBar mBrightness;
    private  SeekBar mContrast;
    private  SeekBar mSaturation;

    private Boolean RESET = false;

    //Interface Setup

    EditImageFragmentListener listener;

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public SettingFragment() {
        // Required empty public constructor
    }

    public void resetSeekbars(boolean value) {

        RESET = value;
//        mBrightness.setMax(140);
//        mBrightness.setProgress(70);
//
//        // keeping contrast value b/w 1.0 - 3.0
//        mContrast.setMax(20);
//        mContrast.setProgress(0);
//
//        // keeping saturation value b/w 0.0 - 3.0
//        mSaturation.setMax(30);
//        mSaturation.setProgress(10);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RESET) {

            Toast.makeText(getActivity(), "Resume", Toast.LENGTH_LONG).show();
//
//            mBrightness.setMax(510);
//            mBrightness.setProgress(255);

            mBrightness.setMax(140);
            mBrightness.setProgress(70);

            // keeping contrast value b/w 1.0 - 3.0
            mContrast.setMax(20);
            mContrast.setProgress(0);

            // keeping saturation value b/w 0.0 - 3.0
            mSaturation.setMax(30);
            mSaturation.setProgress(10);

            RESET = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mBrightness = view.findViewById(R.id.seekbar_brightness);
        mContrast = view.findViewById(R.id.seekbar_contrast);
        mSaturation = view.findViewById(R.id.seekbar_saturation);

        // keeping brightness value b/w -100 / +100


        mBrightness.setMax(140);
        mBrightness.setProgress(70);

        // keeping contrast value b/w 1.0 - 3.0
        mContrast.setMax(20);
        mContrast.setProgress(0);

        // keeping saturation value b/w 0.0 - 3.0
        mSaturation.setMax(30);
        mSaturation.setProgress(10);

        mBrightness.setOnSeekBarChangeListener(this);
        mContrast.setOnSeekBarChangeListener(this);
        mSaturation.setOnSeekBarChangeListener(this);


//        RESET = false ;
        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (listener != null) {

            if (seekBar.getId() == R.id.seekbar_brightness) {
                // brightness values are b/w -100 to +100

//                listener.onBrightnessChanged(progress-brightnessprogress.intValue());
                listener.onBrightnessChanged(progress - 70);
            }

            if (seekBar.getId() == R.id.seekbar_contrast) {
                // converting int value to float
                // contrast values are b/w 1.0f - 3.0f
                // progress = progress > 10 ? progress : 10;
                progress += 10;
                float floatVal = .10f * progress;
                listener.onContrastChanged(floatVal);
            }

            if (seekBar.getId() == R.id.seekbar_saturation) {
                // converting int value to float
                // saturation values are b/w 0.0f - 3.0f
                float floatVal = .10f * progress;
                listener.onSaturationChanged(floatVal);
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if (listener != null) {
            listener.onEditStarted();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (listener != null) {
            listener.onEditCompleted();
        }
    }


    public interface EditImageFragmentListener {

        void onBrightnessChanged(int brightness);

        void onSaturationChanged(float saturation);

        void onContrastChanged(float contrast);

        void onEditStarted();

        void onEditCompleted();
    }

    @Override
    public void onViewStateRestored(Bundle inState) {
        //...
        super.onViewStateRestored(inState);
//        mBrightness.setMax(510);
//        mBrightness.setProgress(255);
//
//        // keeping contrast value b/w 1.0 - 3.0
//        mContrast.setMax(20);
//        mContrast.setProgress(0);
//
//        // keeping saturation value b/w 0.0 - 3.0
//        mSaturation.setMax(30);
//        mSaturation.setProgress(10);
        //...
    }

}
