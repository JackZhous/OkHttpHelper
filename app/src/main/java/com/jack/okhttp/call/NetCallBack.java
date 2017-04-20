package com.jack.okhttp.call;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;


import com.jack.okhttp.base.BaseResponse;
import com.jack.okhttp.utils.JLog;
import com.jack.okhttp.view.LoadDialog;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by jackzhous on 2017/3/14.
 */

public abstract class NetCallBack<T>{

    private static Handler          mainHandler = new Handler(Looper.getMainLooper());
    private LoadDialog              loadDialog;
    private int                     netTaskFlag;
    private boolean                 netReceiveFlag = false;



    public NetCallBack(int netTaskFlag){
        this(netTaskFlag, null);
    }

    public NetCallBack(int  flag, Activity activity){
        netTaskFlag = flag;

        showLoadingDialog(new WeakReference<Activity>(activity));
    }

    public void onError(){
        JLog.i("j_net", "okhttp error ");
        netReceiveFlag = true;
        closeLoadingDialog();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                responseTOMainError(netTaskFlag);
            }
        });
    }

    public void onResponse(final String response) throws IOException{
        JLog.i("j_net", "okhttp onResponse ");
        netReceiveFlag = true;
        closeLoadingDialog();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                responseToMainThread(response, netTaskFlag);
            }
        });

    }

    /**
     * 回调给主线程的方法
     * @param str
     */
    public abstract void responseToMainThread(String str, int taskFlag);



    public abstract void responseTOMainError(int netTaskFlag);

    //延迟显示加载框
    private void showLoadingDialog(final WeakReference<Activity> weakReference){
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!netReceiveFlag){
                    Activity activity = weakReference.get();
                    if(activity == null){
                        return;
                    }
                    loadDialog = new LoadDialog(activity, true, null);
                    setDialogCancelListener();
                    loadDialog.show();
                }
            }
        }, 1000);
    }


    private void setDialogCancelListener(){
        loadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                responseTOMainError(netTaskFlag);
            }
        });
    }

    private void closeLoadingDialog(){
        if(loadDialog != null){
            loadDialog.dismiss();
            loadDialog = null;
        }
    }



}
