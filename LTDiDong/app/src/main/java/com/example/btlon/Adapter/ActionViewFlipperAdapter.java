package com.example.btlon.Adapter;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.btlon.R;

import java.util.List;

public class ActionViewFlipperAdapter {
    public void ActionViewFlipper(Context context, ViewFlipper viewFlipper, List<String> advertisementList) {
        if (advertisementList == null || advertisementList.isEmpty()) {
            return;
        }
        for (String url : advertisementList) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
}
