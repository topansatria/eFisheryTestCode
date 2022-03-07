package com.efhiserytestcode.network.request.optionArea;

import android.content.Context;

import com.efhiserytestcode.model.AreaModel;
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

import static com.efhiserytestcode.MyApp.dbApp;

public class AreaRequest {

    private ApiService apiService;
    private Context context;
    private iViewArea iView;

    public AreaRequest(Context context, iViewArea iView) {
        this.context = context;
        this.iView = iView;
    }

    public void getArea() {
        CompositeDisposable disposable = new CompositeDisposable();
        apiService = ApiClient.getClient(context).create(ApiService.class);

        disposable.add(apiService.getArea()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<ArrayList<AreaModel>>() {

                @Override
                public void onNext(ArrayList<AreaModel> response) {
                    iView.onSuccessArea(response);
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

                            iView.onErrorArea(responseCode, error);

                        } catch (JSONException | IOException ex) {
                            ex.printStackTrace();
                            iView.onErrorArea(responseCode, error);
                        }
                    }
                }

                @Override
                public void onComplete() {}
            })
        );
    }

    public void getAreaRoom() {
        iView.onSuccessArea(dbApp.optionAreaDao().getDataAll());
    }
}
