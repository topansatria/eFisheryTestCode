package com.efhiserytestcode.view.dialog.filter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.efhiserytestcode.R;
import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.SizeModel;
import com.efhiserytestcode.view.dialog.sorting.DialogSortAdapter;
import com.efhiserytestcode.view.dialog.sorting.DialogSorting;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DialogFilter extends Dialog implements View.OnClickListener {

    private Activity mActivity;
    private TextView tvTitle;
    private ImageView ivClose;
    private LinearLayout llProvinsi, llKota;
    private RadioGroup rgOption;
    private RadioButton rbProvinsi, rbKota;
    private Spinner spinProvinsi, spinKota;
    private View compBtn;
    private Button btnFilter;
    private dialogCallback callback;

    private List<AreaModel> areaModels = new ArrayList<>();
    private ArrayList<String> provinceList = new ArrayList<String>();
    private ArrayList<String> cityList = new ArrayList<String>();
    private boolean getProvince = false, getCity = false;

    public DialogFilter(Activity mActivity, List<AreaModel> areaModels) {
        super(mActivity);
        this.mActivity = mActivity;
        this.areaModels = areaModels;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.comp_menu_filtering);

        callback = (dialogCallback) mActivity;

        init();

        tvTitle.setText(mActivity.getString(R.string.text_title_filter));
        btnFilter.setText(mActivity.getString(R.string.btn_filter));
        llProvinsi.setVisibility(View.GONE);
        llKota.setVisibility(View.GONE);

        rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.rbProvinsi:
                        llProvinsi.setVisibility(View.VISIBLE);
                        llKota.setVisibility(View.GONE);

                        getProvinceSpinner();
                        break;
                    case R.id.rbKota:
                        llProvinsi.setVisibility(View.GONE);
                        llKota.setVisibility(View.VISIBLE);

                        getCitySpinner();
                        break;
                }
            }
        });

        ivClose.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        ivClose = findViewById(R.id.ivClose);

        llProvinsi = findViewById(R.id.llProvinsi);
        llKota = findViewById(R.id.llKota);
        rgOption = findViewById(R.id.rgOption);
        rbProvinsi = findViewById(R.id.rbProvinsi);
        rbKota = findViewById(R.id.rbKota);
        spinProvinsi = findViewById(R.id.spinProvinsi);
        spinKota = findViewById(R.id.spinKota);

        compBtn = findViewById(R.id.compBtn);
        btnFilter = compBtn.findViewById(R.id.btnSmall);
    }

    private void getProvinceSpinner() {
        // Province
        for (int i = 0; i < areaModels.size(); i++) {
            String province = areaModels.get(i).getProvince();

            for (int j = 0; j < provinceList.size(); j++) {
                if (province.equals(provinceList.get(j))) {
                    provinceList.remove(j);
                }
            }

            provinceList.add(province);
        }

        provinceList.add(0, "Pilih Provinsi");
        final ArrayAdapter<String> spinnerProvince = new ArrayAdapter<String>(
                mActivity, R.layout.item_spinner_default, provinceList){
            @Override
            public boolean isEnabled(int position){
                return true;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tvTitle = view.findViewById(R.id.tvTitle);

                if(position == 0){
                    // Set the hint text color gray
                    tvTitle.setTextColor(Color.GRAY);
                }
                else {
                    tvTitle.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerProvince.setDropDownViewResource(R.layout.item_spinner_default);
        spinProvinsi.setAdapter(spinnerProvince);

        spinProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getItem = String.valueOf(parent.getItemAtPosition(position));
                getProvince = true;
                getCity = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getProvince = false;
                getCity = false;
            }
        });

    }

    private void getCitySpinner() {
        // Province
        for (int i = 0; i < areaModels.size(); i++) {
            String city = areaModels.get(i).getCity();

            for (int j = 0; j < cityList.size(); j++) {
                if (city.equals(cityList.get(j))) {
                    cityList.remove(j);
                }
            }

            cityList.add(city);
        }

        // City
        cityList.add(0, "Pilih Kota");
        final ArrayAdapter<String> spinnerCity = new ArrayAdapter<String>(
                mActivity, R.layout.item_spinner_default, cityList){
            @Override
            public boolean isEnabled(int position){
                return true;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tvTitle = view.findViewById(R.id.tvTitle);

                if(position == 0){
                    // Set the hint text color gray
                    tvTitle.setTextColor(Color.GRAY);
                }
                else {
                    tvTitle.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerCity.setDropDownViewResource(R.layout.item_spinner_default);
        spinKota.setAdapter(spinnerCity);

        spinKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getItem = String.valueOf(parent.getItemAtPosition(position));
                getCity = true;
                getProvince = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getCity = false;
                getProvince = false;
            }
        });

    }

    public interface dialogCallback {
        void onValuesSetFiltering(String keyFiltering, String valFiltering);
    }

    @Override
    public void onClick(View view) {
        if (view == ivClose) {
            dismiss();
        } else if (view == btnFilter) {
            if (getProvince) {

                String valProvince = spinProvinsi.getSelectedItem().toString();

                if (!valProvince.equals("Pilih Provinsi")) {
                    callback.onValuesSetFiltering("area_provinsi", valProvince);
                    dismiss();
                } else {
                    Toast.makeText(mActivity, "Pilih Provinsi!", Toast.LENGTH_LONG).show();
                }


            } else if (getCity) {

                String valCity = spinKota.getSelectedItem().toString();

                if (!valCity.equals("Pilih Kota")) {
                    callback.onValuesSetFiltering("area_kota", valCity);
                    dismiss();
                } else {
                    Toast.makeText(mActivity, "Pilih Provinsi!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
