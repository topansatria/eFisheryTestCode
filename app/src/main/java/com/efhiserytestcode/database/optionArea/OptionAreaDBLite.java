package com.efhiserytestcode.database.optionArea;

import android.content.Context;

import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.ListModel;

import java.util.List;

import static com.efhiserytestcode.MyApp.dbApp;

public class OptionAreaDBLite {

    private Context context;

    public OptionAreaDBLite(Context context) {
        this.context = context;
    }

    public void addDB(List<AreaModel> areaModels){
        deleteDB();

        if (areaModels.size() > 0) {
            for (int i = 0; i < areaModels.size(); i++) {
                String province = areaModels.get(i).getProvince();
                String city = areaModels.get(i).getCity();

                AreaModel model = new AreaModel();
                model.setProvince(province);
                model.setCity(city);
                dbApp.optionAreaDao().insertData(model);
            }
        }
    }

    public void deleteDB() {
        dbApp.optionAreaDao().resetAI();
        dbApp.optionAreaDao().deleteAllData();
    }
}
