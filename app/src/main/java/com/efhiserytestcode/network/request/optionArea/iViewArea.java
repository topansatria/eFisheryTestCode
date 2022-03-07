package com.efhiserytestcode.network.request.optionArea;

import com.efhiserytestcode.model.AreaModel;

import java.util.List;

public interface iViewArea {
    void onSuccessArea(List<AreaModel> listModels);
    void onErrorArea(int code, String message);
}
