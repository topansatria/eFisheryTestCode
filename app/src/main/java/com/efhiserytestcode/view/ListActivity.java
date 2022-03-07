package com.efhiserytestcode.view;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.efhiserytestcode.R;
import com.efhiserytestcode.adapter.ListAdapter;
import com.efhiserytestcode.database.list.ListDBLite;
import com.efhiserytestcode.database.optionArea.OptionAreaDBLite;
import com.efhiserytestcode.database.optionSize.OptionSizeDBLite;
import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.ListModel;
import com.efhiserytestcode.model.SizeModel;
import com.efhiserytestcode.network.request.add.AddRequest;
import com.efhiserytestcode.network.request.add.iViewAdd;
import com.efhiserytestcode.network.request.list.ListRequest;
import com.efhiserytestcode.network.request.list.iViewList;
import com.efhiserytestcode.network.request.optionArea.AreaRequest;
import com.efhiserytestcode.network.request.optionArea.iViewArea;
import com.efhiserytestcode.network.request.optionSize.SizeRequest;
import com.efhiserytestcode.network.request.optionSize.iViewSize;
import com.efhiserytestcode.utils.LoadingPageUtil;
import com.efhiserytestcode.utils.SnackbarsUtil;
import com.efhiserytestcode.utils.SoftKeyboardUtil;
import com.efhiserytestcode.view.dialog.filter.DialogFilter;
import com.efhiserytestcode.view.dialog.sorting.DialogSorting;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.efhiserytestcode.helper.DateHelper.fromFormat1;
import static com.efhiserytestcode.helper.DateHelper.getDateNow;
import static com.efhiserytestcode.helper.NumberHelper.isNumeric;

public class ListActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        iViewList, iViewAdd, iViewArea, iViewSize, DialogSorting.dialogCallback, DialogFilter.dialogCallback {

    private View appBar;
    private ImageView ivSorting, ivFilter;
    private EditText etSearch;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout llContent, llResetData;
    private RelativeLayout rlMessage;
    private RecyclerView rvList;
    private FloatingActionButton fabAdd;
    private LoadingPageUtil loadingPageUtil;
    private Spinner spinProvinsi, spinKota, spinSize;
    private TextInputLayout tilKomoditas, tilPrice;

    private ListAdapter listAdapter;
    private List<ListModel> listModels = new ArrayList<>();
    private List<AreaModel> areaModels = new ArrayList<>();
    private List<SizeModel> sizeModels = new ArrayList<>();
    private ArrayList<String> provinceList = new ArrayList<String>();
    private ArrayList<String> cityList = new ArrayList<String>();
    private ArrayList<String> sizeList = new ArrayList<String>();

    private ListRequest listRequest;
    private AreaRequest areaRequest;
    private SizeRequest sizeRequest;

    private boolean isSearching = false, isSorting = false, isFiltering = false, isConnect = false;
    private String typeSort = "", sortPrice = "", sortSize = "", sortDate = "", keywordList = "", keyFiltering = "";
    private JSONObject searchList = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        loadingPageUtil = new LoadingPageUtil(this);
        llResetData.setVisibility(View.GONE);

        // Set Swipe Refresh
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(false);

