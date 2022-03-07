package com.efhiserytestcode.network.request.list;

import android.app.Activity;

import com.efhiserytestcode.model.ListModel;
import com.efhiserytestcode.network.service.ApiClient;
import com.efhiserytestcode.network.service.ApiService;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.efhiserytestcode.MyApp.dbApp;

public class ListRequest {

    private ApiService apiService;
    private Activity mActivity;
    private iViewList iView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ListRequest(Activity mActivity, iViewList iView) {
        this.mActivity = mActivity;
        this.iView = iView;
    }

    public void getList(JSONObject search) {
        apiService = ApiClient.getClient(mActivity).create(ApiService.class);

        compositeDisposable.add(apiService.getList(search)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<List<ListModel>>() {

                @Override
                public void onNext(List<ListModel> response) {
                    iView.onSuccessList(response);
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

                            iView.onErrorList(responseCode, error);

                        } catch (JSONException | IOException ex) {
                            ex.printStackTrace();
                            iView.onErrorList(responseCode, error);
                        }
                    }
                }

                @Override
                public void onComplete() {}
            })
        );
    }

    public void getListRoom(String type, String keywordList) {
        if (!keywordList.equals("")) {
            if (type.equals("area_provinsi")) {
                iView.onSuccessList(dbApp.listDao().getDataAllWhereProv(keywordList));
            } else if (type.equals("area_kota")) {
                iView.onSuccessList(dbApp.listDao().getDataAllWhereCity(keywordList));
            } else {
                iView.onSuccessList(dbApp.listDao().getDataAllWhereKomoditas(keywordList));
            }
        } else {
            iView.onSuccessList(dbApp.listDao().getDataAll());
        }
    }
}
