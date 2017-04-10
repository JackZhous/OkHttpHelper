package com.jack.okhttp.call;

import java.util.HashMap;

import okhttp3.Callback;

/**
 * Created by ttttt on 2017/3/14.
 */

public interface NetCall {

    void OkHttpGet(HashMap<String, String> data, Callback callBack);


    void OkHttpPostJSON(String postParams, Callback callBack);
}
