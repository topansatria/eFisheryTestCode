package com.efhiserytestcode.network.request.add;

import org.json.JSONObject;

public interface iViewAdd {
    void onSuccessAdd(JSONObject jsonObject);
    void onErrorAdd(int code, String message);
}
