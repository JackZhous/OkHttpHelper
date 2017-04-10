package com.jack.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jack.okhttp.base.OkHttpManager;
import com.jack.okhttp.call.NetCall;
import com.jack.okhttp.call.NetCallBack;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private NetCall mNetManager = OkHttpManager.getInstance();
    private static final String LOGIN_TASK = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String,String> loginParams = new HashMap<>();
        loginParams.put("mobile", "username");
        loginParams.put("password", "password");

        String params = new JSONObject(loginParams).toString();

        mNetManager.OkHttpPostJSON(params, new NetBack(LOGIN_TASK, this));  //开始网络请求
    }


    private static class NetBack extends NetCallBack {
        WeakReference<MainActivity> weakReference;
        public NetBack(String flag, MainActivity activity) {
            super(flag);

            weakReference = new WeakReference<>(activity);

        }

        /**
         * okhttp回调
         * @param str 回调的字符串
         * @param taskFlag  任务标志
         */
        @Override
        public void responseToMainThread(String str, String taskFlag) {
            MainActivity activity = weakReference.get();
            if(null == activity){
                return;
            }

            if(MainActivity.LOGIN_TASK.equals(taskFlag)){
                //写你的登录结果处理代码
            }

        }

        @Override
        public void responseTOMainError(String netTaskFlag) {
            MainActivity activity = weakReference.get();

            if(null == activity){
                return;
            }


        }
    }

}
