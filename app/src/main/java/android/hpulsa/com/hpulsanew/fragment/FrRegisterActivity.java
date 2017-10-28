package android.hpulsa.com.hpulsanew.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.navigasi.MenuUtama;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import android.hpulsa.com.hpulsanew.API.hPulsaAPI;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrRegisterActivity extends Fragment {
    private EditText username, noHp, email, pass, passKonf;
    private CheckBox cekSetuju;
    private FancyButton btnDaftar;
    private TextView syaratDanKet;
    private RelativeLayout relativeLayout;

    hPulsaAPI api;
    StaticVars sv = new StaticVars();

    ProgressDialog pLoading;
    String uName, uNoHp, uEmail, uPass, uPassConf, messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        setComponent(rootView);
        return rootView;
    }

    private void setComponent(View v) {
        relativeLayout = (RelativeLayout) v.findViewById(R.id.rLayout);
        pLoading = new ProgressDialog(getActivity());
        pLoading.setMessage("Mendaftarkan . . .");
        pLoading.setCancelable(true);
        api = hPulsaAPI.service.create(hPulsaAPI.class);
        username = (EditText) v.findViewById(R.id.tUsername);
        noHp = (EditText) v.findViewById(R.id.tNoHp);
        email = (EditText) v.findViewById(R.id.tEmail);
        pass = (EditText) v.findViewById(R.id.tPass);
        passKonf = (EditText) v.findViewById(R.id.tPassKonfirm);
        cekSetuju = (CheckBox) v.findViewById(R.id.cekSetuju);
        syaratDanKet = (TextView) v.findViewById(R.id.syaratKetentuan);
        syaratDanKet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSyaratDanKet();
            }
        });
        btnDaftar = (FancyButton) v.findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uName = username.getText().toString();
                uNoHp = noHp.getText().toString();
                uEmail = email.getText().toString();
                uPass = pass.getText().toString();
                uPassConf = passKonf.getText().toString();

                if (uName.equals("") || uNoHp.equals("") || uEmail.equals("") || uPass.equals("") || uPassConf.equals("")) {
                    showDialog("", "Isi data anda dengan lengkap");
                } else if (uName.length()<4) {
                    showDialog("", "Nama pengguna minimal 4 karakter");
                } else if (noHp.length() < 10 || noHp.length() > 13) {
                    showDialog("", "No handphone min 10 angka dan max 13 angka");
                } else if (!uPass.equals(uPassConf)) {
                    showDialog("", "Password tidak cocok, silahkan periksa kembali");
                } else if (uPass.length() < 6 || uPass.length() > 12) {
                    showDialog("", "Kata sandi min 6 karakter dan max 12 karakter");
                } else {
                    if (uNoHp.substring(0,2).equals("62")) {
                        String nomor = uNoHp.substring(2, uNoHp.length());
                        uNoHp = "0" + nomor;
                    } else if (!uNoHp.substring(0,2).equals("08")){
                        showDialog("","Periksa kembali no handphone Anda");
                    }  else {
                        if (cekSetuju.isChecked()) {
                            cekKoneksi();
                        } else {
                            showDialog("", "Anda belum menyetujui syarat dan ketentuan");
                        }
                    }
                }

            }
        });
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Snackbar.make(relativeLayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
        } else {
            doRegist();
        }
    }


    private void doRegist() {
        pLoading.show();
        api.registerUser(sv.publickey,sv.privatekey, uName, uNoHp, uEmail, uPass, uPassConf).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                pLoading.dismiss();
                try {
                    if (response.code() == 200) {
                        JsonObject body = response.body();
                        /*JsonObject messeages = body.getAsJsonObject("message");*/
                        String register, message;
                        register = body.get("register").getAsString();
                        if (register.equals("true")) {
                            message = body.get("message").getAsString();
                            cekLogin();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } else {
                            JsonObject messages = body.get("message").getAsJsonObject();
                            if (messages.has("us_username")) {
                                message = messages.get("us_username").getAsString();
                                showDialog(message, "Silahkan gunakan 'Username' lain untuk mendaftar");
                            } else if (messages.has("us_phone")) {
                                message = messages.get("us_phone").getAsString();
                                showDialog(message, "Silahkan gunakan 'Nomor HP lain' lain untuk mendaftar");
                            } else if (messages.has("us_email")) {
                                message = messages.get("us_email").getAsString();
                                showDialog(message, "Silahkan gunakan 'Email' lain untuk mendaftar");
                            }
                        }
                    /*saveLogin(id, namadepan, namabelakang, ttl);*/
                    } else if (response.code() == 400 || response.code() == 401) {
                        showDialog("Gagal Daftar", "Terjadi kesalahan");
                    }
                } catch (Exception e) {
                    showDialog("Gagal Daftar", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pLoading.dismiss();
                showDialog("Gagal Daftar", "Terjadi kesalahan");
            }
        });
    }

    private void cekLogin() {
        pLoading.show();
        username.setText("");
        noHp.setText("");
        email.setText("");
        pass.setText("");
        passKonf.setText("");
        api.loginUser(sv.publickey,sv.privatekey,uName, uPass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    pLoading.dismiss();
                    if (response.code() == 200) {
                        JsonObject body = response.body();
                        sv.loginStat = body.get("login").getAsString();
//                        sv.suksesLogin = body.get("succeess").getAsString();
                        messages = body.get("message").getAsString();
                        sv.token = body.get("akses_token").getAsString();
                        if (sv.loginStat.equals("false")) {
                            showDialog("Login Gagal", messages);
                        } else {
                            api.userProfil(sv.publickey,sv.privatekey,sv.token).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    try {
                                        if (response.code() == 200) {
                                            JsonObject body = response.body();
                                            if (body.has("message")) {
                                                String mess;
                                                mess = body.get("message").getAsString();
                                                showDialog("Gagal tampil data", mess);
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
                                                if (body.has("phone_verified")) {
                                                    sv.verifPhone = body.get("phone_verified").getAsString();
                                                }
                                                if (body.has("email_verified")) {
                                                    sv.verifEmail = body.get("email_verified").getAsString();
                                                }

                                                if (body.has("timestamp")) {
                                                    body.get("timestamp");
                                                }
                                                if (body.has("exp")) {
                                                    body.get("exp");
                                                }
                                                Toast.makeText(getActivity(), "Berhasil muat data", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                                startActivity(new Intent(getActivity(), MenuUtama.class));
                                            }
                                        } else if (response.code() == 400 || response.code() == 401) {
                                            showDialog("Gagal tampil data", "Terjadi kesalahan");
                                        }
                                    } catch (Exception e) {
                                        showDialog("Gagal", "Terjadi kesalahan");
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    showDialog("Gagal", "Terjadi kesalahan");
                                }
                            });
                        }
                    /*saveLogin(id, namadepan, namabelakang, ttl);*/
                    } else if (response.code() == 400 || response.code() == 401) {
                        showDialog("Login gagal", "Periksa kembali username/password anda");
                    }
                } catch (Exception e) {
                    pLoading.dismiss();
                    showDialog("Gagal", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pLoading.dismiss();
                showDialog("Gagal", "Terjadi kesalahan");
            }
        });
    }

    private void showDialog(String title, String ket) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setMessage(ket)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void setSyaratDanKet() {
        String url = "http://kasirpulsaonline.com/tos";
        CookieSyncManager.createInstance(getActivity());
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().setAcceptCookie(true);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
