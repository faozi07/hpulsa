package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Callback;
import retrofit2.Response;

public class Verifikasi extends AppCompatActivity {

    private TextView tNoHp,tEmail,tVerifEmail,tVerifNoHp,btnVerifEmail,btnVerifNoHp;
    private LinearLayout layUtama;
    private hPulsaAPI api;
    private ImageView imgVerifEmail,imgVerifPhone;
    ProgressDialog pLoading;
    String kodeVerif = "";
    AlertDialog theDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifikasi);
        getSupportActionBar().setTitle("Verifikasi Akun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setComponent();
        action();
    }

    private void init() {
        layUtama = (LinearLayout) findViewById(R.id.layUtama);
        tNoHp = (TextView) findViewById(R.id.tNoHp);
        tEmail = (TextView) findViewById(R.id.tEmail);
        tVerifEmail = (TextView) findViewById(R.id.tVerifEmail);
        tVerifNoHp = (TextView) findViewById(R.id.tVerifNoHp);
        btnVerifEmail = (Button) findViewById(R.id.btnVerifEmail);
        btnVerifNoHp = (Button) findViewById(R.id.btnVerifNoHp);
        imgVerifEmail = (ImageView) findViewById(R.id.imgVerifEmail);
        imgVerifPhone = (ImageView) findViewById(R.id.imgVerifPhone);

        api = hPulsaAPI.service.create(hPulsaAPI.class);
        pLoading = new ProgressDialog(this);
        pLoading.setMessage("Mengirim kode verifikasi . . .");
        pLoading.setCancelable(true);
    }
    private void setComponent() {
        SharedPreferences spProfil = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        StaticVars sv = new StaticVars();
        tEmail.setText(spProfil.getString(sv.email,""));
        tNoHp.setText(spProfil.getString(sv.phone,""));
        if (spProfil.getString(sv.verifEmail,"").equals("1")) {
            tVerifEmail.setText("Terverifikasi");
            tVerifEmail.setTextColor(getResources().getColor(R.color.blue));
            btnVerifEmail.setEnabled(false);
            btnVerifEmail.setVisibility(View.GONE);
            imgVerifEmail.setVisibility(View.VISIBLE);
        } else {
            tVerifEmail.setText("Belum Terverifikasi");
            btnVerifEmail.setEnabled(true);
            btnVerifEmail.setVisibility(View.VISIBLE);
            imgVerifEmail.setVisibility(View.GONE);
        }
        if (spProfil.getString(sv.verifPhone,"").equals("1")) {
            tVerifNoHp.setText("Terverifikasi");
            tVerifNoHp.setTextColor(getResources().getColor(R.color.blue));
            btnVerifNoHp.setEnabled(false);
            btnVerifNoHp.setVisibility(View.GONE);
            imgVerifPhone.setVisibility(View.VISIBLE);
        } else{
            tVerifEmail.setText("Belum Terverifikasi");
            btnVerifNoHp.setEnabled(true);
            btnVerifNoHp.setVisibility(View.VISIBLE);
            imgVerifPhone.setVisibility(View.GONE);
        }
    }

    private void action() {
        btnVerifEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Verifikasi.this);
                View dialog_layout = inflater.inflate(R.layout.dialog_verif_email, null);

                AlertDialog.Builder dialogVerifEmail = new AlertDialog.Builder(Verifikasi.this);
                dialogVerifEmail.setView(dialog_layout);
                final AlertDialog theDialog = dialogVerifEmail.create();

                final TextView tKirimKode = (TextView) dialog_layout.findViewById(R.id.tKirimKode);
                tKirimKode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                final EditText eKodeVerif = (EditText) dialog_layout.findViewById(R.id.eKodeVerif);

                FancyButton btnCancel = (FancyButton) dialog_layout.findViewById(R.id.btn_batal);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theDialog.dismiss();
                    }
                });

                FancyButton btnKirim = (FancyButton) dialog_layout.findViewById(R.id.btn_kirim);
                btnKirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!eKodeVerif.getText().toString().equals("")) {
                            kodeVerif = eKodeVerif.getText().toString();

                            theDialog.dismiss();
                        } else {
                            Snackbar.make(layUtama, "Kode Verifikasi Anda belum terisi", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                theDialog.show();
            }
        });

        btnVerifNoHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Verifikasi.this);
                View dialog_layout = inflater.inflate(R.layout.dialog_verif_no_hp, null);

                AlertDialog.Builder dialogVerifNoHp = new AlertDialog.Builder(Verifikasi.this);
                dialogVerifNoHp.setView(dialog_layout);
                theDialog = dialogVerifNoHp.create();

                final TextView tKirimKode = (TextView) dialog_layout.findViewById(R.id.tKirimKode);
                tKirimKode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cekKoneksiNoHp();
                    }
                });
                final EditText eKodeVerif = (EditText) dialog_layout.findViewById(R.id.eKodeVerif);

                FancyButton btnCancel = (FancyButton) dialog_layout.findViewById(R.id.btn_batal);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theDialog.dismiss();
                    }
                });

                FancyButton btnKirim = (FancyButton) dialog_layout.findViewById(R.id.btn_kirim);
                btnKirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!eKodeVerif.getText().toString().equals("")) {
//                            verifKodeNoHp();
                        } else {
                            Snackbar.make(layUtama, "Kode Verifikasi Anda belum terisi", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                theDialog.show();
            }
        });
    }

    private void kirimVerifNoHp() {
        pLoading.show();
        SharedPreferences spProfil = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        StaticVars sv = new StaticVars();
        api.kirimKodeVerifSms(spProfil.getString(sv.token,""),"").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                pLoading.dismiss();
                try {
                    if (response.code() == 200) {
                        String mess = "";
                        JsonObject body = response.body();
                        if (body.has("success")) {
                            String sukses = body.get("success").getAsString();
                            if (!sukses.equals(false) || !sukses.equals("false")) {
                                mess = body.get("message").getAsString();
                                Toast.makeText(Verifikasi.this,mess,Toast.LENGTH_LONG).show();
                                theDialog.dismiss();
                            } else {
                                dialogGagalKode("", mess,"OK");
                            }
                        }
//                            saveLogUser();
                    } else {
                        dialogGagalKode("", "Terjadi kesalahan","OK");
                    }
                } catch (Exception e) {
                    dialogGagalKode("", "Terjadi kesalahan","OK");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                dialogGagalKode("", "Terjadi kesalahan","OK");
                pLoading.dismiss();
            }
        });
    }

    private void kirimVerifEmail() {

    }

    private void cekKoneksiEmail() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            dialogReconnect("Koneksi Tidak Ada","Pastikan internet anda aktif","Terhubung kembali");
        } else {
            kirimVerifEmail();
        }
    }

    private void cekKoneksiNoHp() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            dialogReconnect("Koneksi Tidak Ada","Pastikan internet anda aktif","Terhubung kembali");
        } else {
//            kirimVerifNoHp();
        }
    }

    private void verifKodeNoHp() {
        pLoading.show();
        SharedPreferences spProfil = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        StaticVars sv = new StaticVars();
        api.verifKode(spProfil.getString(sv.token,""),"",kodeVerif)
                .enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                pLoading.dismiss();
                try {
                    if (response.code() == 200) {
                        String mess = "";
                        JsonObject body = response.body();
                        if (body.has("success")) {
                            String sukses = body.get("success").getAsString();
                            mess = body.get("message").getAsString();
                            if (!sukses.equals(false) || !sukses.equals("false")) {
                                Toast.makeText(Verifikasi.this,mess,Toast.LENGTH_LONG).show();
                            } else {
                                dialogGagalKode("", mess,"OK");
                            }
                        }
//                            saveLogUser();
                    } else {
                        dialogGagalKode("", "Terjadi kesalahan","OK");
                    }
                } catch (Exception e) {
                    dialogGagalKode("", "Terjadi kesalahan","OK");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                dialogGagalKode("", "Terjadi kesalahan","OK");
                pLoading.dismiss();
            }
        });
    }

    private void dialogGagalKode(String title, String content,String teksButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Verifikasi.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(teksButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dialogReconnect(String title, String content,String teksButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Verifikasi.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(teksButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        cekKoneksiNoHp();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }
}
