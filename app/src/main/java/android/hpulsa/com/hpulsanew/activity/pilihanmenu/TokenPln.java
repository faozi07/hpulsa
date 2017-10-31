package android.hpulsa.com.hpulsanew.activity.pilihanmenu;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.adapter.gabunganAdapter;
import android.hpulsa.com.hpulsanew.model.modNomPulsa;
import android.hpulsa.com.hpulsanew.util.Permissions;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class TokenPln extends AppCompatActivity {

    public static EditText nomorHp,noPln;
    public static TextView provider;
    public static String number = "";
    public static LinearLayout layPilihNom,layPilihKontak;
    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    private gabunganAdapter gabunganAdapter;
    ArrayList<modNomPulsa> arrNominal = new ArrayList<>();
    private LinearLayoutManager llm;
    private RecyclerView rvNominal;

    final int RQS_PICKCONTACT = 1;
    ImageView imgContact;
    private String opProduk = "";
    boolean isload = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.token_pln);
        declaration();
        action();
        getSupportActionBar().setTitle("Token Pulsa PLN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        opProduk="";
        sv.nomorHP="";
        sv.nomorPLN="";
        return;
    }

    private void declaration() {
        sv.produk="token";
        llm = new LinearLayoutManager(this);
        rvNominal = (RecyclerView) findViewById(R.id.rvNominal);
        rvNominal.setLayoutManager(llm);
        rvNominal.setHasFixedSize(true);
        layPilihKontak = (LinearLayout) findViewById(R.id.layPilihNoHp);
        layPilihNom = (LinearLayout) findViewById(R.id.layNominal);
        nomorHp = (EditText) findViewById(R.id.tNoHp);
        noPln = (EditText) findViewById(R.id.tNoMeterPln);
        provider = (TextView) findViewById(R.id.provider);
        imgContact = (ImageView) findViewById(R.id.imgPilihKontak);
    }

    private void action() {
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {
                    if (Permissions.checkContactPermission(TokenPln.this)) {
                        final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                        Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                        startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                    } else {
                        ActivityCompat.requestPermissions(TokenPln.this, new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS}, RQS_PICKCONTACT);
                    }
                } else {
                    final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                    Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                    startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                }
            }
        });

        noPln.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length()<4 || nomorHp.getText().length()<4) {
                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.GONE);
                    layPilihKontak.setVisibility(View.VISIBLE);
                    opProduk = "";
                    isload = true;
                    sv.nomorPLN="";
                } else {
                    sv.nomorPLN = edit.toString();
                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.VISIBLE);
                    layPilihKontak.setVisibility(View.GONE);
                    opProduk = "token_pln";
                    if (!opProduk.equals("") && isload) {
                        getHarga(opProduk);
                        isload = false;
                    }

                }
            }
        });

        nomorHp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length()<4 || noPln.getText().length()<4) {
                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.GONE);
                    layPilihKontak.setVisibility(View.VISIBLE);
                    opProduk = "";
                    isload = true;
                    sv.nomorHP="";
                } else {
                    sv.nomorHP = edit.toString();
                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.VISIBLE);
                    layPilihKontak.setVisibility(View.GONE);
                    opProduk = "token_pln";
                    if (!opProduk.equals("") && isload) {
                        getHarga(opProduk);
                        isload = false;
                    }

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(resultCode == RESULT_OK){
            if(requestCode == RQS_PICKCONTACT){
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);

                if(cursor.moveToNext()){
                    int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactID = cursor.getString(columnIndex_ID);

                    int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);

                    if(stringHasPhoneNumber.equalsIgnoreCase("1")){
                        Cursor cursorNum = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID,
                                null,
                                null);

                        //Get the first phone number
                        if(cursorNum.moveToNext()){
                            int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            String stringNumber = cursorNum.getString(columnIndex_number).replaceAll("[^0-9.]", "");
                            int pjgNumber = stringNumber.length();
                            String karDepan = stringNumber.substring(0,2);
                            String noHp = stringNumber.substring(2,pjgNumber);
                            if (karDepan.equals("62")) {
                                karDepan = "0";
                                nomorHp.setText(karDepan+noHp);
                            } else {
                                nomorHp.setText(stringNumber);
                            }
                        }

                    }else{
                        Toast.makeText(TokenPln.this, "Nomor handphone tidak ditemukan", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(TokenPln.this, "Tidak ada data!", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void getHarga(String opProduk) {
        arrNominal.clear();
        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.daftarHrgAllPulsa(sv.publickey, sv.privatekey, opProduk);
        api_request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {

                        JSONArray responseArray = new JSONArray(response.body().string());
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject data = responseArray.getJSONObject(i);

                            modNomPulsa mr = new modNomPulsa();

                            mr.setJnsProduk("Pulsa");
                            mr.setId(data.getInt("vo_id"));
                            mr.setProvider(data.getString("op_nama"));
                            mr.setKode(data.getString("vo_kode"));
                            mr.setNominal(data.getString("vo_nominal"));
                            mr.setHrg(data.getString("vo_harga"));
                            mr.setHrgJual(data.getString("vo_harga"));
                            mr.setStok(data.getString("vo_status"));

                            arrNominal.add(mr);
                        }
                        gabunganAdapter = new gabunganAdapter(TokenPln.this, arrNominal);
                        rvNominal.setAdapter(gabunganAdapter);

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
                dialogGagalLoad("Gagal memuat data","Terjadi kesalahan");
            }
        });
    }

    private void dialogGagalLoad(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TokenPln.this);
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