        // Scheme colors for animation
        swipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.eFisheryDarkGreen),
                getResources().getColor(R.color.eFisheryLightGreen),
                getResources().getColor(R.color.eFisheryYellow),
                getResources().getColor(R.color.eFisheryYellowTeal)
        );

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        isSearching = true;
                        keywordList = etSearch.getText().toString();
                        searchList.put("komoditas", keywordList);
                        SoftKeyboardUtil.hide(ListActivity.this);

                        getData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        // OnClick
        fabAdd.setOnClickListener(this);
        llResetData.setOnClickListener(this);
        ivSorting.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
    }

    private void init() {
        appBar = findViewById(R.id.appBar);
        etSearch = appBar.findViewById(R.id.etSearch);
        ivSorting = appBar.findViewById(R.id.ivSorting);
        ivFilter = appBar.findViewById(R.id.ivFilter);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        llResetData = findViewById(R.id.llResetData);
        llContent = findViewById(R.id.llContent);
        rlMessage = findViewById(R.id.rlMessage);
        rvList = findViewById(R.id.rvList);

        fabAdd = findViewById(R.id.fabAdd);
    }

    // Get All Data
    private void getData() {
        getList();
        getArea();
        getSize();

        checkConnection();

        if (isConnect) {
            listRequest.getList(searchList);
            areaRequest.getArea();
            sizeRequest.getSize();
        } else {
            if (isFiltering) {
                listRequest.getListRoom(keyFiltering, keywordList);
            } else {
                listRequest.getListRoom("search", keywordList);
            }
            areaRequest.getAreaRoom();
            sizeRequest.getSizeRoom();
        }
    }

    // GET LIST
    private void getList() {
        loadingPageUtil.start();

        iViewList iView = this;
        listRequest = new ListRequest(this, iView);
    }

    @Override
    public void onSuccessList(List<ListModel> data) {
        ListDBLite dbLite = new ListDBLite(this);

        if (data.size() > 0) {
            listModels.clear();
            listModels = data;

            if (isConnect) {
                dbLite.deleteDB();
                dbLite.addDB(listModels);
            }

            if (isSorting) {
                sortingData();
            } else {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String date1 = mList1.getTglParsed();
                        String date2 = mList2.getTglParsed();

                        return (date2).compareTo(date1);
                    }
                });
            }

            listAdapter = new ListAdapter(this, listModels);
            rvList.setAdapter(listAdapter);
            rvList.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            rvList.setLayoutManager(mLayoutManager);

            llContent.setVisibility(View.VISIBLE);
            rlMessage.setVisibility(View.GONE);

        } else {
            dbLite.deleteDB();
            llContent.setVisibility(View.GONE);
            rlMessage.setVisibility(View.VISIBLE);
        }

        if (isSearching || isSorting || isFiltering) llResetData.setVisibility(View.VISIBLE);
        if (keywordList != null && !keywordList.isEmpty()) {
            etSearch.setText(keywordList);
        }
    }

    @Override
    public void onErrorList(int code, String message) {
        if (keywordList != null && !keywordList.isEmpty()) {
            etSearch.setText(keywordList);
        }

        SnackbarsUtil snackbarsUtil = new SnackbarsUtil(this);
        snackbarsUtil.onFailed("Terjadi Kesalahan Saat Memuat data!");

        loadingPageUtil.stop();
        swipeRefresh.setRefreshing(false);
    }

    private void sortingData() {
        // Sorting data from callback sorting dialog
        // Sorting price
        if (typeSort.equals("price")) {
            if (sortPrice.equals("desc")) {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String price1 = mList1.getPrice();
                        String price2 = mList2.getPrice();

                        if (price1.equals("") || !isNumeric(price1)) price1 = "0";
                        if (price2.equals("") || !isNumeric(price2)) price2 = "0";

                        Integer intPrice1 = Integer.valueOf(price1);
                        Integer intPrice2 = Integer.valueOf(price2);

                        return (intPrice2).compareTo(intPrice1);
                    }
                });
            } else if (sortPrice.equals("asc")) {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String price1 = mList1.getPrice();
                        String price2 = mList2.getPrice();

                        if (price1.equals("") || !isNumeric(price1)) price1 = "0";
                        if (price2.equals("") || !isNumeric(price2)) price2 = "0";

                        Integer intPrice1 = Integer.valueOf(price1);
                        Integer intPrice2 = Integer.valueOf(price2);

                        return (intPrice1).compareTo(intPrice2);
                    }
                });
            }
        }

        // Sorting size
        if (typeSort.equals("size")) {
            if (sortSize.equals("desc")) {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String size1 = mList1.getSize();
                        String size2 = mList2.getSize();

                        if (size1.equals("") || !isNumeric(size1)) size1 = "0";
                        if (size2.equals("") || !isNumeric(size2)) size2 = "0";

                        Integer intSize1 = Integer.valueOf(size1);
                        Integer intSize2 = Integer.valueOf(size2);

                        return (intSize2).compareTo(intSize1);
                    }
                });
            } else if (sortSize.equals("asc")) {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String size1 = mList1.getSize();
                        String size2 = mList2.getSize();

                        if (size1.equals("") || !isNumeric(size1)) size1 = "0";
                        if (size2.equals("") || !isNumeric(size2)) size2 = "0";

                        Integer intSize1 = Integer.valueOf(size1);
                        Integer intSize2 = Integer.valueOf(size2);

                        return (intSize1).compareTo(intSize2);
                    }
                });
            }
        }

        // Sorting date
        if (typeSort.equals("date")) {
            if (sortDate.equals("desc")) {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String date1 = mList1.getTglParsed();
                        String date2 = mList2.getTglParsed();

                        return (date2).compareTo(date1);
                    }
                });
            } else if (sortDate.equals("asc")) {
                Collections.sort(listModels, new Comparator<ListModel>() {
                    public int compare(ListModel mList1, ListModel mList2) {
                        String date1 = mList1.getTglParsed();
                        String date2 = mList2.getTglParsed();

                        return (date1).compareTo(date2);
                    }
                });
            }
        }
    }

    // Filtering And Sorting
    @Override
    public void onValuesSetSorting(String type, String price, String size, String date) {
        isSorting = true;
        typeSort = type;
        sortPrice = price;
        sortSize = size;
        sortDate = date;

        getData();
    }

    @Override
    public void onValuesSetFiltering(String key, String val) {
        try {
            isSorting = true;
            keyFiltering = key;
            keywordList = val;
            searchList.put(keyFiltering, keywordList);
            SoftKeyboardUtil.hide(ListActivity.this);

            getData();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // GET AREA
    private void getArea() {
        iViewArea iView = this;
        areaRequest = new AreaRequest(this, iView);
    }

    @Override
    public void onSuccessArea(List<AreaModel> data) {
        areaModels.clear();
        areaModels = data;

        if (isConnect) {
            OptionAreaDBLite dbLite = new OptionAreaDBLite(this);
            dbLite.addDB(areaModels);
        }

        getSize();
    }

    @Override
    public void onErrorArea(int code, String message) {
        SnackbarsUtil snackbarsUtil = new SnackbarsUtil(this);
        snackbarsUtil.onFailed("Terjadi Kesalahan Saat Memuat Data!");
        loadingPageUtil.stop();
        swipeRefresh.setRefreshing(false);
    }

    // GET SIZE
    private void getSize() {
        iViewSize iView = this;
        sizeRequest = new SizeRequest(this, iView);
    }

    @Override
    public void onSuccessSize(List<SizeModel> data) {
        sizeModels.clear();
        sizeModels = data;

        if (isConnect) {
            OptionSizeDBLite dbLite = new OptionSizeDBLite(this);
            dbLite.addDB(sizeModels);
        }

        loadingPageUtil.stop();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onErrorSize(int code, String message) {
        SnackbarsUtil snackbarsUtil = new SnackbarsUtil(this);
        snackbarsUtil.onFailed("Terjadi Kesalahan Saat Memuat Data!");
        loadingPageUtil.stop();
        swipeRefresh.setRefreshing(false);
    }

    // ADD DATA
    private void getAreaSpinner() {
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
                this, R.layout.item_spinner_default, provinceList){
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

        // City
        cityList.add(0, "Pilih Kota");
        final ArrayAdapter<String> spinnerCity = new ArrayAdapter<String>(
                this, R.layout.item_spinner_default, cityList){
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

        spinProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getItem = String.valueOf(parent.getItemAtPosition(position));

                cityList.clear();
                cityList.add(0, "Pilih Kota");
                for (int i = 0; i < areaModels.size(); i++) {
                    String province = areaModels.get(i).getProvince();
                    String city = areaModels.get(i).getCity();

                    if (province.equals(getItem)) {
                        cityList.add(city);
                    }
                }

                spinnerCity.setDropDownViewResource(R.layout.item_spinner_default);
                spinKota.setAdapter(spinnerCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getSizeSpinner() {
        // Size
        for (int i = 0; i < sizeModels.size(); i++) {
            String size = sizeModels.get(i).getSize();
            sizeList.add(size);
        }
        sizeList.add(0, "Pilih Ukuran");

        final ArrayAdapter<String> spinnerSize = new ArrayAdapter<String>(
                this, R.layout.item_spinner_default, sizeList){
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
        spinnerSize.setDropDownViewResource(R.layout.item_spinner_default);
        spinSize.setAdapter(spinnerSize);
    }

    private void showDialogAdd() {

        try {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.comp_bottom_sheet_dialog_add);

            View compSection = bottomSheetDialog.findViewById(R.id.compSection);
            TextView tvTitle = compSection.findViewById(R.id.tvTitle);
            TextView tvDescription = compSection.findViewById(R.id.tvDescription);

            tvTitle.setText(getString(R.string.sec_title_add));
            tvDescription.setText(getString(R.string.sec_description_add));

            tilKomoditas = bottomSheetDialog.findViewById(R.id.tilKomoditas);
            EditText etKomoditas = bottomSheetDialog.findViewById(R.id.etKomoditas);
            spinProvinsi = bottomSheetDialog.findViewById(R.id.spinProvinsi);
            spinKota = bottomSheetDialog.findViewById(R.id.spinKota);
            spinSize = bottomSheetDialog.findViewById(R.id.spinSize);
            tilPrice = bottomSheetDialog.findViewById(R.id.tilPrice);
            EditText etPrice = bottomSheetDialog.findViewById(R.id.etPrice);

            getAreaSpinner();
            getSizeSpinner();

            Date dateNow = getDateNow();
            String newDateStr = fromFormat1.format(dateNow);

            View compBtn = bottomSheetDialog.findViewById(R.id.compBtn);
            Button btnAdd = compBtn.findViewById(R.id.btnSmall);
            btnAdd.setText(getString(R.string.btn_save));

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String provinsi = spinProvinsi.getSelectedItem().toString();
                    String kota = spinKota.getSelectedItem().toString();
                    String ukuran = spinSize.getSelectedItem().toString();

                    ArrayList<ListModel> listModels = new ArrayList<>();

                    ListModel listModel = new ListModel();
                    listModel.setUuid(UUID.randomUUID().toString());
                    listModel.setKomoditas(etKomoditas.getText().toString());
                    listModel.setAreaProvinsi(provinsi);
                    listModel.setAreaKota(kota);
                    listModel.setSize(ukuran);
                    listModel.setPrice(etPrice.getText().toString());
                    listModel.setTglParsed(newDateStr);
                    listModels.add(listModel);

                    if (provinsi.equals("Pilih Provinsi")) {
                        Toast.makeText(ListActivity.this, "Pilih Provinsi!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (kota.equals("Pilih Kota")) {
                        Toast.makeText(ListActivity.this, "Pilih Kota!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (ukuran.equals("Pilih Ukuran")) {
                        Toast.makeText(ListActivity.this, "Pilih Ukuran!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!validateKomoditas() | !validatePrice()){
                        return;
                    } else {

                        iViewAdd iView = ListActivity.this;
                        AddRequest req = new AddRequest(ListActivity.this, iView);

                        loadingPageUtil.start();

                        if (isConnect) {
                            req.onAddRow(listModels);
                        } else {
                            req.onAddRowRoom(listModels);
                        }

                        bottomSheetDialog.dismiss();
                    }

                }
            });

            bottomSheetDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccessAdd(JSONObject jsonObject) {

        SnackbarsUtil snackbarsUtil = new SnackbarsUtil(this);
        snackbarsUtil.onSuccess("Tambah Data Berahasil!");

        resetList();
        getData();
    }

    @Override
    public void onErrorAdd(int code, String message) {

        SnackbarsUtil snackbarsUtil = new SnackbarsUtil(this);
        snackbarsUtil.onFailed("Tambah Data Gagal!");

        resetList();
        getList();
    }

    // Reset List After Sorting Or Filter Or Search
    private void resetList() {
        isSearching = false;
        isSorting = false;
        isFiltering = false;
        searchList = new JSONObject();
        keywordList = "";
        etSearch.setText("");
        etSearch.clearFocus();
        llResetData.setVisibility(View.GONE);
    }

    // Validation Input For Add Data List
    private boolean validateKomoditas(){
        String komoditas =  tilKomoditas.getEditText().getText().toString();

        if (komoditas.isEmpty()) {
            tilKomoditas.setError(getString(R.string.text_validate_komoditas));
            return false;
        } else {
            tilKomoditas.setError(null);
            return true;
        }
    }

    private boolean validatePrice(){
        String price =  tilPrice.getEditText().getText().toString();

        if (price.isEmpty()) {
            tilPrice.setError(getString(R.string.text_validate_price));
            return false;
        } else {
            tilPrice.setError(null);
            return true;
        }
    }

    // Check Connection Manual
    private void checkConnection() {
        isConnect = isInternetConnection();
    }

    public boolean isInternetConnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        return connected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onRefresh() {
        getData();
        loadingPageUtil.stop();
    }

    @Override
    public void onClick(View view) {
        if (view == fabAdd) {
            showDialogAdd();
        } else if (view == llResetData) {
            resetList();
            getData();
        } else if (view == ivSorting) {
            DialogSorting dialog = new DialogSorting(this, searchList);
            dialog.show();
        } else if (view == ivFilter) {
            DialogFilter dialog = new DialogFilter(this, areaModels);
            dialog.show();
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Silakan klik KEMBALI lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}