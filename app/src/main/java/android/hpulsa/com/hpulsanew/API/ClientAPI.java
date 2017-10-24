package android.hpulsa.com.hpulsanew.API;

import android.hpulsa.com.hpulsanew.util.StaticVars;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ozi on 23/10/2017.
 */

public class ClientAPI {
    static StaticVars sv = new StaticVars();
    public static Retrofit getMyRetrofit() {

        String BASE_URL = sv.HOST;

        OkHttpClient okHttpClient = new OkHttpClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.newBuilder().connectTimeout(StaticVars.TIMEOUT, TimeUnit.SECONDS).readTimeout(StaticVars.TIMEOUT, TimeUnit.SECONDS).writeTimeout(StaticVars.TIMEOUT, TimeUnit.SECONDS).build())
                .build();
    }

}
