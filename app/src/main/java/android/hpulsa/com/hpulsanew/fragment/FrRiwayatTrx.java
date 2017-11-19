package android.hpulsa.com.hpulsanew.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.adapter.listRiwayatAdapter;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrRiwayatTrx extends Fragment {

    public static EditText eNoHp;
    private RecyclerView listRiwayat;
    private ImageView imgSearch;
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
    public static int offset = 0,limit = 10,totalRiwayat=0;
    public static boolean is_first = true;

    ArrayList<modRiwayat> arrayRiwayat = new ArrayList<>();

    public static boolean isLoading = false,isLastPage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fr_riwayat_trx, container, false);
        setComponent(v);
        riwayat();
        return v;
    }

    private void setComponent(final View view) {
        swipRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipRefresh);
        eNoHp = (EditText) view.findViewById(R.id.editNoHp);
        listRiwayat = (RecyclerView) view.findViewById(R.id.list_riwayat);
        imgSearch = (ImageView) view.findViewById(R.id.imgSearh);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayRiwayat.clear();
                offset=0;
                is_first=true;
                riwayat();
            }
        });

        llm = new LinearLayoutManager(getActivity());
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
                            if (offset < totalRiwayat) {
                                riwayat();
                            }
                        }
                    }
                }
            }
        });

        layButtom = (LinearLayout) view.findViewById(R.id.lay_progressbar_bottom);
/*        listRiwayatAdapter = new listRiwayatAdapter(RiwayatTransaksi.this, arrayRiwayat);
        listRiwayat.setAdapter(listRiwayatAdapter);*/
    }

    private void riwayat() {
        pLoading = new ProgressDialog(getActivity());
        if (offset == 0 && is_first) {
            pLoading.setTitle("Memuat data ...");
            pLoading.show();
            layButtom.setVisibility(View.GONE);
        } else {
            layButtom.setVisibility(View.VISIBLE);
        }

        spLogin = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        if (eNoHp.getText().toString().equals("")) {
            api_request = api.riwayat(sv.publickey, sv.privatekey, spLogin.getString(sv.token, ""), limit, offset);
        } else {
            api_request = api.riwayatByPhone(sv.publickey, sv.privatekey, spLogin.getString(sv.token, ""), limit, offset,eNoHp.getText().toString());
        }
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                layButtom.setVisibility(View.GONE);
                try {
                    if(response.code() == 200){
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray responseArray = jsonObject.getJSONArray("data");
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
                        totalRiwayat = jsonObject.getInt("total_data");
                        if(offset == 0) {
                            listRiwayatAdapter = new listRiwayatAdapter(getActivity(), arrayRiwayat);
                            listRiwayat.setAdapter(listRiwayatAdapter);
                        }
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
}
