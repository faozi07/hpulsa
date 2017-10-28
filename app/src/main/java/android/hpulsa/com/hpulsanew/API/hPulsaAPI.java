package android.hpulsa.com.hpulsanew.API;

import android.hpulsa.com.hpulsanew.util.StaticVars;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
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
    @POST("auth/login") //============ LOGIN ==========
    @FormUrlEncoded
    Call<JsonObject> loginUser(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("auth/check_token") //==================== CEK TOKEN ====================
    Call<JsonObject> cekToken(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token);

    @GET("users/detail") //======================= USER DETAIL =======================
    Call<JsonObject> userProfil(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token);

    @POST("users/register") // ========================== REGISTER ========================
    @FormUrlEncoded
    Call<JsonObject> registerUser(@Header("publickey") String publickey,
                                  @Header("privatekey") String privatekey,
                                  @Field("us_username") String username,
                                  @Field("us_phone") String phone,
                                  @Field("us_email") String email,
                                  @Field("us_password") String password,
                                  @Field("us_password_confirmasi") String passwordConf);

    @GET("verifikasi/sms") //========================= KIRM KODE VERIFIKASI SMS ============================
    Call<JsonObject> kirimKodeVerifSms(@Header("token") String token,@Header("tokenaplikasi") String tokenaplikasi);


    @POST("verifikasi/sms/input") //===================== VERIFIKASI KODE SMS ===================
    Call<JsonObject> verifKode(@Header("token") String token,@Header("tokenaplikasi") String tokenaplikasi,
                               @Header("kode_verifikasi") String kode_verif);

    @GET("akun/riwayat_transaksi") //===================== RIWAYAT TRANSAKSI =============================
    Call<ResponseBody> riwayat(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token);

    @GET("testimoni/get_all") //============================== TESTIMONI =================
    Call<ResponseBody> testimoniAll(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey);

    @POST("testimoni/input_testi")//======================== INPUT TESTIMONI =====================
    @FormUrlEncoded
    Call<ResponseBody> kirimTestimoni(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("pesan") String pesan);

    @POST("pulsa/daftar_product") //========================= DAFTAR PRODUK ALL PULSA =====================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgAllPulsa(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opproduct") String opproduct);


    @POST("pulsa/daftar_product") //======================== DAFTAR PRODUK BY OP ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgByOp(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opproduct") String opproduct,
            @Field("opslug") String opslug);
}
