package com.example.zf_android.trade.common;

import android.content.Context;
import android.text.TextUtils;

import com.example.zf_android.R;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by Leo on 2015/2/11.
 */
public class HttpRequest {

    private AsyncHttpClient client;
    private Context context;
    private TextHttpResponseHandler responseHandler;
    private HttpCallback callback;

    public HttpRequest(final Context context, final HttpCallback callback) {
        this.context = context;
        this.client = new AsyncHttpClient();
        this.callback = callback;
        this.responseHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onFailure(context.getString(R.string.load_data_failed));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Response data = JsonParser.fromJson(responseString, callback.getTypeToken());
                if (data.getCode() == 1) {
                    callback.onSuccess(data.getResult());
                } else if (!TextUtils.isEmpty(data.getMessage())) {
                    callback.onFailure(data.getMessage());
                }
            }

            @Override
            public void onFinish() {
                callback.postLoad();
            }

            @Override
            public void onStart() {
                callback.preLoad();
            }
        };
    }

    public void get(String url) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            callback.onFailure(context.getString(R.string.network_info));
            return;
        }
        client.get(url, responseHandler);
    }

    public void post(String url, RequestParams requestParams) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            callback.onFailure(context.getString(R.string.network_info));
            return;
        }
        client.post(url, requestParams, responseHandler);
    }
}