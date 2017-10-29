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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private LinearLayoutManager llm;
    private SwipeRefreshLayout swipRefresh;
    private SearchableSpinner spinProduk, spinSubProduk;
    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;
    HargaProdukAdapter hargaProdukAdapter;
    private String opProduk="",opSlug="";
    private boolean isFirst = true;

    ArrayList<modHrgProduk> arrHrgProduk = new ArrayList<>();
    ArrayAdapter<String> subAllAdapt, subPulsaAdapt, subInterAdapt, subTokenAdapt, subGameAdapt, subTelpAdapt, subBBAdapt, subGojekAdapt,
            subTagihanAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.harga_product);

        getSupportActionBar().setTitle("Harga Produk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tampilHrgAll();
        declaration();
        setAdapter();
        tampilList();
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
        isFirst = true;
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

    private void setAdapter() {
        subAllAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_all));
        subPulsaAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_pulsa));
        subInterAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_internet));
        subTokenAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_token_pln));
        subGameAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_voucher_game));
        subTelpAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_paket_telp_sms));
        subBBAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_paket_bb));
        subGojekAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_gojek));
        subTagihanAdapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sub_tagihan));
        spinSubProduk.setAdapter(subAllAdapt);
    }

    public void action() {
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipRefresh.setRefreshing(false);
                arrHrgProduk.clear();
                if(isFirst) {
                    tampilHrgAll();
                } else {
                    tampilHrgByJns(opProduk,opSlug);
                }
            }
        });
    }

    public void tampilList() {

        spinProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        spinSubProduk.setAdapter(subAllAdapt);
                        opProduk = "";
                        break;
                    case 1:
                        spinSubProduk.setAdapter(subPulsaAdapt);
                        opProduk = "pulsa";
                        break;
                    case 2:
                        spinSubProduk.setAdapter(subInterAdapt);
                        opProduk = "paket_internet";
                        break;
                    case 3:
                        spinSubProduk.setAdapter(subTokenAdapt);
                        opProduk = "token_pln";
                        break;
                    case 4:
                        spinSubProduk.setAdapter(subGameAdapt);
                        opProduk = "voucher_game";
                        break;
                    case 5:
                        spinSubProduk.setAdapter(subTelpAdapt);
                        opProduk = "paket_telp_sms";
                        break;
                    case 6:
                        spinSubProduk.setAdapter(subBBAdapt);
                        opProduk = "paket_blackberry";
                        break;
                    case 7:
                        spinSubProduk.setAdapter(subGojekAdapt);
                        opProduk = "saldo_gojek";
                        break;
                    case 8:
                        spinSubProduk.setAdapter(subTagihanAdapt);
                        opProduk = "tagihan";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinSubProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                opSlug = "";
                actionListPulsa();
                actionListInternet();
                actionListPlnGame();
                actionListTelpSms();
                actionListLainnya();
                if (!opProduk.equals("") && !opSlug.equals("")) {
                    tampilHrgByJns(opProduk, opSlug);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }


    private void actionListPulsa() {
        switch (spinSubProduk.getSelectedItem().toString()) {
            case "Axis":
                opSlug = "axis";
                break;
            case "Telkomsel":
                opSlug = "telkomsel";
                break;
            case "XL":
                opSlug = "xl";
                break;
            case "Indosat":
                opSlug = "indosat";
                break;
            case "Smartfren":
                opSlug = "smartfren";
                break;
            case "Three":
                opSlug = "three";
                break;
            case "Bolt":
                opSlug = "bolt";
                break;
        }
    }

    private void actionListInternet() {
        switch (spinSubProduk.getSelectedItem().toString()) {
            case "Axis Data":
                opSlug = "axis-data";
                break;
            case "Telkomsel Data":
                opSlug = "telkomsel-data";
                break;
            case "XL Data":
                opSlug = "xl-data";
                break;
            case "Indosat Data":
                opSlug = "indosat-data";
                break;
            case "Smartfren Data":
                opSlug = "smartfren-data";
                break;
            case "Three Data":
                opSlug = "three-data";
                break;
            case "Bolt Data":
                opSlug = "bolt-data";
                break;
        }
    }

    private void actionListPlnGame() {
        switch (spinSubProduk.getSelectedItem().toString()) {
            case "PLN Special":
                opSlug = "pln-special";
                break;
            case "PLN":
                opSlug = "pln";
                break;
            case "Garena Shells":
                opSlug = "garena-shells";
                break;
            case "Voucher Game Online":
                opSlug = "voucher-game-online";
                break;
        }
    }

    private void actionListTelpSms() {
        switch (spinSubProduk.getSelectedItem().toString()) {
            case "Three Telpon SMS":
                opSlug = "three-telpon-sms";
                break;
            case "XL Telpon SMS":
                opSlug = "xl-telpon-sms";
                break;
            case "Tsel Telpon SMS":
                opSlug = "tsel-telpon-sms";
                break;
            case "Isat Telpon SMS Masa Aktif":
                opSlug = "isat-telpon-sms-masa-aktif";
                break;
        }
    }

    private void actionListLainnya() {
        switch (spinSubProduk.getSelectedItem().toString()) {
            case "Paket Blackberry":
                opSlug = "paket-blackberry";
                break;
            case "Saldo Gojek":
                opSlug = "saldo-gojek";
                break;
            case "PLN Pascabayar":
                opSlug = "pln-pascabayar";
                break;
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
        api_request = api.daftarHrgAllPulsa(sv.publickey, sv.privatekey, "pulsa");
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

    public void tampilHrgByJns(String opProduk,String opSlug) {
        pLoading = new ProgressDialog(HargaProduct.this);
        pLoading.setTitle("Memuat data ...");
        pLoading.show();
        arrHrgProduk.clear();
        isFirst = false;
        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.daftarHrgByOp(sv.publickey, sv.privatekey, opProduk, opSlug);
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

                            modHrgProduk mr = new modHrgProduk();
                            if (data.getString("op_produk").equals("pulsa")) {
                                mr.setJnsProduk("Pulsa");
                            } else if (data.getString("op_produk").equals("token_pln")) {
                                mr.setJnsProduk("Token PLN");
                            } else if (data.getString("op_produk").equals("paket_internet")) {
                                mr.setJnsProduk("Paket Data");
                            } else if (data.getString("op_produk").equals("voucher_game")) {
                                mr.setJnsProduk("Voucher Game");
                            } else if (data.getString("op_produk").equals("paket_telp_sms")) {
                                mr.setJnsProduk("Paket Telp & SMS");
                            } else if (data.getString("op_produk").equals("tagihan")) {
                                mr.setJnsProduk("Tagihan");
                            } else if (data.getString("op_produk").equals("saldo_gojek")) {
                                mr.setJnsProduk("Saldo Gojek");
                            } else if (data.getString("op_produk").equals("paket_blackberry")) {
                                mr.setJnsProduk("Paket Blackberry");
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
                        dialogGagalLoad("Gagal memuat data","Terjadi kesalahan");
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    dialogGagalLoad("Gagal memuat data","Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pLoading.dismiss();
                dialogGagalLoad("Gagal memuat data","Terjadi kesalahan");
            }
        });
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
