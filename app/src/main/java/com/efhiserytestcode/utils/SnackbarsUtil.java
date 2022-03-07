package com.efhiserytestcode.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.efhiserytestcode.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarsUtil {

    private Activity activity;
    private Snackbar snackbars;
    private View layoutInflater;
    private TextView tvDescription;

    public SnackbarsUtil(Activity activity) {
        this.activity = activity;
    }

    public void onSuccess(String message){
        snackbars = Snackbar.make(activity.findViewById(android.R.id.content), " ", Snackbar.LENGTH_LONG);
        layoutInflater = activity.getLayoutInflater().inflate(R.layout.comp_snackbars_success, null);
        init();

        snackbars.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbars.getView();
        snackBarView.setPadding(0, 0, 0, 200);

        tvDescription.setText(message);

        snackBarView.addView(layoutInflater, 0);
        snackbars.show();
    }

    public void onFailed(String message) {
        snackbars = Snackbar.make(activity.findViewById(android.R.id.content), " ", Snackbar.LENGTH_LONG);
        layoutInflater = activity.getLayoutInflater().inflate(R.layout.comp_snackbars_failed, null);
        init();

        snackbars.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbars.getView();
        snackBarView.setPadding(0, 0, 0, 200);

        tvDescription.setText(message);

        snackBarView.addView(layoutInflater, 0);
        snackbars.show();
    }

    private void init() {
        tvDescription = layoutInflater.findViewById(R.id.tvDescription);
    }

}
