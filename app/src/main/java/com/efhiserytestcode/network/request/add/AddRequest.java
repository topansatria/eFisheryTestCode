package com.efhiserytestcode.network.request.add;

import android.app.Activity;
import android.content.Context;

import com.efhiserytestcode.database.list.ListDBLite;
import com.efhiserytestcode.model.ListModel;
import com.efhiserytestcode.network.service.ApiClient;
import com.efhiserytestcode.network.service.ApiService;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class AddRequest {

    private ApiService apiService;
    private Activity mActivity;
    private iViewAdd iView;

    public AddRequest(Activity mActivity, iViewAdd iView) {
        this.mActivity = mActivity;
        this.iView = iView;
    }

    public void onAddRow(ArrayList<ListModel> listModels) {
        CompositeDisposable disposable = new CompositeDisposable();
        apiService = ApiClient.getClient(mActivity).create(ApiService.class);

        disposable.add(apiService.addRow(listModels)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<JSONObject>() {

                @Override
                public void onNext(JSONObject response) {
                    iView.onSuccessAdd(response);
                }

                @Override
                public void onError(Throwable e) {
                    // Show response from body when error
                    if (e instanceof HttpException) {
                        ResponseBody responseBody = ((HttpException) e).response().errorBody();
                        int responseCode = ((HttpException)e).response().code();
                        String error = "";

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            error = jsonObject.getString("error");

                            iView.onErrorAdd(responseCode, error);

                        } catch (JSONException | IOException ex) {
                            ex.printStackTrace();
                            iView.onErrorAdd(responseCode, error);
                        }
                    }
                }

                @Override
                public void onComplete() {}
            })
        );
    }

    public void onAddRowRoom(ArrayList<ListModel> listModels) {
        try {
            ListDBLite dbLite = new ListDBLite(mActivity);
            dbLite.addDB(listModels);

            JSONObject response = new JSONObject();
            response.put("updatedRange", "Success");
            iView.onSuccessAdd(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
