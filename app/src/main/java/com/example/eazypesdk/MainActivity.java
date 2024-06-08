package com.example.eazypesdk;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easypepg.EasePePg;
import com.example.easypepg.HelloWorld;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        HelloWorld.TestToast("Test","Test log sdk");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = new Intent(MainActivity.this, EasePePg.class);
        intent.putExtra("pay_id","1224120718175615");
        intent.putExtra("order_id","Test84645");
        intent.putExtra("amount","100");
        intent.putExtra("txntype","SALE");
        intent.putExtra("cust_name","Demo");
        intent.putExtra("cust_street_address1","Gurgaon");
        intent.putExtra("cust_zip","123456");
        intent.putExtra("cust_phone","9911889966");
        intent.putExtra("cust_email","rm@gmail.com");
        intent.putExtra("product_desc","CD Player");
        intent.putExtra("currency_code","356");
        intent.putExtra("hash","09a9fc2cb4956fb34a62db016d95a49ce66d5600f1025faafe22cbc1d245a08c");
        intent.putExtra("return_url","https://apiuat.acepy.in/test");
        startActivity(intent);

    }
}