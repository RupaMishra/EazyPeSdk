package com.example.easypepg;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.easypepg.databinding.ActivityResponseBinding;
import com.example.easypepg.modal.ResponseData;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseActivity extends AppCompatActivity {

    ActivityResponseBinding binding;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResponseBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        ResponseData responseData = new ResponseData();
        String amount = "0";
        if (extras != null) {
            responseData.setRESPONSE_CODE(extras.getString("RESPONSE_CODE"));
            responseData.setRESPONSE_DATE_TIME(extras.getString("RESPONSE_DATE_TIME"));
            responseData.setPG_TXN_MESSAGE(extras.getString("PG_TXN_MESSAGE"));
            responseData.setSTATUS(extras.getString("STATUS"));
            responseData.setPAY_ID(extras.getString("PAY_ID"));
            responseData.setPG_REF_NUM(extras.getString("PG_REF_NUM"));
            responseData.setTXN_ID(extras.getString("TXN_ID"));
            responseData.setORDER_ID(extras.getString("ORDER_ID"));
            responseData.setRESPONSE_MESSAGE(extras.getString("RESPONSE_MESSAGE"));
            responseData.setTXNTYPE(extras.getString("TXNTYPE"));
            responseData.setHASH(extras.getString("HASH"));
            amount = extras.getString("amount");
            // and get whatever type user account id is
        }
        Log.d("response ", "onCreate: "+amount);

        List<Map.Entry<String,Object>> data = convertToKeyValuePairs(responseData);

        if(!responseData.getRESPONSE_CODE().equals("000")){
            binding.ivCheck.setVisibility(View.GONE);
            binding.ivClear.setVisibility(View.VISIBLE);
            binding.textSuccess.setVisibility(View.GONE);
            binding.textFail.setVisibility(View.VISIBLE);
            binding.btn.setBackgroundColor(Color.parseColor("#FFF34D4D"));
        } else {
            binding.ivCheck.setVisibility(View.VISIBLE);
            binding.ivClear.setVisibility(View.GONE);
            binding.textSuccess.setVisibility(View.VISIBLE);
            binding.textFail.setVisibility(View.GONE);
            binding.btn.setBackgroundColor(Color.parseColor("#518f6f"));
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(data);
        binding.recyclerView.setAdapter(adapter);
        binding.tvAmtVal.setText("₹"+amount);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                binding.timer.setText(f.format(min) + ":" + f.format(sec));
            }
            public void onFinish() {
                binding.timer.setText("00:00");
            }
        }.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 60000);

    }

    public static List<Map.Entry<String, Object>> convertToKeyValuePairs(ResponseData model) {
        List<Map.Entry<String, Object>> keyValuePairs = new ArrayList<>();

        // Get all fields of the class (including private fields)
        Field[] fields = model.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            try {
                String key = field.getName();
                Object value = field.get(model);
                keyValuePairs.add(new AbstractMap.SimpleEntry<>(key, value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return keyValuePairs;
    }
}

