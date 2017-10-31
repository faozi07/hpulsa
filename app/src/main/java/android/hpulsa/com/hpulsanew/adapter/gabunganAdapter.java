package android.hpulsa.com.hpulsanew.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PulsaHP;
import android.hpulsa.com.hpulsanew.model.modListBank;
import android.hpulsa.com.hpulsanew.model.modNomPulsa;
import android.hpulsa.com.hpulsanew.model.modTransaksi;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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

/**
 * Created by ozi on 29/10/2017.
 */

public class gabunganAdapter extends RecyclerView.Adapter {

    public static Activity activity;
    public static ArrayList<modNomPulsa> items;
    modNomPulsa mrt;

    private final int VIEW_ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading,isTersedia = true;;
    StaticVars sv = new StaticVars();

    private int lastPosition = -1,posisiKlik;
    double nominal, harga;
    DecimalFormat kursInd,nonKurs;
    DecimalFormatSymbols formatRp,formatNonRp;

    private ArrayAdapter<String> arrListBankAdapter;
    private ArrayList<modListBank> arrayListBank = new ArrayList<modListBank>();
    private ArrayList<String> arrayListNamaBank = new ArrayList<String>();
    ProgressDialog pLoading;
    SharedPreferences spLogin;
    private hPulsaAPI api;
    Spinner spinBank;
    String arrBank,trPembayaran,voId;
    private modListBank mr;
    private modNomPulsa mnp = new modNomPulsa();
    AlertDialog theDialog;

