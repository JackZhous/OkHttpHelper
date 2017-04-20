package com.jack.okhttp.base;

import android.util.Log;


import com.jack.okhttp.call.NetCall;
import com.jack.okhttp.call.NetCallBack;
import com.jack.okhttp.utils.JLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ttttt on 2017/3/14.
 */

public class OkHttpManager implements NetCall {
    private static final String      TAG = "j_net";
    private static final String      BASE_URL = "http://www.baidu.com/";
    private static final MediaType   JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpManager     instance = new OkHttpManager();

    private OkHttpClient             client;

    private OkHttpManager(){
        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                                           .writeTimeout(10, TimeUnit.SECONDS)
                                           .readTimeout(30, TimeUnit.SECONDS)
                                           .build();
    }

    public static OkHttpManager getInstance(){
        if(null == instance){
            instance = new OkHttpManager();
        }

        return instance;
    }

    @Override
    public void OkHttpGet(HashMap<String, String> data, NetCallBack callBack) {
        Request request = new Request.Builder().url(attachHttpGetParams(data)).build();
        syncRequest(request, callBack);
    }

    @Override
    public void OkHttpPostJSON(String postParams, NetCallBack callBack) {
        Log.i("j_net", "send: " + postParams);
        RequestBody body = RequestBody.create(JSON, postParams);
        Request request = new Request.Builder().post(body).url(BASE_URL).build();
        syncRequest(request, callBack);
    }

    /**
     * GET请求添加附加参数
     * @param params
     * @return
     */
    private static String attachHttpGetParams(HashMap<String, String> params){
        if(null == params){
            return BASE_URL;
        }
        String result = BASE_URL + "?";
        for(String key : params.keySet()){
            result = result + key + "=" + params.get(key) + "&";
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 开始请求网络数据
     * @param request
     * @param callback
     */
    private void syncRequest(final Request request, final NetCallBack callback){
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                JLog.i(TAG, "net error, " + e.getMessage());
                e.printStackTrace();
                callback.onError();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                callback.onResponse(respon);
            }
        });
    }
}
