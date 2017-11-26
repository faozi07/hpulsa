package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.adapter.NotifAdapter;
import android.hpulsa.com.hpulsanew.model.modNotif;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Pemberitahuan extends AppCompatActivity {

    SwipeRefreshLayout swipeRefresh;
    RecyclerView rvNotif;
    NotifAdapter notifAdapter;
    private LinearLayoutManager llm;
    private LinearLayout layButtom;

    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;

    ArrayList<modNotif> arrNotif = new ArrayList<>();
    public static int offset = 0,limit = 10,totalData=0;
    static boolean is_first = true;
    public static boolean isLoading = false,isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pemberitahuan);

        declaration();
        action();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pemberitahuan");
        }
        cekKoneksi();
    }

    private void declaration() {
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipRefresh);
        rvNotif = (RecyclerView) findViewById(R.id.rvPemberitahuan);
        llm = new LinearLayoutManager(this);
        rvNotif.setLayoutManager(llm);
        rvNotif.setHasFixedSize(true);
        layButtom = (LinearLayout) findViewById(R.id.lay_progressbar_bottom);
        notifAdapter = new NotifAdapter(Pemberitahuan.this, arrNotif);
        rvNotif.setAdapter(notifAdapter);
    }

    private void action() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                offset=0;
                is_first=true;
                cekKoneksi();
            }
        });

        rvNotif.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            if (offset < totalData) {
                                cekKoneksi();
                            }
                        }
                    }
                }
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
        return;
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Snackbar.make(swipeRefresh, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
        } else {
            tampilPemberitahuan();
        }
    }

    private void tampilPemberitahuan() {
        pLoading = new ProgressDialog(Pemberitahuan.this);
        if (offset == 0 && is_first) {
            pLoading.setTitle("Memuat data ...");
            pLoading.show();
            layButtom.setVisibility(View.GONE);
            if (arrNotif != null) {
                arrNotif.clear();
            }
        } else {
            layButtom.setVisibility(View.VISIBLE);
        }

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.pemberitahuan(sv.publickey, sv.privatekey,spLogin.getString(sv.token,""),offset,limit);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                try {
                    if(response.code() == 200){
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray responseArray = jsonObject.getJSONArray("data");
                        Log.d("Object ","");
                        for (int i=0; i<responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);

                            modNotif mn = new modNotif();
                            mn.setId(data.getString("nf_id"));
                            mn.setJudul(data.getString("nf_judul"));
                            mn.setKonten(data.getString("nf_teks"));
                            mn.setTanggal(data.getString("nf_tanggal"));
                            arrNotif.add(mn);
                        }
                        totalData = jsonObject.getInt("total_data");
                        offset = offset+10;
                        notifAdapter.notifyDataSetChanged();
                        layButtom.setVisibility(View.GONE);
                        isLastPage = false;
                        is_first=false;
                        if (offset == 0) {
                            notifAdapter = new NotifAdapter(Pemberitahuan.this, arrNotif);
                            rvNotif.setAdapter(notifAdapter);
                        }

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
}
