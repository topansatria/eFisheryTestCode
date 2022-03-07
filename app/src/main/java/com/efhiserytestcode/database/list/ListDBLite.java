package com.efhiserytestcode.database.list;

import android.content.Context;

import com.efhiserytestcode.model.ListModel;

import java.util.List;

import static com.efhiserytestcode.MyApp.dbApp;

public class ListDBLite {

    private Context context;

    public ListDBLite(Context context) {
        this.context = context;
    }

    public void addDB(List<ListModel> listModels){
        if (listModels.size() > 0) {
            for (int i = 0; i < listModels.size(); i++) {
                String uuid = listModels.get(i).getUuid();
                String komoditas = listModels.get(i).getKomoditas();
                String areaProvinsi = listModels.get(i).getAreaProvinsi();
                String areaKota = listModels.get(i).getAreaKota();
                String ukuran = listModels.get(i).getSize();
                String harga = listModels.get(i).getPrice();
                String tglParsed = listModels.get(i).getTglParsed();

                ListModel model = new ListModel();
                model.setUuid(uuid);
                model.setKomoditas(komoditas);
                model.setAreaProvinsi(areaProvinsi);
                model.setAreaKota(areaKota);
                model.setSize(ukuran);
                model.setPrice(harga);
                model.setTglParsed(tglParsed);
                dbApp.listDao().insertData(model);
            }
        }
    }

    public void deleteDB() {
        dbApp.listDao().resetAI();
        dbApp.listDao().deleteAllData();
    }
}
