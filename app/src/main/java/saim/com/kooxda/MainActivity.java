package saim.com.kooxda;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imgExit;

    LinearLayout layoutProgress;
    TextView layoutProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //progressDialog = new ProgressDialog(this);
        //progressDialog.setCanceledOnTouchOutside(false);

        layoutProgress = (LinearLayout) findViewById(R.id.layoutProgress);
        layoutProgressText = (TextView) findViewById(R.id.layoutProgressText);

        imgExit = (ImageView) findViewById(R.id.imgExit);
        mWebView = (WebView) findViewById(R.id.webView);

        int scale = (int) (112 * mWebView.getScale());
        mWebView.setInitialScale(scale);

        renderWebPage("http://www.kooxda.com");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                renderWebPage("http://www.kooxda.com");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog();
            }
        });
    }

    protected void renderWebPage(String urlToRender){
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new MyBrowser());
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                layoutProgressText.setText(newProgress + "%");
                mWebView.setEnabled(false);
                if (newProgress == 100) {
                    mWebView.setEnabled(true);
                    layoutProgress.setVisibility(View.GONE);
                }
            }
        });

        mWebView.loadUrl(urlToRender);
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("SAIM ANDROID M", url);
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("SAIM ANDROID N", request.getUrl().toString());
            view.loadUrl(String.valueOf(request.getUrl()));
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            layoutProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        ExitDialog();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public void ExitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit kooxda!");

        alertDialogBuilder.setMessage("Are you sure want to close kooxda?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
