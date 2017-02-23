###android java 与 webView Js 相互调用
```
/**
 * js ---调用---> java  test is ok
 */
mWebView.addJavascriptInterface(new JSBridge(),"slack");
mWebView.loadUrl("file:///android_asset/index.html");// loadUrl ok

/**
 * will call back in {@link android.webkit.WebChromeClient#onJsAlert(
 * android.webkit.WebView, java.lang.String, java.lang.String, android.webkit.JsResult)}
 * 在log 里可以查看  test is ok
 */
mWebView.loadUrl("javascript:" + JS_FUNCTION);//注入js函数
//mWebView.loadUrl("javascript:" + initAssertJSToString("testJs.js"));// read from file is also ok
mWebView.loadUrl("javascript:jsFun("+TOP_SID+")");//调用js函数
//mWebView.loadUrl("javascript:justTest("+TOP_SID+","+SUB_SID+")");// double param is also ok
```