    public gabunganAdapter(Activity act, ArrayList<modNomPulsa> data){
        activity = act;
        this.items = data;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView tNominal,tHarga,tKet;
        CardView cardView;
        ImageView imgTersedia;

        public BrandViewHolder(View v) {
            super(v);

            tNominal = (TextView) v.findViewById(R.id.tKet);
            tHarga = (TextView) v.findViewById(R.id.tHarga);
            tKet = (TextView) v.findViewById(R.id.tKet);
            imgTersedia = (ImageView) v.findViewById(R.id.imgTersedia);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_nominal, parent, false);

            vh = new gabunganAdapter.BrandViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof gabunganAdapter.BrandViewHolder) {

            spLogin = activity.getSharedPreferences(sv.token, Context.MODE_PRIVATE);

            mrt = items.get(position);

            harga = Double.parseDouble(mrt.getHrgJual());
            kursInd = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursInd.setDecimalFormatSymbols(formatRp);

            ((gabunganAdapter.BrandViewHolder) holder).tNominal.setText(mrt.getNominal());
            ((gabunganAdapter.BrandViewHolder) holder).tHarga.setText(kursInd.format(harga));

            if (mrt.getStok().equals("0")) {
                ((gabunganAdapter.BrandViewHolder) holder).imgTersedia.setImageResource(R.drawable.ic_stop);
                ((gabunganAdapter.BrandViewHolder) holder).cardView.setBackgroundResource(R.color.abu);
                isTersedia = false;
            } else {
                ((gabunganAdapter.BrandViewHolder) holder).imgTersedia.setImageResource(R.drawable.ic_plus);
                ((gabunganAdapter.BrandViewHolder) holder).cardView.setBackgroundResource(R.color.putih);
                isTersedia = true;
            }

            ((gabunganAdapter.BrandViewHolder) holder).tKet.setText(mrt.getNominal());

            setAnimation(((gabunganAdapter.BrandViewHolder) holder).cardView, position);

            ((gabunganAdapter.BrandViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isTersedia) {
                        posisiKlik = holder.getPosition();
                        getListBank();
                    }
                }
            });

        } else {
            ((gabunganAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);

        }
    }

    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.zoomin);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void getListBank() {
        pLoading = new ProgressDialog(activity);
        pLoading.setTitle("Memuat data Bank...");
        pLoading.show();

        spLogin = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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

                            mr = new modListBank();
                            mr.setId(data.getInt("id"));
                            mr.setNamaBank(data.getString("nama"));
                            mr.setKeyBank(data.getString("key"));
                            mr.setNamaRek(data.getString("acc_name"));
                            mr.setNoRek(data.getString("acc_num"));

                            arrayListBank.add(mr);
                        }
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);
                            arrBank = data.getString("nama");

                            arrayListNamaBank.add(arrBank);
                        }

                        popupBayar();
                        arrListBankAdapter = new ArrayAdapter<String>(activity,
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

    private void popupBayar() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.popup_bayar, null);

        AlertDialog.Builder dialogBayar = new AlertDialog.Builder(activity);
        dialogBayar.setView(v);
        theDialog = dialogBayar.create();

        final TextView tJmlBayar = (TextView) v.findViewById(R.id.tJmlBayar);
        spinBank = (Spinner) v.findViewById(R.id.spinBank);
        for (int i=0;i<=items.size();i++) {
            if (i==posisiKlik) {
                mrt = items.get(i);
                harga = Double.parseDouble(mrt.getHrgJual());
                voId = String.valueOf(mrt.getId());
                tJmlBayar.setText(kursInd.format(harga));
            }
        }

        spinBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mr = arrayListBank.get(position);
                trPembayaran = String.valueOf(mr.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        FancyButton btnBayar = (FancyButton) v.findViewById(R.id.btnBayar);
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesTransaksi();
            }
        });

        theDialog.show();
    }

    private void prosesTransaksi() {
        pLoading = new ProgressDialog(activity);
        pLoading.setTitle("Memuat transaksi ...");
        pLoading.show();

        spLogin = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request = null;
        api = retrofit.create(hPulsaAPI.class);
        if (sv.produk.equals("pulsa")) {
            api_request = api.trxPulsa(sv.publickey, sv.privatekey,spLogin.getString(sv.token,""),PulsaHP.nomorHp,trPembayaran,voId);
        } else if (sv.produk.equals("internet")) {
            api_request = api.trxInternet(sv.publickey, sv.privatekey,spLogin.getString(sv.token,""),PulsaHP.nomorHp,trPembayaran,voId);
        } /*else if (sv.produk.equals("telpsms")) {
            api_request = api.listBank(sv.publickey, sv.privatekey);
        } else if (sv.produk.equals("token")) {
            api_request = api.listBank(sv.publickey, sv.privatekey);
        } else if (sv.produk.equals("game")) {
            api_request = api.listBank(sv.publickey, sv.privatekey);
        } else if (sv.produk.equals("bbm")) {
            api_request = api.listBank(sv.publickey, sv.privatekey);
        } else if (sv.produk.equals("gojek")) {
            api_request = api.listBank(sv.publickey, sv.privatekey);
        } else if (sv.produk.equals("tagihan")) {
            api_request = api.listBank(sv.publickey, sv.privatekey);
        }*/
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                try {
                    if (response.code() == 200) {

                        JSONObject data = new JSONObject(response.body().string());

                            modTransaksi mt = new modTransaksi();
                            mt.setTransaksi(data.getBoolean("transaksi"));
                            mt.setJenisProduk(data.getString("jenis_produk"));
                            mt.setNominal(data.getString("nominal"));
                            mt.setNomorHp(data.getString("nomor_hape"));
                            mt.setHarga(data.getString("harga"));
                            mt.setPembayaran(data.getString("pembayaran"));
                            mt.setTglPembelian(data.getString("tanggal_pembelian"));
                            mt.setSttsPembayaran(data.getString("status_pembayaran"));
                            mt.setSttsPengisian(data.getString("status_pengisian"));
                            if (!data.isNull("bank_detail")) {
                                JSONArray bankArray = data.getJSONArray("bank_detail");
                                Log.d("Object ", "");
                                for (int i = 0; i < bankArray.length(); i++) {
                                    JSONObject bankDetail = bankArray.getJSONObject(i);

                                    mt.setAccName(bankDetail.getString("acc_name"));
                                    mt.setAccNumber(bankDetail.getString("acc_number"));
                                    mt.setBank(bankDetail.getString("bank"));
                                }
                            }
                            mt.setMessage(data.getString("message"));
                            mt.setBalance(data.getString("balance"));
                            theDialog.dismiss();
                        dialogTransaksi("",mt.getMessage());
                    } else {
                        dialogTransaksi("Gagal membuat transaksi","Silahkan coba lagi");
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    dialogTransaksi("Gagal","Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {pLoading.dismiss();
                dialogTransaksi("Gagal","Terjadi kesalahan");}
        });
    }

    private void dialogTransaksi(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
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
