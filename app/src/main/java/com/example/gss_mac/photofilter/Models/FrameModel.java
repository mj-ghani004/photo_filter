package com.example.gss_mac.photofilter.Models;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;

public class FrameModel {

    private String FrameName;
    private Drawable FrameId;

    public FrameModel(String frameName, Drawable frameId) {
        FrameName = frameName;
        FrameId = frameId;
    }

    public String getFrameName() {
        return FrameName;
    }

    public Drawable getFrameId() {
        return FrameId;
    }
}
