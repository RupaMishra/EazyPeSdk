package com.example.easypepg.retrofit_setup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://pg.eazype.co/pgws/";
//    private static final String BASE_URL = "https://pg.eazype.co/pgui/jsp/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Create a logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create an OkHttpClient and add the interceptor
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            // Create a Gson instance with lenient mode enabled
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Build the Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
