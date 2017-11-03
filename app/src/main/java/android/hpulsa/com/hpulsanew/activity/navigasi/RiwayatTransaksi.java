package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.adapter.listRiwayatAdapter;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RiwayatTransaksi extends AppCompatActivity {

    public static EditText eNoHp;
    private RecyclerView listRiwayat;
    private ImageView imgSearch;
    private FloatingActionButton fabKetSttus;
    private SwipeRefreshLayout swipRefresh;
    private LinearLayout layButtom;

    private listRiwayatAdapter listRiwayatAdapter;
    private LinearLayoutManager llm;

    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;
    double harga;
    DecimalFormat kursIndonesia;
    DecimalFormatSymbols formatRp;
    public static int offset = 0,limit = 10,totalProduk=20;
    static boolean is_first = true;

    ArrayList<modRiwayat> arrayRiwayat = new ArrayList<>();

    public static boolean isLoading = false,isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_transaksi);

        getSupportActionBar().setTitle("Riwayat Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        riwayat();
        setComponent();
    }

    private void setComponent() {
        swipRefresh = (SwipeRefreshLayout) findViewById(R.id.swipRefresh);
        eNoHp = (EditText) findViewById(R.id.editNoHp);
        listRiwayat = (RecyclerView) findViewById(R.id.list_riwayat);
        imgSearch = (ImageView) findViewById(R.id.imgSearh);
        fabKetSttus = (FloatingActionButton) findViewById(R.id.fabKetStatus);

        llm = new LinearLayoutManager(this);
        listRiwayat.setLayoutManager(llm);
        listRiwayat.setHasFixedSize(true);


        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipRefresh.setRefreshing(false);
                arrayRiwayat.clear();
                offset=0;
                is_first=true;
                riwayat();
            }
        });
        listRiwayat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = llm.getChildCount();
                int totalItemCount = llm.getItemCount();
                int firstVisibleItemPosition = llm.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if (dy > 0) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0 && totalItemCount >= 10) {
                            isLastPage = true;
                            if (offset < totalProduk) {
                                riwayat();
                            }
                        }
                    }
                }
            }
        });
    }

    private void riwayat() {
        layButtom = (LinearLayout) findViewById(R.id.lay_progressbar_bottom);
        pLoading = new ProgressDialog(RiwayatTransaksi.this);
        if (offset == 0 && is_first) {
            pLoading.setTitle("Memuat data ...");
            pLoading.show();
            layButtom.setVisibility(View.GONE);
        } else {
            layButtom.setVisibility(View.VISIBLE);
        }

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.riwayat(sv.publickey, sv.privatekey, spLogin.getString(sv.token, ""),limit,offset);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                layButtom.setVisibility(View.GONE);
                try {
                    if(response.code() == 200){
                        JSONArray responseArray = new JSONArray(response.body().string());
                        Log.d("Object ","");
                        for (int i=0; i<responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);

                            modRiwayat mr = new modRiwayat();
                            mr.setTrxID(data.getString("tr_id"));
                            mr.setUsID(data.getString("us_id"));
                            mr.setSvID(data.getString("sv_id"));
                            mr.setOpId(data.getString("op_id"));
                            mr.setOpProduk(data.getString("op_produk"));
                            mr.setOpNama(data.getString("op_nama"));
                            mr.setVoID(data.getString("vo_id"));
                            mr.setVoKode(data.getString("vo_kode"));
                            mr.setVoNominal(data.getString("vo_nominal"));
                            mr.setTrxIDPlnggn(data.getString("tr_id_plgn"));
                            mr.setTrxNoHP(data.getString("tr_no_hp"));
                            harga = Double.parseDouble(data.getString("tr_harga"));
                            kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                            formatRp = new DecimalFormatSymbols();

                            formatRp.setCurrencySymbol("Rp. ");
                            formatRp.setMonetaryDecimalSeparator(',');
                            formatRp.setGroupingSeparator('.');

                            kursIndonesia.setDecimalFormatSymbols(formatRp);
                            mr.setTrxHarga(kursIndonesia.format(harga));
                            mr.setTrxHarga2(data.getString("tr_harga2"));
                            mr.setTrxIncome(data.getString("tr_income"));
                            mr.setTrxRate(data.getString("tr_rate"));
                            if (data.getString("tr_pembayaran").equals("balance")) {
                                mr.setTrxPembayaran("Saldo Akun");
                            } else {
                                mr.setTrxPembayaran("Transfer Bank");
                            }
                            mr.setTrxSttsPembayaran(data.getString("tr_status_pembayaran"));
                            mr.setTrxIDPembayaran(data.getString("tr_id_pembayaran"));
                            mr.setTrxRetry(data.getString("tr_retry"));
                            mr.setTrxCekMutasi(data.getString("tr_cek_mutasi"));
                            if (data.getString("tr_status").equals("pending")||
                                    data.getString("tr_status_pembayaran").equals("pending")) {
                                mr.setTrxStts("IP");
                            } else if (data.getString("tr_status").equals("sukses")) {
                                mr.setTrxStts("OK");
                            } else if (data.getString("tr_status").equals("gagal")) {
                                mr.setTrxStts("CL");
                            } else {
                                mr.setTrxStts("");
                            }
                            mr.setTrxOpsi(data.getString("tr_opsi"));
                            mr.setTrxTgl(data.getString("tr_tanggal"));
                            mr.setTrxTrkrim(data.getString("tr_terkirim"));
                            arrayRiwayat.add(mr);
                        }
                        listRiwayatAdapter = new listRiwayatAdapter(RiwayatTransaksi.this, arrayRiwayat);
                        listRiwayat.setAdapter(listRiwayatAdapter);
                        listRiwayatAdapter.notifyDataSetChanged();
                        offset = offset+10;
                        is_first = false;
                        isLastPage = false;
                    } else {

                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pLoading.dismiss();
                layButtom.setVisibility(View.GONE);
            }
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
        offset=0;
        is_first=true;
        return;
    }

}
