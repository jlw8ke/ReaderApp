package com.cauliflower.readerapp;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jlw8k_000 on 12/1/13.
 */
public class PDFWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
