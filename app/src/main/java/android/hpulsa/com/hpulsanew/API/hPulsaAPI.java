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

    @POST("akun/riwayat_transaksi") //===================== RIWAYAT TRANSAKSI =============================
    @FormUrlEncoded
    Call<ResponseBody> riwayat(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("limit") int limit,
            @Field("offset") int offset);

    @POST("akun/riwayat_transaksi") //===================== RIWAYAT TRANSAKSI BY PHONE =============================
    @FormUrlEncoded
    Call<ResponseBody> riwayatByPhone(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("hp") String noHp);

    @POST("deposit/riwayat") //===================== RIWAYAT DEPOSIT =============================
    @FormUrlEncoded
    Call<ResponseBody> riwayatDeposit(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("offset") int offset,
            @Field("limit") int limit);

    @POST("testimoni/get_all") //============================== TESTIMONI =================
    @FormUrlEncoded
    Call<ResponseBody> testimoniAll(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("limit") int limit,
            @Field("offset") int offset);

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


    @POST("pulsa/daftar_product") //======================== DAFTAR HARGA PULSA ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgByOp(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opproduct") String opproduct,
            @Field("opslug") String opslug);

    @POST("pln/daftar_product") //======================== DAFTAR HARGA TOKEN PLN ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgTokenPln(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opslug") String opslug,
            @Field("opproduct") String opproduct);

    @POST("pln/daftar_product") //======================== DAFTAR HARGA GO-PAY ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgGoPay(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opslug") String opslug,
            @Field("opproduct") String opproduct);

    @POST("game/daftar_product") //======================== DAFTAR HARGA V.GAME ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgVoGame(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opslug") String opslug,
            @Field("opproduct") String opproduct);

    @POST("blackberry/daftar_product") //======================== DAFTAR HARGA PAKET BBM ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgBbm(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opslug") String opslug,
            @Field("opproduct") String opproduct);


    @POST("telpon_sms/daftar_product") //======================== DAFTAR HARGA PAKET TELPON DAN SMS ======================
    @FormUrlEncoded
    Call<ResponseBody> daftarHrgTelpSms(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("opslug") String opslug,
            @Field("opproduct") String opproduct);

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
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id);

    @POST("paket_internet/order") //======================== TRANSAKSI INTERNET PAKET DATA ======================
    @FormUrlEncoded
    Call<ResponseBody> trxInternet(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id);

    @POST("blackberry/order") //======================== TRANSAKSI BB ======================
    @FormUrlEncoded
    Call<ResponseBody> trxBB(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id);

    @POST("gojek/order") //======================== TRANSAKSI GO-JEK ======================
    @FormUrlEncoded
    Call<ResponseBody> trxGojek(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id);

    @POST("pln/order") //======================== TRANSAKSI TOKEN PLN ======================
    @FormUrlEncoded
    Call<ResponseBody> trxTokenPLN(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id,
            @Field("idpelanggan") String idPelanggan);

    @POST("game/order") //======================== TRANSAKSI VOUCHER GAME ======================
    @FormUrlEncoded
    Call<ResponseBody> trxGame(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id);

    @POST("telpon_sms/order") //======================== TRANSAKSI TELPON DAN SMS ======================
    @FormUrlEncoded
    Call<ResponseBody> trxTelpSms(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("nomorhp") String nomorhp,
            @Field("trpembayaran") String trpembayaran, //balance atau ID bank
            @Field("void") String vo_id);

    @POST("deposit/tambah") //======================== TRANSAKSI TAMBAH DEPOSIT ======================
    @FormUrlEncoded
    Call<ResponseBody> isiDeposit(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("tokenuser") String token,
            @Field("deposit") String deposit,
            @Field("trpembayaran") int trpembayaran); //ID bank

    @POST("auth/forgot_password") //======================== LUPA PASSWORD 1 ======================
    @FormUrlEncoded
    Call<ResponseBody> kirimKodeReset(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("username") String username,
            @Field("email") String email);

    @POST("auth/get_token_reset") //======================== LUPA PASSWORD 2 ======================
    @FormUrlEncoded
    Call<ResponseBody> verifKodeReset(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Field("resetcode") String reset_code);

    @POST("auth/reset_pass") //======================== LUPA PASSWORD 3 ======================
    @FormUrlEncoded
    Call<ResponseBody> newPass(
            @Header("publickey") String publickey,
            @Header("privatekey") String privatekey,
            @Header("resettoken") String resettoken,
            @Field("password1") String password1,
            @Field("password2") String password2);


}
