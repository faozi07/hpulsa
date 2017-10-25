package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.adapter.HargaProdukAdapter;
import android.hpulsa.com.hpulsanew.model.modHrgProduk;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

public class HargaProduct extends AppCompatActivity {

    LinearLayout laySemuaProduk;
    private RecyclerView rvHarga;
    private HargaProdukAdapter hargaProdukAdapter;
    private LinearLayoutManager llm;
    private SwipeRefreshLayout swipRefresh;
    private SearchableSpinner spinProduk,spinSubProduk;
    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;

    ArrayList<modHrgProduk> arrHrgProduk = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.harga_product);

        getSupportActionBar().setTitle("Harga Produk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tampilHrgAll();
        declaration();
        action();
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

    public void declaration() {
        swipRefresh = (SwipeRefreshLayout) findViewById(R.id.swipRefresh);
        rvHarga = (RecyclerView) findViewById(R.id.rv_harga);
        llm = new LinearLayoutManager(this);
        rvHarga.setLayoutManager(llm);
        rvHarga.setHasFixedSize(true);
        laySemuaProduk = (LinearLayout) findViewById(R.id.laySemuaproduk);
        spinProduk = (SearchableSpinner) findViewById(R.id.spinJnsProduk);
        spinProduk.setPositiveButton("TUTUP");
        spinProduk.setTitle("Pilih Jenis Produk");
        spinSubProduk = (SearchableSpinner) findViewById(R.id.spinSubProduk);
        spinSubProduk.setPositiveButton("TUTUP");
        spinSubProduk.setTitle("Pilih Produk");
    }

     public void action() {

     }

    public void tampilList() {
        if (spinProduk.getSelectedItem().equals("Pulsa")) {
//            spinSubProduk.setAdapter();
        }
    }

    public void tampilHrgAll() {
        pLoading = new ProgressDialog(HargaProduct.this);
        pLoading.setTitle("Memuat data ...");
        pLoading.show();

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.daftarHrgAllPulsa(sv.publickey, sv.privatekey,"pulsa");
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                try {
                    if(response.code() == 200){

                        JSONArray responseArray = new JSONArray(response.body().string());
                        Log.d("Object ","");
                        for (int i=0; i<responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);

                            modHrgProduk mr = new modHrgProduk();
                            if (data.getString("op_produk").equals("pulsa")) {
                                mr.setJnsProduk("Pulsa");
                            }
                            mr.setProvider(data.getString("op_nama"));
                            mr.setKode(data.getString("vo_kode"));
                            mr.setNominal(data.getString("vo_nominal"));
                            mr.setHrg(data.getString("vo_harga"));
                            mr.setHrgJual(data.getString("vo_harga"));
                            if (data.getString("vo_status").equals("1")) {
                                mr.setStok("Tersedia");
                            } else {
                                mr.setStok("Tidak Tersedia");
                            }
                            arrHrgProduk.add(mr);
                        }
                        hargaProdukAdapter = new HargaProdukAdapter(HargaProduct.this, arrHrgProduk);
                        rvHarga.setAdapter(hargaProdukAdapter);

                    } else {

                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pLoading.dismiss();
            }
        });
    }

    public void tampilHrgByJns() {

    }

    private void dialogGagalLoad(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HargaProduct.this);
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
