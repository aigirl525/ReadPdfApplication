package com.example.readpdfapplication;


import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity {
    SDKWebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        //WebView加载显示本地html文件
        webView =  this.findViewById(R.id.office);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                try {
                    if(url.endsWith(".pdf")){
                        //进行下载等相关操作
                        //download();
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.loadUrl(url);
                return true;
            }

        });
        webView.loadUrl("file:///android_asset/esb_goodopen_provision/esb_goodopen_provision.html");
        drawcicle();
    }

    private void drawcicle(){
        final ObjectAnimator[] res = new ObjectAnimator[1];
            res[0] = ObjectAnimator.ofInt(webView, "scrollY",webView.getScrollY(), 1000);
            res[0].setStartDelay(200);
            res[0].setDuration(500);
            res[0].setStartDelay(2000);
            res[0].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    runOnUiThread(() -> webView.drawOvalByPos(1020,
                            1000,
                            100,
                            2000));
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        res[0].start();
    }



    private void requestPermission() {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}