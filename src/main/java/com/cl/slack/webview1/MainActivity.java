package com.cl.slack.webview1;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WebView";
    private WebView mWebView;
    private FrameLayout mRootView;
    private String mJsString = "";

    /**
     * 虎牙直播  这些需要更换
     * 房间号  ？
     * topSid
     * subSid
     */
    private final static String HUYA_WEB_URL = "http://m.huya.com/" + "mianzai"; // only load js this is not need
    private final static String TOP_SID = "69361899";
    private final static String SUB_SID = "2545237228";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mRootView = (FrameLayout) findViewById(R.id.activity_main);
        initWebView();
        mJsString = initAssertJSToString("js/huya.js");
    }

    private String initAssertJSToString(String assetName) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = getAssets().open(assetName);
            StringBuilder sb = new StringBuilder();
            String line;
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
//            mJsString = "<script type=\"text/javascript\">" + mJsString + "</script>";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

        /**
         * 不在xml中定义 Webview ，而是在需要的时候在Activity中创建，
         * 并且Context使用 getApplicationgContext()
         */
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1,1);
//                FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mRootView.addView(mWebView,0);

        WebSettings setting = mWebView.getSettings();
        /**
         * 支持javascript脚本  会 产生 跨站脚本攻击
         */
        setting.setJavaScriptEnabled(true);

        /**
         * 设置编码
         */
        setting.setDefaultTextEncodingName("utf-8");

        /**
         * 设置自适应屏幕，两者合用
         */
        setting.setUseWideViewPort(true); //将图片调整到适合webview的大小
        setting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        setting.setSupportZoom(true); // 支持缩放
        setting.setBuiltInZoomControls(true); // 支持手势缩放
        setting.setDisplayZoomControls(false); //隐藏原生的缩放控件

        /**
         * 缓存模式如下：
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        setting.setAllowFileAccess(true); //设置可以访问文件
        setting.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        setting.setLoadsImagesAutomatically(true); //支持自动加载图片
        setting.setDefaultTextEncodingName("utf-8");//设置编码格式


        setting.setPluginState(WebSettings.PluginState.ON);

//        //方式1. 加载一个网页：
//        mWebView.loadUrl("http://www.google.com/");
//
//        //方式2：加载apk包中的html页面
//        mWebView.loadUrl("file:///android_asset/test.html");
//
//        //方式3：加载手机本地的html页面
//        mWebView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted..." + url);
//                view.loadUrl("javascript:" + mJsString);//注入js函数
//                view.loadUrl("javascript:HuYaListener("+TOP_SID+","+SUB_SID+")");//调用js函数
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
//                Log.i(TAG, "onLoadResource..." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                Log.i(TAG, "onPageFinished..." + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i(TAG, "onReceivedError..." + error.getDescription().toString());
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
//                Log.i(TAG, "onProgressChanged..." + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Log.i(TAG, "onReceivedTitle..." + title);
            }

            /**
             * 设置响应js 的Alert()函数
             * W/WebViewCallback: Unable to create JsDialog without an Activity
             * new  AlertDialog.Builder ...
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i(TAG, "onJsAlert..." + message);
                return super.onJsAlert(view, url, message, result);
            }

            /**
             * 设置响应js 的Confirm()函数
             * new  AlertDialog.Builder ...
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            /**
             * 设置响应js 的Confirm()函数
             * 需要自定一个提示的布局文件
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                Log.i(TAG,"onConsoleMessage : " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

        });

        /**
         * 设置本地调用对象及其接口
         * addJavascriptInterface minSdkVersion 17
         */
        mWebView.addJavascriptInterface(new HuYaWebInterface(),"HuYa");

    }


    public void startConnectWeb(View view) {
//        mWebView.loadUrl("http://www.baidu.com/"); // ok
//        mWebView.loadData(HTML_CODE,"text/html","uft-8"); // ok

        /**
         * js ---调用---> java  test is ok
         */
//        mWebView.addJavascriptInterface(new JSBridge(),"slack");
//        mWebView.loadUrl("file:///android_asset/index.html");// loadUrl ok

        /**
         * onPageStarted 加载js
         */
//        mWebView.loadUrl(HUYA_WEB_URL);

        /**
         * 直接 js 的方法，现在想加入 jquery ,也可以类似 mJsString 读取一下，转String
         * 感觉有点low，加载一个空白网页吧还是
         */
        mWebView.loadUrl("javascript:" + mJsString);//注入js函数
        mWebView.loadUrl("javascript:HuYaListener("+TOP_SID+","+SUB_SID+")");//调用js函数

        /**
         * load empty html include local js
         * test is ok
         */
//        mWebView.loadUrl("file:///android_asset/huya.html");

        /**
         * this test is ok
         * 测试发现 加上 类似下面的注释，就调用不了了
         * ////////////////////////////////////////////////////////
         */
//        mWebView.loadUrl("javascript:" + mJsString);
//        mWebView.loadUrl("javascript:justTest("+TOP_SID+","+SUB_SID+")");
    }

    /**
     *  虎牙 直播 js 调用 接口
     */
    public class HuYaWebInterface{

        /**
         * 登录 的 反馈 信息
         */
        @JavascriptInterface
        public void huyaLogin(String str) {
            Log.i(TAG,"login : " + str);
        }

        /**
         * 1400 : 聊天信息
         * @param nick  nickname
         * @param msg message
         */
        @JavascriptInterface
        public void huyaChartMessage(String nick,String msg) {
            Log.i(TAG,"1400: nick: " + nick + " + msg: " + msg);
        }

        /**
         * 6501 : tanmu
         * 弹幕 信息暂时获取不到详情
         * nick: n.sSenderNick,
         * propName: c.tanmu.propsInfo[t].propName,
         * icon: c.tanmu.propsInfo[t].propIcon,
         * count: n.iItemCount
         */
        @JavascriptInterface
        public void huyaDanMuMessage(String nick,String count) {
            Log.i(TAG,"tanmu: nick: " + nick + " + count: " + count);
        }

        /**
         * 在线人数 liveCount
         * @param count
         */
        @JavascriptInterface
        public void huyaAttendeeCount(String count) {
            Log.i(TAG,"liveCount: " + count);
        }
    }

    /**
     * java ---调用---> js   need startConnectWeb first ,test is ok
     */
    public void javaCallJs(View view) {
//        mWebView.loadUrl("javascript:javaCallJs('java call js')");

        /**
         * will call back in {@link android.webkit.WebChromeClient#onJsAlert(
         * android.webkit.WebView, java.lang.String, java.lang.String, android.webkit.JsResult)}
         * 在log 里可以查看  test is ok
         */
//        mWebView.loadUrl("javascript:" + JS_FUNCTION);//注入js函数
//        mWebView.loadUrl("javascript:" + initAssertJSToString("js/testJs.js"));// read from file is also ok
//        mWebView.loadUrl("javascript:jsFun("+TOP_SID+")");//调用js函数
//        mWebView.loadUrl("javascript:justTest("+TOP_SID+","+SUB_SID+")");// double param is also ok
    }

    public class JSBridge {
        @JavascriptInterface
        public void jsMessage(String message) {
            Log.i(TAG, "jsMessage" + message);
            Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 激活WebView为活跃状态，能正常执行网页的响应
         */
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行
         */
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {

        /**
         * 先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
         */
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            /**
             * 清理cache 和历史记录
             */
            mWebView.clearCache(true);
            mWebView.clearHistory();
            /**
             * 销毁 Webview
             * Webview调用destory时
             * Webview仍绑定在Activity
             */
            mRootView.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    final static String JS_FUNCTION =
            "function jsFun(msg) {\n" +
            "          alert(\"jsFun...\" + msg); " +
            "           }";

    final static String HTML_CODE =
        "   <html>\n" +
        "        <head> " +
        "           <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
        "           <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,\n" +
        "                                minimum-scale=1.0, maximum-scale=2.0, user-scalable=yes\"/>" +
        "           <title>webview index</title>" +
        "           <script type=\"text/javascript\">\n" +
        "    　　         function myfun() { 　　\n" +
        "               var nickname = document.getElementById(\"nickname\");　\n" +
        "               nickname.value = \"匿名\";\n" +
        "      　        } 　　\n" +
        "           </script>" +
        "       </head>\n" +
        "       <body onload=\"myfun()\">" +
        "           <input type=\"text\" id=\"nickname\" name=\"NickName\" value=\"slack\"/>"+
        "       </body>\n" +
        "   </html>";
}
