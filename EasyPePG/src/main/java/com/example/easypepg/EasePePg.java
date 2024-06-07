package com.example.easypepg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easypepg.databinding.ActivityEasePePgBinding;
import com.example.easypepg.modal.ResponseData;
import com.example.easypepg.modal.SDKData;
import com.example.easypepg.retrofit_setup.GetDataService;
import com.example.easypepg.retrofit_setup.RetrofitClientInstance;
import com.example.easypepg.utility.CustomWebViewClient;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.security.MessageDigest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EasePePg extends AppCompatActivity {

    private WebView webView;
    ActivityEasePePgBinding binding;
    private String TAG = "PAYMENT_GATEWAY";
    private static final String URL = "https://pg.eazype.co/pgui/jsp/paymentrequest";
    private static final String txnFailed = "https://pg.eazype.co/pgui/jsp/txncancel";
    String pay_id=null ,
            order_id=null,
            amount=null ,
            txntype=null,
            cust_name=null ,
            cust_street_addresss1=null ,
            cust_zip=null ,
            cust_phone=null ,
            cust_email=null ,
            product_desc=null ,
            currency_code=null ,
            salt=null ,
            return_url=null ;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEasePePgBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            // and get whatever type user account id is
            pay_id = extras.getString("pay_id");
            order_id = extras.getString("order_id");
            amount = extras.getString("amount");
            txntype = extras.getString("txntype");
            cust_name = extras.getString("cust_name");
            cust_street_addresss1 = extras.getString("cust_street_addresss1");
            cust_zip = extras.getString("cust_zip");
            cust_phone = extras.getString("cust_phone");
            cust_email = extras.getString("cust_email");
            product_desc = extras.getString("product_desc");
            currency_code = extras.getString("currency_code");
            return_url = extras.getString("return_url");
            salt = extras.getString("salt");

        }else{
            Toast.makeText(this, "Please Enter Data Correctly", Toast.LENGTH_SHORT).show();
        }
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "shouldOverrideUrlLoading: init");
                String method = request.getMethod();
                Log.d(TAG, "shouldOverrideUrlLoading Request URL:========== " + url);
                Log.d(TAG, "shouldOverrideUrlLoading Request Method:========== " + method);

                if (url.startsWith("tez://upi/pay")||url.startsWith("phonepe://pay")||url.startsWith("paytmmp://pay")||url.startsWith("upi://pay")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    Log.d(TAG, "shouldOverrideUrlLoading: true");
                    return true;
                }
                if(url.startsWith(txnFailed)){
                    binding.webview.setVisibility(View.GONE);
                    try {
                        handleReturnUrl(url);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: "+url);
                Log.d(TAG, "onPageFinished: ");
//                view.loadUrl("javascript:window.Android.processHTML(document.documentElement.outerHTML);");
                if (url.startsWith(URL)) {
                    try {
                        binding.webview.setVisibility(View.GONE);
                        handleReturnUrl(url);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted: "+url);
//                if (url.startsWith(return_url)) {
//                    try {
//                        handleReturnUrl(url);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
            }
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                try {
                    String url =  request.getUrl().toString();
                    if (url.startsWith("tez://upi/pay")||url.startsWith("phonepe://pay")||url.startsWith("paytmmp://pay")||url.startsWith("upi://pay")) {
                        // UPI payment link, launch UPI app
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        view.getContext().startActivity(intent);
                    }
                    return super.shouldInterceptRequest(view, request);
                } catch (Exception e) {
                    e.printStackTrace();
                    return super.shouldInterceptRequest(view, request);
                }
            }
        });

        startTxn();
    }

    void startTxn(){
        if(!pay_id.equals(null)||
                !order_id.equals(null)||
                !amount.equals(null)||
                !txntype.equals(null)||
                !cust_name.equals(null)||
                !cust_street_addresss1.equals(null)||
                !cust_zip.equals(null)||
                !cust_phone.equals(null)||
                !cust_email.equals(null)||
                !product_desc.equals(null)||
                !currency_code.equals(null)||
                !return_url.equals(null)||
                !salt.equals(null)){

        String hashString = "AMOUNT="+amount+"~CURRENCY_CODE="+currency_code+"~CUST_EMAIL="+cust_email+"~CUST_NAME="+cust_name+"~CUST_PHONE="+cust_phone+"~CUST_STREET_ADDRESS1="+cust_street_addresss1+"~CUST_ZIP="+cust_zip+"~ORDER_ID="+order_id+"~PAY_ID="+pay_id+"~PRODUCT_DESC="+product_desc+"~RETURN_URL="+return_url+"~TXNTYPE="+txntype+salt;
        String generatedHash = sha256(hashString);

            String htmlViewString = "<html>\n" +
                    "<head>\n" +
                    "<title>Test</title>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "function submitForm() {\n" +
                    "    document.forms[0].submit();\n" +
                    "}\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body onload=\"submitForm()\">\n" +
                    "<form action=\"https://pg.eazype.co/pgui/jsp/paymentrequest\" method=\"post\"> \n" +
                    "<input type=\"hidden\" name=\"PAY_ID\" value=\""+pay_id+"\"/> \n" +
                    "<input type=\"hidden\" name=\"ORDER_ID\" value=\""+order_id+"\"/> \n" +
                    "<input type=\"hidden\" name=\"AMOUNT\" value=\""+amount+"\"/> \n" +
                    "<input type=\"hidden\" name=\"TXNTYPE\" value=\""+txntype+"\"/> \n" +
                    "<input type=\"hidden\" name=\"CUST_NAME\" value=\""+cust_name+"\"/> \n" +
                    "<input type=\"hidden\" name=\"CUST_STREET_ADDRESS1\" value=\""+cust_street_addresss1+"\"/> \n" +
                    "<input type=\"hidden\" name=\"CUST_ZIP\" value=\""+cust_zip+"\"/> \n" +
                    "<input type=\"hidden\" name=\"CUST_PHONE\" value=\""+cust_phone+"\"/> \n" +
                    "<input type=\"hidden\" name=\"CUST_EMAIL\" value=\""+cust_email+"\"/> \n" +
                    "<input type=\"hidden\" name=\"PRODUCT_DESC\" value=\""+product_desc+"\"/> \n" +
                    "<input type=\"hidden\" name=\"CURRENCY_CODE\" value=\""+currency_code+"\"/> \n" +
                    "<input type=\"hidden\" name=\"RETURN_URL\" value=\""+return_url+"\"/> \n" +
                    "<input type=\"hidden\" name=\"HASH\" value=\""+generatedHash+"\"/> \n" +
                    "</form>\n" +
                    "</body>\n" +
                    "</html>";

            webView.loadDataWithBaseURL(null, htmlViewString, "text/html", "utf-8", null);

        }else {
            Toast.makeText(this, "Please Enter Data Correctly", Toast.LENGTH_SHORT).show();
        }
    }

    public String sha256(final String base) {
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
//            System.out.println();
//            Log.d(TAG, "sha256: "+hexString.toString());
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void handleReturnUrl(String url) throws JSONException {
        // Parse the return URL to extract parameters if needed
        Uri uri = Uri.parse(url);

        // Handle the return URL logic
        Log.d(TAG, "handleReturnUrl: "+url);
        String message = "Returned to app via: " + url;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        SDKData sdkData = new SDKData();
        sdkData.setAMOUNT(amount);
        sdkData.setCURRENCY_CODE(currency_code);
        sdkData.setORDER_ID(order_id);
        sdkData.setPAY_ID(pay_id);
        sdkData.setTXNTYPE(txntype);
        String hashString = "AMOUNT="+amount+"~CURRENCY_CODE="+currency_code+"~ORDER_ID="+order_id+"~PAY_ID="+pay_id+"~TXNTYPE="+txntype+salt;
        String generatedHash = sha256(hashString);

        sdkData.setHASH(generatedHash);

        binding.progressLayout.progressRL.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = getDataService.checkStatus(sdkData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Log.d(TAG, "onResponse: "+response.body().toString());
                    String responseBodyString = null;
                    try {
                        responseBodyString = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Gson gson = new Gson();
                    ResponseData responseData = gson.fromJson(responseBodyString, ResponseData.class);
                    finish();
                    Intent intent = new Intent(EasePePg.this, ResponseActivity.class);
                    intent.putExtra("RESPONSE_DATE_TIME",responseData.getRESPONSE_DATE_TIME());
                    intent.putExtra("RESPONSE_CODE",responseData.getRESPONSE_CODE());
                    intent.putExtra("PG_TXN_MESSAGE",responseData.getPG_TXN_MESSAGE());
                    intent.putExtra("STATUS",responseData.getSTATUS());
                    intent.putExtra("PAY_ID",responseData.getPAY_ID());
                    intent.putExtra("PG_REF_NUM",responseData.getPG_REF_NUM());
                    intent.putExtra("TXN_ID",responseData.getTXN_ID());
                    intent.putExtra("ORDER_ID",responseData.getORDER_ID());
                    intent.putExtra("RESPONSE_MESSAGE",responseData.getRESPONSE_MESSAGE());
                    intent.putExtra("TXNTYPE",responseData.getTXNTYPE());
                    intent.putExtra("HASH",responseData.getHASH());
                    intent.putExtra("amount",(Integer.parseInt(amount)/100)+"");
                    startActivity(intent);
                } else {
                    // Handle error response
                    Toast.makeText(EasePePg.this, "Something went wrong: "+ response.code(), Toast.LENGTH_SHORT).show();
                }
                binding.progressLayout.progressRL.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle request failure
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(EasePePg.this, "Somthing went wrong: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressLayout.progressRL.setVisibility(View.GONE);
            }
        });
    }

    public class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            // Handle the HTML content or extract data from it
            Log.d("HTML", html);
        }
    }
}