package android.hpulsa.com.hpulsanew.API;

import android.hpulsa.com.hpulsanew.util.StaticVars;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ozi on 23/09/2017.
 */

public interface hPulsaAPI {
    Retrofit service = new Retrofit.Builder()
            .baseUrl(StaticVars.HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @POST("auth/login")
    @FormUrlEncoded
    Call<JsonObject> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("auth/ceck_token")
    Call<JsonObject> cekToken(@Header("token") String token);

    @GET("users/detail")
    Call<JsonObject> userProfil(@Header("token") String token);

    @POST("users/register")
    @FormUrlEncoded
    Call<JsonObject> registerUser(@Header("tokenaplikasi") String token,
                                  @Field("us_username") String username,
                                  @Field("us_phone") String phone,
                                  @Field("us_email") String email,
                                  @Field("us_password") String password,
                                  @Field("us_password_confirmasi") String passwordConf);

    @GET("verifikasi/sms")
    Call<JsonObject> kirimKodeVerifSms(@Header("token") String token,@Header("tokenaplikasi") String tokenaplikasi);


    @POST("verifikasi/sms/input")
    Call<JsonObject> verifKode(@Header("token") String token,@Header("tokenaplikasi") String tokenaplikasi,
                               @Header("kode_verifikasi") String kode_verif);
}
