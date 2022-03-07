package com.efhiserytestcode.database.optionSize;

import android.content.Context;

import com.efhiserytestcode.model.AreaModel;
import com.efhiserytestcode.model.SizeModel;

import java.util.List;

import static com.efhiserytestcode.MyApp.dbApp;

public class OptionSizeDBLite {

    private Context context;

    public OptionSizeDBLite(Context context) {
        this.context = context;
    }

    public void addDB(List<SizeModel> sizeModels){
        deleteDB();

        if (sizeModels.size() > 0) {
            for (int i = 0; i < sizeModels.size(); i++) {
                String size = sizeModels.get(i).getSize();

                SizeModel model = new SizeModel();
                model.setSize(size);
                dbApp.optionSizeDao().insertData(model);
            }
        }
    }

    public void deleteDB() {
        dbApp.optionSizeDao().resetAI();
        dbApp.optionSizeDao().deleteAllData();
    }
}
