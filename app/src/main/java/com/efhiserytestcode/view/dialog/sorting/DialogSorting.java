package com.efhiserytestcode.view.dialog.sorting;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.efhiserytestcode.R;
import com.efhiserytestcode.model.MenuModel;

import org.json.JSONObject;

import java.util.ArrayList;

public class DialogSorting extends Dialog implements DialogSortAdapter.ClickListener, View.OnClickListener {

    private Activity mActivity;
    private ArrayList<MenuModel> mList = new ArrayList<>();
    private DialogSortAdapter adapter;
    private dialogCallback callback;

    private TextView tvTitle;
    private ImageView ivClose;
    private RecyclerView rvMenu;

    private RecyclerView.LayoutManager managerSorting;
    private JSONObject keyword;

    public DialogSorting(@NonNull Activity mActivity, JSONObject keyword) {
        super(mActivity);
        this.mActivity = mActivity;
        this.keyword = keyword;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.comp_menu_sorting);

        callback = (dialogCallback) mActivity;

        init();
        setListMenu();

        tvTitle.setText(mActivity.getString(R.string.text_title_sorting));
        adapter.setOnItemClickListener(this);
        ivClose.setOnClickListener(this);
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);
        rvMenu = findViewById(R.id.rvMenu);

        mList = new ArrayList<>();
        adapter = new DialogSortAdapter(getContext(), mList);
        managerSorting = new LinearLayoutManager(mActivity);
    }

    private void setListMenu() {
        fillList();
        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(managerSorting);
        rvMenu.setAdapter(adapter);
    }

    private void fillList() {
        mList.add(new MenuModel(mActivity.getString(R.string.text_highest_price)));
        mList.add(new MenuModel(mActivity.getString(R.string.text_lowest_price)));
        mList.add(new MenuModel(mActivity.getString(R.string.text_highest_size)));
        mList.add(new MenuModel(mActivity.getString(R.string.text_lowest_size)));
        mList.add(new MenuModel(mActivity.getString(R.string.text_newest)));
        mList.add(new MenuModel(mActivity.getString(R.string.text_oldest)));
    }

    public interface dialogCallback {
        void onValuesSetSorting(String type, String sortPrice, String sortSize, String sortDate);
    }

    @Override
    public void onItemClick(int position, View v) {
        if (position == 0){
            callback.onValuesSetSorting("price", "desc", "", "");
            dismiss();
        } else if (position == 1){
            callback.onValuesSetSorting("price", "asc", "", "");
            dismiss();
        } else if (position == 2){
            callback.onValuesSetSorting("size", "", "desc", "");
            dismiss();
        } else if (position == 3){
            callback.onValuesSetSorting("size", "", "asc", "");
            dismiss();
        } else if (position == 4){
            callback.onValuesSetSorting("date", "", "", "desc");
            dismiss();
        } else if (position == 5){
            callback.onValuesSetSorting("date", "", "", "asc");
            dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ivClose) {
            dismiss();
        }
    }
}
