package com.efhiserytestcode.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.efhiserytestcode.R;
import com.kaopiz.kprogresshud.KProgressHUD;

public class LoadingPageUtil {

    private KProgressHUD loading;
    private Context mContext;

    public LoadingPageUtil(Context context) {
        this.mContext = context;

        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.comp_spin_animation);
        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
        drawable.start();

        loading = KProgressHUD.create(mContext)
                .setCustomView(imageView)
                .setBackgroundColor(Color.TRANSPARENT)
                .setCancellable(false)
                .setAnimationSpeed(10)
                .setDimAmount(0.5f);
    }

    public void start(){
        loading.show();
    }

    public void stop(){
        loading.dismiss();
    }

}
