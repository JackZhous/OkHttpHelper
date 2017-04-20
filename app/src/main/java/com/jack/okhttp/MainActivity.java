package com.jack.okhttp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jack.okhttp.base.OkHttpManager;
import com.jack.okhttp.call.NetCall;
import com.jack.okhttp.call.NetCallBack;
import com.jack.okhttp.utils.JLog;
import com.jack.okhttp.view.LoadDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MainActivity extends Activity {

    private NetCall mNetManager = OkHttpManager.getInstance();
    private static final int  LOGIN_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


    private static class NetBack extends NetCallBack {
        WeakReference<MainActivity> weakReference;
        public NetBack(int flag, MainActivity activity) {
            super(flag, activity);          //这里我要使用弹出加载框

            weakReference = new WeakReference<>(activity);

        }

        /**
         * okhttp回调
         * @param str 回调的字符串
         * @param taskFlag  任务标志
         */
        @Override
        public void responseToMainThread(String str, int taskFlag) {
            MainActivity activity = weakReference.get();
            if(null == activity){
                return;
            }

            switch (taskFlag){
                case LOGIN_TASK:
                    break;
            }

            JLog.i("j_net", str);
        }

        @Override
        public void responseTOMainError(int netTaskFlag) {
            MainActivity activity = weakReference.get();

            if(null == activity){
                return;
            }
        }
    }




    public void onClick(View view){
        HashMap<String,String> loginParams = new HashMap<>();
        loginParams.put("mobile", "username");
        loginParams.put("password", "password");

        String params = new JSONObject(loginParams).toString();

        mNetManager.OkHttpPostJSON(params, new NetBack(LOGIN_TASK, this));  //开始网络请求
    }



}
