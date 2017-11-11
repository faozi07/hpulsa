package android.hpulsa.com.hpulsanew.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.adapter.depositAdapter;
import android.hpulsa.com.hpulsanew.adapter.listRiwayatAdapter;
import android.hpulsa.com.hpulsanew.model.modDeposit;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.hpulsa.com.hpulsanew.util.StaticVars;
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
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrRiwayatDeposit extends Fragment {

    private RecyclerView listDeposit;
    private SwipeRefreshLayout swipRefresh;
    private LinearLayout layButtom;

    private depositAdapter depositAdapter;
    private LinearLayoutManager llm;

    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;
    double harga;
    DecimalFormat kursIndonesia;
    DecimalFormatSymbols formatRp;
    public static int offset = 0, limit = 10, totalDeposit = 0;
    public static boolean is_first = true;

    ArrayList<modDeposit> arrayDeposit = new ArrayList<>();

    public static boolean isLoading = false, isLastPage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fr_riwayat_deposit, container, false);
        setComponent(view);
        tampilDeposit();
        return view;
    }

    private void setComponent(View v) {
        swipRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipRefresh);
        listDeposit = (RecyclerView) v.findViewById(R.id.list_deposit);
        layButtom = (LinearLayout) v.findViewById(R.id.lay_progressbar_bottom);

        llm = new LinearLayoutManager(getActivity());
        listDeposit.setLayoutManager(llm);
        listDeposit.setHasFixedSize(true);
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipRefresh.setRefreshing(false);
                arrayDeposit.clear();
                offset = 0;
                is_first = true;
                tampilDeposit();
            }
        });
        listDeposit.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            if (offset < totalDeposit) {
                                tampilDeposit();
                            }
                        }
                    }
                }
            }
        });
    }

    private void tampilDeposit() {
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
        api_request = api.riwayatDeposit(sv.publickey, sv.privatekey, spLogin.getString(sv.token, ""), offset, limit);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                layButtom.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray responseArray = jsonObject.getJSONArray("data");
                        Log.d("Object ", "");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);

                            modDeposit mr = new modDeposit();
                            mr.setId(data.getString("de_id"));
                            mr.setBank(data.getString("de_payment"));
                            mr.setTglDeposit(data.getString("de_tanggal"));
                            mr.setSttsDeposit(data.getString("de_status"));
                            harga = Double.parseDouble(data.getString("de_amount"));
                            kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                            formatRp = new DecimalFormatSymbols();

                            formatRp.setCurrencySymbol("Rp. ");
                            formatRp.setMonetaryDecimalSeparator(',');
                            formatRp.setGroupingSeparator('.');

                            kursIndonesia.setDecimalFormatSymbols(formatRp);
                            mr.setJmlDeposit(kursIndonesia.format(harga));
                            arrayDeposit.add(mr);
                        }
                        totalDeposit = jsonObject.getInt("total_data");
                        if (offset == 0) {
                            depositAdapter = new depositAdapter(getActivity(), arrayDeposit);
                            listDeposit.setAdapter(depositAdapter);
                        }
                        depositAdapter.notifyDataSetChanged();
                        offset = offset + 10;
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
