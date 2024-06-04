package com.example.easypepg.utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CustomWebViewClient extends WebViewClient {

    private static final String TAG = "SDK_WEBVIEW_CLIENT";
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d(TAG, "Loading URL: " + url);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        Log.d(TAG, "shouldOverrideUrlLoading: init");
        if (url.startsWith("tez://upi/pay")||url.startsWith("phonepe://pay")||url.startsWith("paytmmp://pay")||url.startsWith("upi://pay")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            Log.d(TAG, "shouldOverrideUrlLoading: true");
            return true;
        } else {
            Log.d(TAG, "shouldOverrideUrlLoading: false");
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        try {
            String url =  request.getUrl().toString();
            String method = request.getMethod();
            Log.d(TAG, "Request URL:========== " + url);
            Log.d(TAG, "Request Method:========== " + method);
            Log.d(TAG, "Request Headers:========== " + request.getRequestHeaders());

            if (url.startsWith("tez://upi/pay")||url.startsWith("phonepe://pay")||url.startsWith("paytmmp://pay")||url.startsWith("upi://pay")) {
                // UPI payment link, launch UPI app
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return super.shouldInterceptRequest(view, request);
            }else {
                // For debugging purposes, capture the request body if needed
                // Note: WebView does not provide direct access to the request body
                // but we can capture responses

                WebResourceResponse response = super.shouldInterceptRequest(view, request);
                if (response != null) {
                    // Log the response headers
                    Log.d(TAG, "Response Headers: " + response.getResponseHeaders());

                    // Log the response body
                    String responseBody = streamToString(response.getData());
                    Log.d(TAG, "Response Body: " + responseBody);
                }
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.shouldInterceptRequest(view, request);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private String streamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}
