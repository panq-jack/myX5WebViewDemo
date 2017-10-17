package com.example.test_webview_demo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.test_webview_demo.utils.WebViewJavaScriptFunction;
import com.example.test_webview_demo.utils.X5WebView;

public class FullScreenActivity extends Activity {

    /**
     * 用于演示X5webview实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
     */

    X5WebView webView;

    ViewGroup rootView;

    boolean isFullScrenn = false;

    Handler handler=new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filechooser_layout);
        Log.d("ppp","tbsCoreVersion: "+webView.getTbsCoreVersion(this));
        Log.d("ppp","tbsSdkVersion: "+webView.getTbsSDKVersion(this));
        rootView = (ViewGroup) findViewById(R.id.root);
        webView = (X5WebView) findViewById(R.id.web_filechooser);

//		webView.loadUrl("https://h5.m.jd.com/dev/2YXgtR7PCjksqNDs5StJZwLYdVvY/index.html");
//		webView.loadUrl("https://v.qq.com/p/topic/CPC19th/index.html");   //原生无法播放   qq
//		webView.loadUrl("http://video.sina.com.cn/news/spj/topvideoes20171015/?opsubject_id=top1#251761084");  //sina 原生能播，无法放大
//		webView.loadUrl("http://v.youku.com/v_show/id_XMzA5MDM4NDM2OA==.html?spm=a2hww.20027244.m_250166.5~5!2~5~5!4~5~5~A&f=51241000");

        webView.loadUrl("file:///android_asset/webpage/fullscreenVideo.html");
//		webView.loadUrl("https://h5.m.jd.com/dev/4YdVcE7cCh7uutnm3s31vcRQkksv/index.html");
//		webView.loadUrl("http://news.youku.com/zgm2017?spm=a2hww.20027244.m_250166.5~5!2~5~5~5~5~A");
//		webView.loadUrl("http://baishi.baidu.com/watch/01129265824475080703.html");
//		webView.loadUrl("http://103.40.220.39/youku/67753C464CF4D7E0732C86B92/03000A010059DC6C25E5673156600588A527FE-53BE-8EA3-DEBC-D269C9DE5C6B.mp4?sid=05082130262931219e355&ctype=12&ccode=0512&duration=185&expire=18000&psid=6aa857cf1818a2aa782826e34b15d14d&ups_client_netip=118.242.26.178&ups_ts=1508213026&ups_userid=&utid=Qb1rEerA%2BlsCATr3lmtxSPZq&vkey=A14055bd3aff58352e6fa55679f867f79&vid=XMzA3NjE4OTQ0OA%3D%3D");
//		source="http://storage.jd.com/dushiliren/video2.mp4?Expires=3654090943&AccessKey=DZXu2HxH8GzCKCuS&Signature=g0ldYJhOFbGLnyfDCxiM%2FvEH0h0%3D";
//		source="http://59.152.16.209/65667AC65D4C82E6C5473246A/030011010059E2FE12554719B69A66CEAE9B14-CFE6-6A65-19E5-F574A313AE58.mp4?ccode=0502&duration=70&expire=18000&psid=caf46334f58fb5993247f0e69470e3b4&ups_client_netip=124.15.247.138&ups_ts=1508072103&ups_userid=&utid=IrMtEg6ctVkCAXwP9jfRCJ8%2F&vid=XMzA4NDgwNTUyMA%3D%3D&vkey=A5eacb789d6de6314b648af17df88559f";

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub

            }

            @JavascriptInterface
            public void onX5ButtonClicked() {
                FullScreenActivity.this.enableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onCustomButtonClicked() {
                FullScreenActivity.this.disableX5FullscreenFunc();
            }

            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                FullScreenActivity.this.enableLiteWndFunc();
            }

            @JavascriptInterface
            public void onPageVideoClicked() {
                FullScreenActivity.this.enablePageVideoFunc();
            }

            @JavascriptInterface
            public void onEnterFullScreen() {
                Toast.makeText(FullScreenActivity.this, "onEnterFullScreen", Toast.LENGTH_SHORT).show();
                enterFullScreenOnMainThread();

            }
            @JavascriptInterface
            public void onExitFullScreen() {
                Toast.makeText(FullScreenActivity.this, "onExitFullScreen", Toast.LENGTH_SHORT).show();
                exitFullScreenOnMainThread();
            }
        }, "Android");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub

        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d("ppp", "onConfigurationChanged: " + "ORIENTATION_LANDSCAPE");
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d("ppp", "onConfigurationChanged: " + "ORIENTATION_PORTRAIT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    FullscreenHolder fullscreenHolder;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    // /////////////////////////////////////////
    // 向webview发出信息

    private void enterFullScreenOnMainThread(){
        if (Looper.myLooper()!=Looper.getMainLooper()){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    enterFullScreen();
                }
            });
        }else {
            enterFullScreen();
        }
    }

    private void enterFullScreen() {
        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();

        if (null != webView && null != rootView) {

            rootView.removeView(webView);
        }
        fullscreenHolder = new FullscreenHolder(FullScreenActivity.this);
        fullscreenHolder.addView(webView, COVER_SCREEN_PARAMS);
        decorView.addView(fullscreenHolder, COVER_SCREEN_PARAMS);
        //横屏
        FullScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setStatusBarVisibility(false);

        isFullScrenn=true;


    }

    private void exitFullScreenOnMainThread(){
        if (Looper.myLooper()!=Looper.getMainLooper()){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    exitFullScreen();
                }
            });
        }else {
            exitFullScreen();
        }
    }

    private void exitFullScreen() {
        setStatusBarVisibility(true);
        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        fullscreenHolder.removeAllViews();
        decorView.removeView(fullscreenHolder);
        fullscreenHolder = null;
        rootView.addView(webView);
        //竖屏
        FullScreenActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isFullScrenn=false;
    }


    private void enableX5FullscreenFunc() {

        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.holo_red_dark));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (isFullScrenn) {
                    exitFullScreenOnMainThread();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

}
