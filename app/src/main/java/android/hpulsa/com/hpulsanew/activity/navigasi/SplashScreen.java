package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.TrialHabis;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PulsaHP;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Callback;
import retrofit2.Response;

import android.hpulsa.com.hpulsanew.API.hPulsaAPI;

import java.util.Calendar;

public class SplashScreen extends AppCompatActivity {

    ProgressDialog pLoading;
    StaticVars sv = new StaticVars();
    hPulsaAPI api;
    ImageView logo;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logo = (ImageView) findViewById(R.id.logo);
        api = hPulsaAPI.service.create(hPulsaAPI.class);
        pLoading = new ProgressDialog(this);
        pLoading.setMessage("Memuat data . . .");
        pLoading.setCancelable(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoomin);
                logo.setAnimation(animation);
                logo.setVisibility(View.VISIBLE);
                /*Pair<View, String> pair = Pair.create(findViewById(R.id.logo), "logo");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this, pair);*/
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                token = sharedPreferences.getString(sv.token, "");
                if (token.equals("")) {
                    startActivity(new Intent(SplashScreen.this, SignActivity.class)/*, optionsCompat.toBundle()*/);
                } else {
                    cekKoneksi();
                }

                this.finish();
            }

            private void finish() {
            }
        }, 2000);
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            dialogReconnect("Koneksi Tidak Ada", "Pastikan internet anda aktif", "Terhubung kembali");
        } else {
            cekToken();
            /*final Calendar c = Calendar.getInstance();
            int mYear, mMonth, mDay;
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH) + 1;
            mDay = c.get(Calendar.DAY_OF_MONTH);
            if (mYear >= 2018 || mMonth >= 11 || mDay >= 18) {
                finish();
                startActivity(new Intent(SplashScreen.this, TrialHabis.class));
            } else {
                cekToken();
            }*/
//            getData();
        }
    }

    private void cekToken() {
        pLoading.show();
        api.cekToken(token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                pLoading.dismiss();

                if (response.code() == 200) {
                    String mess = "";
                    JsonObject body = response.body();
                    if (body.has("exp")) {
                        String exp = body.get("exp").getAsString();
                        if (!exp.equals(false) || !exp.equals("false")) {
                            getData();
                        } else {
                            dialogLoginBack("Sesi anda telah habis", "Silahkan login kembali", "OK");
                        }
                    }
//                            saveLogUser();
                } else if (response.code() == 401) {
                    dialogLoginBack("Sesi anda telah habis", "Silahkan login kembali", "OK");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                dialogReconnect("Gagal", "Terjadi kesalahan", "OK");
                pLoading.dismiss();
            }
        });
    }

    private void getData() {
        pLoading.show();
        api.userProfil(token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                pLoading.dismiss();

                if (response.code() == 200) {
                    String mess = "";
                    JsonObject body = response.body();
//                                            JsonObject body = body.get("bodyentifier").getAsJsonObject();
                    if (body.has("message")) {
                        mess = body.get("message").getAsString();
                        dialogLoginBack("Gagal Login", mess, "OK");
                    } else {
                        if (body.has("us_id")) {
                            sv.uid = body.get("us_id").getAsString();
                        }
                        if (body.has("us_username")) {
                            sv.username = body.get("us_username").getAsString();
                        }
                        if (body.has("us_name")) {
                            sv.name = body.get("us_name").getAsString();
                        }
                        if (body.has("us_email")) {
                            sv.email = body.get("us_email").getAsString();
                        }
                        if (body.has("us_gender")) {
                            sv.gender = body.get("us_gender").getAsString();
                        }
                        if (body.has("us_location")) {
                            sv.location = body.get("us_location").getAsString();
                        }
                        if (body.has("us_phone")) {
                            sv.phone = body.get("us_phone").getAsString();
                        }
                        if (body.has("us_balance")) {
                            sv.balance = body.get("us_balance").getAsString();
                        }
                        if (body.has("us_verified")) {
                            sv.verified = body.get("us_verified").getAsString();
                        }
                        if (body.has("us_rights")) {
                            sv.rights = body.get("us_rights").getAsString();
                        }
                        if (body.has("us_block")) {
                            sv.block = body.get("us_block").getAsString();
                        }
                        if (body.has("us_data")) {
                            sv.data = body.get("us_data").getAsString();
                        }
                        if (body.has("us_reseller")) {
                            sv.reseller = body.get("us_reseller").getAsString();
                        }
                        if (body.has("us_reseller_api")) {
                            sv.reseller_api = body.get("us_reseller_api").getAsString();
                        }
                        if (body.has("us_telegram_id")) {
                            sv.telegram_id = body.get("us_telegram_id").getAsString();
                        }
                        if (body.has("us_telegram_username")) {
                            sv.telegram_username = body.get("us_telegram_username").getAsString();
                        }
                        if (body.has("us_telegram_state")) {
                            sv.telegram_state = body.get("us_telegram_state").getAsString();
                        }
                        if (body.has("us_smstrx")) {
                            sv.sms_trx = body.get("us_smstrx").getAsString();
                        }
                        if (body.has("us_total_order")) {
                            sv.total_order = body.get("us_total_order").getAsString();
                        }
                        if (body.has("us_lastdate")) {
                            sv.last_date = body.get("us_lastdate").getAsString();
                        }
                        if (body.has("us_regdate")) {
                            sv.reg_date = body.get("us_regdate").getAsString();
                        }

                        if (body.has("via")) {
                            sv.via = body.get("via").getAsString();
                        }
                        if (body.has("logo")) {
                            sv.logo = body.get("logo").getAsString();
                        }

                        startActivity(new Intent(SplashScreen.this, MenuUtama.class));
//                            saveLogUser();
                    }
                } else {
                    dialogLoginBack("Gagal tampil data", "Silahkan login kembali", "OK");
                }

            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                dialogReconnect("Gagal", "Terjadi kesalahan", "OK");
                pLoading.dismiss();
            }
        });
    }

    private void dialog(String title, String content, String teksButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(teksButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(SplashScreen.this, MenuUtama.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dialogLoginBack(String title, String content, String teksButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(teksButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        startActivity(new Intent(SplashScreen.this, SignActivity.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dialogReconnect(String title, String content, String teksButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(teksButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        cekKoneksi();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
