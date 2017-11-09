package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.adapter.TestimoniAdapter;
import android.hpulsa.com.hpulsanew.adapter.listRiwayatAdapter;
import android.hpulsa.com.hpulsanew.captcha.TextCaptcha;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.hpulsa.com.hpulsanew.model.modTestimoniAll;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

public class Testimoni extends AppCompatActivity {

    FloatingActionButton fabBuatTesti;
    private RecyclerView rvTestimoni;

    private TestimoniAdapter testimoniAdapter;
    private LinearLayoutManager llm;
    private RelativeLayout layoutKirimTes;
    private SwipeRefreshLayout swipRefresh;
    private LinearLayout layButtom;

    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    ProgressDialog pLoading;

    ArrayList<modTestimoniAll> arrTest = new ArrayList<>();
    TextCaptcha textCaptcha;
    int numberOfCaptchaFalse = 1;
    public static int offset = 0,limit = 10,totalData=0;
    String pesan;
    private boolean isKirimTest = false;

    static boolean is_first = true;
    public static boolean isLoading = false,isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testimoni);
        getSupportActionBar().setTitle("Testimonial");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        declaration();
        action();
        tampilTest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isKirimTest == true) {
            tampilTest();
        }
    }

    private void declaration() {
        swipRefresh = (SwipeRefreshLayout) findViewById(R.id.swipRefresh);
        fabBuatTesti = (FloatingActionButton) findViewById(R.id.fabAddTes);
        rvTestimoni = (RecyclerView) findViewById(R.id.rv_test);
        llm = new LinearLayoutManager(this);
        rvTestimoni.setLayoutManager(llm);
        rvTestimoni.setHasFixedSize(true);
        layButtom = (LinearLayout) findViewById(R.id.lay_progressbar_bottom);
        testimoniAdapter = new TestimoniAdapter(Testimoni.this, arrTest);
        rvTestimoni.setAdapter(testimoniAdapter);
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Snackbar.make(layoutKirimTes, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
        } else {
            kirimTest();
        }
    }

    private void action() {
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipRefresh.setRefreshing(false);
                isLoading = false;
                is_first=true;
                offset=0;
                arrTest.clear();
                tampilTest();
            }
        });

        fabBuatTesti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(Testimoni.this);
                View dialog_layout = inflater.inflate(R.layout.buat_testimoni, null);

                AlertDialog.Builder dialogBuatTes = new AlertDialog.Builder(Testimoni.this);
                dialogBuatTes.setView(dialog_layout);
                final AlertDialog theDialog = dialogBuatTes.create();

                final EditText eUsername = (EditText) dialog_layout.findViewById(R.id.eUsername);
                final EditText eNoHp = (EditText) dialog_layout.findViewById(R.id.eNoHp);
                final EditText ePesan = (EditText) dialog_layout.findViewById(R.id.ePesan);
                final EditText eCaptcha = (EditText) dialog_layout.findViewById(R.id.teksCaptcha);
                layoutKirimTes = (RelativeLayout) findViewById(R.id.layoutKirimTes);

                eUsername.setText(spLogin.getString(sv.username,""));
                eNoHp.setText(spLogin.getString(sv.phone,""));

                final ImageView captcha = (ImageView) dialog_layout.findViewById(R.id.Captcha);
                final ImageView imgRefCaptcha = (ImageView) dialog_layout.findViewById(R.id.imgRefrCaptcha);

                textCaptcha = new TextCaptcha(600, 150, 5, TextCaptcha.TextOptions.NUMBERS_ONLY);
                captcha.setImageBitmap(textCaptcha.getImage());
                imgRefCaptcha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numberOfCaptchaFalse++;
                        textCaptcha = new TextCaptcha(600, 150, 5, TextCaptcha.TextOptions.NUMBERS_ONLY);
                        captcha.setImageBitmap(textCaptcha.getImage());
                    }
                });

                FancyButton btnKirim = (FancyButton) dialog_layout.findViewById(R.id.btn_kirim);
                btnKirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = eUsername.getText().toString();
                        String noHp = eNoHp.getText().toString();
                        pesan = ePesan.getText().toString();
                        String teksCaptcha = eCaptcha.getText().toString();
                        if (username.equals("") || noHp.equals("") || pesan.equals("") || teksCaptcha.equals("")) {
                            dialogGagalKirim("Isi dengan lengkap", "Inputan tidak boleh kosong");
                        } else if (!textCaptcha.checkAnswer(teksCaptcha.trim())) {
                            dialogGagalKirim("Captcha tidak cocok", "Periksa kembali inputan captcha anda");
                            eCaptcha.setText("");
                        } else {
                            cekKoneksi();
                            theDialog.dismiss();
                        }
                    }
                });

                FancyButton btnBatal = (FancyButton) dialog_layout.findViewById(R.id.btn_batal);
                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theDialog.dismiss();
                    }
                });

                theDialog.show();
            }
        });
        rvTestimoni.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                tampilTest();
                            }
                        }
                    }
                }
            }
        });
    }

    // ========================================= Tampil Testimoni =================================
    private void tampilTest() {
        pLoading = new ProgressDialog(Testimoni.this);
        if (offset == 0 && is_first) {
            pLoading.setTitle("Memuat data ...");
            pLoading.show();
            layButtom.setVisibility(View.GONE);
        } else {
            layButtom.setVisibility(View.VISIBLE);
        }
        arrTest.clear();

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.testimoniAll(sv.publickey, sv.privatekey,limit,offset);
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

                            modTestimoniAll mr = new modTestimoniAll();
                            mr.setNamaTes(data.getString("nama"));
                            mr.setTglTes(data.getString("tanggal"));
                            mr.setNoHpTes(data.getString("no_hp"));
                            mr.setPesanTes(data.getString("pesan"));
                            arrTest.add(mr);
                        }
                        totalData = jsonObject.getInt("total_data");
                        offset = offset+10;
                        testimoniAdapter.notifyDataSetChanged();
                        layButtom.setVisibility(View.GONE);
                        isLastPage = false;
                        is_first=false;

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

// ============================================ KIRIM TESTIMONI ===============================
    private void kirimTest() {
        pLoading = new ProgressDialog(Testimoni.this);
        pLoading.setTitle("Mengirim testimoni ...");
        pLoading.show();

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.kirimTestimoni(sv.publickey, sv.privatekey,spLogin.getString(sv.token,""),pesan);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pLoading.dismiss();
                try {
                    if(response.code() == 200){

                        JSONObject resObj = new JSONObject(response.body().string());
                        if (resObj.getString("success").equals("true") || resObj.getString("success").equals(true)) {
                            Toast.makeText(Testimoni.this,resObj.getString("message"),Toast.LENGTH_LONG).show();
                            isKirimTest = true;
                            tampilTest();
                        } else {
                            Toast.makeText(Testimoni.this,"Gagal kirim testimoni",Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(Testimoni.this,"Gagal kirim testimoni",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException | IOException e) {
                    Toast.makeText(Testimoni.this,"Gagal kirim testimoni",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pLoading.dismiss();
                Toast.makeText(Testimoni.this,"Gagal kirim testimoni",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dialogGagalKirim(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Testimoni.this);
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
