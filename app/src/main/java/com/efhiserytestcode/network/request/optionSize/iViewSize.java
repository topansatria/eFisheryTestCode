package com.efhiserytestcode.network.request.optionSize;

import com.efhiserytestcode.model.SizeModel;

import java.util.List;

public interface iViewSize {
    void onSuccessSize(List<SizeModel> listModels);
    void onErrorSize(int code, String message);
}
