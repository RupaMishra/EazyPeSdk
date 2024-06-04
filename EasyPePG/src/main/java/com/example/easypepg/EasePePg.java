package com.example.easypepg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easypepg.databinding.ActivityEasePePgBinding;
import com.example.easypepg.utility.CustomWebViewClient;

public class EasePePg extends AppCompatActivity {

    private WebView webView;
    ActivityEasePePgBinding binding;
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
            return_url=null ;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEasePePgBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
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

        }else{
            Toast.makeText(this, "Please Enter Data Correctly", Toast.LENGTH_SHORT).show();
        }
        webView = findViewById(R.id.webview);
        webView.requestFocus();
        webView.getSettings().setGeolocationEnabled(true);
        webView.setSoundEffectsEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebContentsDebuggingEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
//        loadWebView();
        startTxn();
    }

    void startTxn(){
        String htmlViewString = "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<form action=\"https://pg.eazype.co/pgui/jsp/paymentrequest\" method=post> \n" +
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
                "<input type=\"hidden\" name=\"HASH\" \n" +
                "value=\"d2ec9f146c6e802cc54577b06358860687a199679b552341c1c495110f1cb3eb\"/> \n" +
                "<input type=\"submit\" value=\"Click to Pay\"/> \n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
        webView.loadDataWithBaseURL(null, htmlViewString, "text/html", "utf-8", null);
    }
}