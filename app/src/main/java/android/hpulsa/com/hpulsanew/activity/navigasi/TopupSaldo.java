package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.adapter.HargaProdukAdapter;
import android.hpulsa.com.hpulsanew.model.modListBank;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopupSaldo extends AppCompatActivity {

    private Spinner spinBank;
    private FancyButton btnDeposit;
    private EditText eNomDeposit;
    private ArrayAdapter<String> arrListBankAdapter;
    private ArrayList<modListBank> arrayListBank = new ArrayList<modListBank>();
    private ArrayList<String> arrayListNamaBank = new ArrayList<String>();
    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_saldo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Deposit Saldo");

        declaration();
        action();
        getListBank();
    }

    private void declaration() {
        spinBank = (Spinner) findViewById(R.id.spinBank);
        eNomDeposit = (EditText) findViewById(R.id.eNominalDeposit);
        btnDeposit = (FancyButton) findViewById(R.id.btnDeposit);
    }

    private void action() {
        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getListBank() {
        pLoading = new ProgressDialog(TopupSaldo.this);
        pLoading.setTitle("Memuat data Bank...");
        pLoading.show();

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.listBank(sv.publickey, sv.privatekey);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                try {
                    if (response.code() == 200) {

                        JSONArray responseArray = new JSONArray(response.body().string());
                        Log.d("Object ", "");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);

                            modListBank mr = new modListBank();
                            mr.setId(data.getInt("id"));
                            mr.setNamaBank(data.getString("nama"));
                            mr.setKeyBank(data.getString("key"));
                            mr.setNamaRek(data.getString("acc_name"));
                            mr.setNoRek(data.getString("acc_num"));

                            arrayListBank.add(mr);
                        }
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);
                            String arrBank = data.getString("nama");

                            arrayListNamaBank.add(arrBank);
                        }
                        Log.d("arrListBank ",String.valueOf(arrayListNamaBank));
                        arrListBankAdapter = new ArrayAdapter<String>(TopupSaldo.this,
                                android.R.layout.simple_list_item_1,arrayListNamaBank);
                        spinBank.setAdapter(arrListBankAdapter);

                    } else {

                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {pLoading.dismiss();}
        });
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
    private void dialogGagalLoad(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TopupSaldo.this);
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
