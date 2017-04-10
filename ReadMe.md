# okhttp使用简单封装库

## 简介
okhttp执行网络请求时是自动开子线程进行的，但是处理网络数据回调回来时，也在子线程中，而我们日常开发主要是在主线程中更新UI，这样是极不方便的，为了解决这个问题和简化okhttp的使用难度，本库对okhttp做了一个简单的封装

## 版本说明：

__okhttp版本信息：3.6.0__

## 使用说明
本库分为三个类：
	1. OkHttpManager -- 网络底层类，执行网络请求的真正实现类
	
	2. NetCall       -- 网络请求类标准接口，OKHttpManager就实现这个接口，如果要添加新的网络访问方式，就在这个类添加标准接口即可

	3. NetCallBack   -- 网络回调类，网络请求完成后回调类


### 具体使用方法：
以一个登陆Activity里面使用为例说明：

```java

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
```

## 总结
如有更好的解决办法，不吝赐教
