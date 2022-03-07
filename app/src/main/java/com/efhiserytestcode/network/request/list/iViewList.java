package com.efhiserytestcode.network.request.list;

import com.efhiserytestcode.model.ListModel;

import java.util.ArrayList;
import java.util.List;

public interface iViewList {
    void onSuccessList(List<ListModel> listModels);
    void onErrorList(int code, String message);
}
