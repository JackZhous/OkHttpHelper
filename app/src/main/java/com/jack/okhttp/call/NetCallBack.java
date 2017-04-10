package com.jack.okhttp.call;

import android.os.Handler;
import android.os.Looper;


import com.jack.okhttp.utils.JLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jackzhous on 2017/3/14.
 */

public abstract class NetCallBack implements Callback{

    private static Handler mainHandler = new Handler(Looper.getMainLooper());
    private String         netTaskFlag;

    public NetCallBack(String  flag){
        netTaskFlag = flag;
    }

    @Override
    public void onFailure(Call call, IOException e){
        JLog.i("j_net", "okhttp error ");
        e.printStackTrace();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                responseTOMainError(netTaskFlag);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException{
        final String result = doOkhttpResponse(response);
        JLog.i("j_net", "receive: " + result);

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                responseToMainThread(result, netTaskFlag);
            }
        });

    }

    /**
     * 回调给主线程的方法
     * @param str
     */
    public abstract void responseToMainThread(String str, String taskFlag);



    public abstract void responseTOMainError(String netTaskFlag);

    /**
     * 处理直接网络底层的数据
     * @param response
     * @return
     */
    public static String doOkhttpResponse(Response response){

        try {
            String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

}
