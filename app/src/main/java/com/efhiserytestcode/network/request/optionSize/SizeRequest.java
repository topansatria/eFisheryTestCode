package com.efhiserytestcode.network.request.optionSize;

import android.content.Context;

import com.efhiserytestcode.model.SizeModel;
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

public class SizeRequest {

    private ApiService apiService;
    private Context context;
    private iViewSize iView;

    public SizeRequest(Context context, iViewSize iView) {
        this.context = context;
        this.iView = iView;
    }

    public void getSize() {
        CompositeDisposable disposable = new CompositeDisposable();
        apiService = ApiClient.getClient(context).create(ApiService.class);

        disposable.add(apiService.getSize()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<ArrayList<SizeModel>>() {

                @Override
                public void onNext(ArrayList<SizeModel> response) {
                    iView.onSuccessSize(response);
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

                            iView.onErrorSize(responseCode, error);

                        } catch (JSONException | IOException ex) {
                            ex.printStackTrace();
                            iView.onErrorSize(responseCode, error);
                        }
                    }
                }

                @Override
                public void onComplete() {}
            })
        );
    }

    public void getSizeRoom() {
        iView.onSuccessSize(dbApp.optionSizeDao().getDataAll());
    }
}
