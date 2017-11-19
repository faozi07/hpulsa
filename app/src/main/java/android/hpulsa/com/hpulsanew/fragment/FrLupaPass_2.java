package android.hpulsa.com.hpulsanew.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.navigasi.LupaPassword;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrLupaPass_2 extends Fragment {

    FancyButton btnKirim;
    EditText eKodeVerif;
    LinearLayout llayout;

    StaticVars sv = new StaticVars();
    hPulsaAPI api;

    String kodeVerif;
    ProgressDialog pLoading;
    SharedPreferences spLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_lupa_pass_2, container, false);
        setComponent(v);
        init();
        return v;
    }

    private void setComponent(View v) {
        llayout = (LinearLayout) v.findViewById(R.id.llayout);
        btnKirim = (FancyButton) v.findViewById(R.id.btnKirim);
        eKodeVerif = (EditText) v.findViewById(R.id.eKodeVerif);
        spLogin = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    private void init() {
        api = hPulsaAPI.service.create(hPulsaAPI.class);
        pLoading = new ProgressDialog(getActivity());
        pLoading.setMessage("Mengirim kode verifikasi . . .");
        pLoading.setCancelable(true);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kodeVerif = eKodeVerif.getText().toString();
                if (kodeVerif.equals("")) {
                    dialFail("Isi dengan lengkap", "Inputan tidak boleh kosong");
                } else {
                    cekKoneksi();
                }
            }
        });
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Snackbar.make(llayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
        } else {
            verifKode();
        }
    }

    private void verifKode() {
        pLoading = new ProgressDialog(getActivity());
        pLoading.setTitle("Verifikasi kode ...");
        pLoading.show();

        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.verifKodeReset(sv.publickey,sv.privatekey,kodeVerif);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                try {
                    if (response.code() == 200) {

                        boolean sukses;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        sukses = jsonObject.getBoolean("success");
                        if (sukses == true) {
                            if (jsonObject.has("token_for_reset")) {
                                LupaPassword.tokenreset = jsonObject.getString("token_for_reset");
                                LupaPassword.nextPage(2);
                            }
                        } else {
                            dialFail("", "Token gagal dimuat");
                        }

                    } else {
                        dialFail("Gagal memuat data", "Terjadi kesalahan");
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    dialFail("Gagal memuat data", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pLoading.dismiss();
                dialFail("Gagal memuat data", "Terjadi kesalahan");
            }
        });
    }

    private void dialFail(String title, String content) {
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
