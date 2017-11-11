package android.hpulsa.com.hpulsanew.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.navigasi.LupaPassword;
import android.hpulsa.com.hpulsanew.activity.navigasi.MenuUtama;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Callback;
import retrofit2.Response;

import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.captcha.TextCaptcha;

public class FrLoginActivity extends Fragment {
    FancyButton btnMasuk;
    TextView tLupaPass;
    EditText eUsername, ePass;
    private LinearLayout llayout;

    StaticVars sv = new StaticVars();
    hPulsaAPI api;

    String username, password, message;
    ProgressDialog pLoading;
    int numberOfCaptchaFalse = 1;

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        setComponent(v);
        init();
        return v;
    }

    private void setComponent(View v) {
        llayout = (LinearLayout) v.findViewById(R.id.llayout);
        btnMasuk = (FancyButton) v.findViewById(R.id.btnMasuk);
        tLupaPass = (TextView) v.findViewById(R.id.teksLupaPass);
        eUsername = (EditText) v.findViewById(R.id.eUsername);
        ePass = (EditText) v.findViewById(R.id.ePassword);
    }

    private void init() {
        api = hPulsaAPI.service.create(hPulsaAPI.class);
        pLoading = new ProgressDialog(getActivity());
        pLoading.setMessage("Memuat data . . .");
        pLoading.setCancelable(true);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = eUsername.getText().toString();
                password = ePass.getText().toString();
                if (username.equals("") || password.equals("")) {
                    dialogGagalLogin("Isi dengan lengkap", "Inputan tidak boleh kosong");
                } else {
                    cekKoneksi();
                }
            }
        });

        tLupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LupaPassword.class));
            }
        });
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Snackbar.make(llayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
        } else {
            cekLogin();
        }
    }

    private void cekLogin() {
        pLoading.show();
        eUsername.setText("");
        ePass.setText("");
        api.loginUser(sv.publickey,sv.privatekey,username, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    pLoading.dismiss();
                    if (response.code() == 200) {
                        JsonObject body = response.body();
                        sv.loginStat = body.get("login").getAsString();
//                        sv.suksesLogin = body.get("succeess").getAsString();
                        message = body.get("message").getAsString();
                        final String token = body.get("token_users").getAsString();
                        if (sv.loginStat.equals("false")) {
                            dialogGagalLogin("Login Gagal", message);
                        } else {
                            api.userProfil(sv.publickey,sv.privatekey,token).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                                    try {
                                        if (response.code() == 200) {
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            String mess = "";
                                            /*final String bodys = response.body().toString(); // Or use response.raw(), if want raw response string
                                            JsonParser jsonParser = new JsonParser();
                                            JsonObject body = jsonParser.parse(bodys).getAsJsonObject();*/
                                            /*JsonObject body = response.body();
                                            JsonPrimitive prim=(JsonPrimitive)body.get("");
                                            String className=prim.getAsString();*/
                                            JsonObject body = response.body();
//                                            JsonObject body = body.get("bodyentifier").getAsJsonObject();
                                            if (body.has("message")) {
                                                mess = body.get("message").getAsString();
                                                dialogGagalLogin("Gagal Login", mess);
                                            } else {
                                                editor.putString(sv.token, token);

                                                if (body.has("us_id")) {
                                                    sv.uid = body.get("us_id").getAsString();
                                                    editor.putString(sv.uid, body.get("us_id").getAsString());
                                                }
                                                if (body.has("us_username")) {
                                                    sv.username = body.get("us_username").getAsString();
                                                    editor.putString(sv.username, body.get("us_username").getAsString());
                                                }
                                                if (body.has("us_name")) {
                                                    sv.name = body.get("us_name").getAsString();
                                                    editor.putString(sv.name, body.get("us_name").getAsString());
                                                }
                                                if (body.has("us_email")) {
                                                    sv.email = body.get("us_email").getAsString();
                                                    editor.putString(sv.email, body.get("us_email").getAsString());
                                                }
                                                if (body.has("us_gender")) {
                                                    sv.gender = body.get("us_gender").getAsString();
                                                    editor.putString(sv.gender, body.get("us_gender").getAsString());
                                                }
                                                if (body.has("us_location")) {
                                                    sv.location = body.get("us_location").getAsString();
                                                    editor.putString(sv.location, body.get("us_location").getAsString());
                                                }
                                                if (body.has("us_phone")) {
                                                    sv.phone = body.get("us_phone").getAsString();
                                                    editor.putString(sv.phone, body.get("us_phone").getAsString());
                                                }
                                                if (body.has("us_balance")) {
                                                    sv.balance = body.get("us_balance").getAsString();
                                                    editor.putString(sv.balance, body.get("us_balance").getAsString());
                                                }
                                                if (body.has("us_rights")) {
                                                    sv.rights = body.get("us_rights").getAsString();
                                                    editor.putString(sv.rights, body.get("us_rights").getAsString());
                                                }
                                                if (body.has("us_block")) {
                                                    sv.block = body.get("us_block").getAsString();
                                                    editor.putString(sv.block, body.get("us_block").getAsString());
                                                }
                                                if (body.has("us_data")) {
                                                    sv.data = body.get("us_data").getAsString();
                                                    editor.putString(sv.data, body.get("us_data").getAsString());
                                                }
                                                if (body.has("us_reseller")) {
                                                    sv.reseller = body.get("us_reseller").getAsString();
                                                    editor.putString(sv.reseller, body.get("us_reseller").getAsString());
                                                }
                                                if (body.has("us_reseller_api")) {
                                                    sv.reseller_api = body.get("us_reseller_api").getAsString();
                                                    editor.putString(sv.reseller_api, body.get("us_reseller_api").getAsString());
                                                }
                                                if (body.has("us_telegram_id")) {
                                                    sv.telegram_id = body.get("us_telegram_id").getAsString();
                                                    editor.putString(sv.telegram_id, body.get("us_telegram_id").getAsString());
                                                }
                                                if (body.has("us_telegram_username")) {
                                                    sv.telegram_username = body.get("us_telegram_username").getAsString();
                                                    editor.putString(sv.telegram_username, body.get("us_telegram_username").getAsString());
                                                }
                                                if (body.has("us_telegram_state")) {
                                                    sv.telegram_state = body.get("us_telegram_state").getAsString();
                                                    editor.putString(sv.telegram_state, body.get("us_telegram_state").getAsString());
                                                }
                                                if (body.has("us_smstrx")) {
                                                    sv.sms_trx = body.get("us_smstrx").getAsString();
                                                    editor.putString(sv.sms_trx, body.get("us_smstrx").getAsString());
                                                }
                                                if (body.has("us_total_order")) {
                                                    sv.total_order = body.get("us_total_order").getAsString();
                                                    editor.putString(sv.total_order, body.get("us_total_order").getAsString());
                                                }
                                                if (body.has("us_lastdate")) {
                                                    sv.last_date = body.get("us_lastdate").getAsString();
                                                    editor.putString(sv.last_date, body.get("us_lastdate").getAsString());
                                                }
                                                if (body.has("us_regdate")) {
                                                    sv.reg_date = body.get("us_regdate").getAsString();
                                                    editor.putString(sv.reg_date, body.get("us_regdate").getAsString());
                                                }

                                                if (body.has("via")) {
                                                    sv.via = body.get("via").getAsString();
                                                    editor.putString(sv.via, body.get("via").getAsString());
                                                }
                                                if (body.has("logo")) {
                                                    sv.logo = body.get("logo").getAsString();
                                                    editor.putString(sv.logo, body.get("logo").getAsString());
                                                }
                                                if (body.has("phone_verified")) {
                                                    sv.verifPhone = body.get("phone_verified").getAsString();
                                                    editor.putString(sv.verifPhone, body.get("phone_verified").getAsString());
                                                }
                                                if (body.has("email_verified")) {
                                                    sv.verifEmail = body.get("email_verified").getAsString();
                                                    editor.putString(sv.verifEmail, body.get("email_verified").getAsString());
                                                }

                                                editor.apply();

                                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                                startActivity(new Intent(getActivity(), MenuUtama.class));
                                            }
                                        } else if (response.code() == 400 || response.code() == 401) {
                                            dialogGagalLogin("Gagal tampil data", "Terjadi kesalahan");
                                        }
                                    } catch (Exception e) {
                                        dialogGagalLogin("Gagal", "Terjadi kesalahan");
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                                    dialogGagalLogin("Gagal", "Terjadi kesalahan");
                                }
                            });
                        }
//                    saveLogin(id, namadepan, namabelakang, ttl);
                    } else if (response.code() == 400 || response.code() == 401) {
                        dialogGagalLogin("Login gagal", "Periksa kembali username/password anda");
                    }
                } catch (Exception e) {
                    pLoading.dismiss();
                    dialogGagalLogin("Gagal", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                pLoading.dismiss();
                dialogGagalLogin("Gagal", "Terjadi kesalahan");
            }
        });
    }

    private void dialogGagalLogin(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
