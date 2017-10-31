package android.hpulsa.com.hpulsanew.API;

import android.hpulsa.com.hpulsanew.util.StaticVars;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
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
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    OkHttpClient client = new OkHttpClient();

    Retrofit service = new Retrofit.Builder()
            .baseUrl(StaticVars.HOST)/*
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))*/
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
    Call<JsonObject> kirimKodeVerifSms(@Header("publickey") String publickey,
                                       @Header("privatekey") String privatekey,
                                       @Header("tokenuser") String token);


    @POST("verifikasi/sms/input") //===================== VERIFIKASI KODE SMS ===================
    @FormUrlEncoded
    Call<JsonObject> verifKode(@Header("publickey") String publickey,
                               @Header("privatekey") String privatekey,
                               @Header("tokenuser") String token,
                               @Field("kodeverifikasi") String kode_verif);

    @GET("verifikasi/email_get") //========================= KIRM KODE VERIFIKASI EMAIL ============================
    Call<JsonObject> kirimKodeVerifEmail(@Header("publickey") String publickey,
                                       @Header("privatekey") String privatekey,
                                       @Header("tokenuser") String token);

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

    @GET("payment/listing") //============================== LIST BANK =================
    Call<ResponseBody> listBank(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey);

    @POST("pulsa/order_pulsa") //======================== TRANSAKSI PULSA ======================
    @FormUrlEncoded
    Call<ResponseBody> trxPulsa(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran,
            @Field("void") String vo_id);

    @POST("paket_internet/order") //======================== TRANSAKSI INTERNET PAKET DATA ======================
    @FormUrlEncoded
    Call<ResponseBody> trxInternet(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran,
            @Field("void") String vo_id);
}
