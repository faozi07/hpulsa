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

public class PaketTelpSMS extends AppCompatActivity {

    public static EditText nomor;
    public static TextView provider;
    public static String number = "";
    public static LinearLayout layPilihNom,layPilihKontak;
    private hPulsaAPI api;
    private StaticVars sv = new StaticVars();
    SharedPreferences spLogin;
    gabunganAdapter gabunganAdapter;
    ArrayList<modNomPulsa> arrNominal = new ArrayList<>();
    private LinearLayoutManager llm;
    private RecyclerView rvNominal;

    final int RQS_PICKCONTACT = 1;
    ImageView imgProvider,imgContact;
    private String opSlug = "";
    boolean isLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulsa_hp);
        declaration();
        action();
        getSupportActionBar().setTitle("Paket Telpon dan SMS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declaration() {
        sv.produk="telpsms";
        llm = new LinearLayoutManager(this);
        rvNominal = (RecyclerView) findViewById(R.id.rvNominal);
        rvNominal.setLayoutManager(llm);
        rvNominal.setHasFixedSize(true);
        layPilihKontak = (LinearLayout) findViewById(R.id.layPilihNoHp);
        layPilihNom = (LinearLayout) findViewById(R.id.layNominal);
        nomor = (EditText) findViewById(R.id.nomor);
        provider = (TextView) findViewById(R.id.provider);
        imgProvider = (ImageView) findViewById(R.id.imgProvider);
        imgContact = (ImageView) findViewById(R.id.imgPilihKontak);
    }

    private void action() {
        imgProvider.setVisibility(View.GONE);
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {
                    if (Permissions.checkContactPermission(PaketTelpSMS.this)) {
                        final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                        Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                        startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                    } else {
                        ActivityCompat.requestPermissions(PaketTelpSMS.this, new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS}, RQS_PICKCONTACT);
                    }
                } else {
                    final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                    Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                    startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                }
            }
        });

        nomor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length()<4) {
                    imgProvider.setVisibility(View.GONE);
                    nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,0,0);

                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.GONE);
                    layPilihKontak.setVisibility(View.VISIBLE);
                    opSlug="";
                    isLoad=true;
                } else {
                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.VISIBLE);
                    layPilihKontak.setVisibility(View.GONE);
                    number = nomor.getText().toString().substring(0, 4);
                    if (number.equals("0857") || number.equals("0856") || number.equals("0858") || number.equals("0815")
                            || number.equals("0816")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_indosat);
                        opSlug = "isat-telpon-sms-masa-aktif";
                    } else if (number.equals("0859") || number.equals("0878") || number.equals("0819") || number.equals("0877")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_xl);
                        opSlug = "xl-telpon-sms";
                    } else if (number.equals("0812") || number.equals("0852") || number.equals("0853") || number.equals("0821")
                            || number.equals("0813") || number.equals("0822") || number.equals("0823")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_telkomsel);
                        opSlug = "tsel-telpon-sms";
                    } else if (number.equals("0896") || number.equals("0895") || number.equals("0899") || number.equals("0897")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_three);
                        opSlug = "three-telpon-sms";
                    } else if (number.equals("0838") || number.equals("0831") || number.equals("0832")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_axis);
                        opSlug = "";
                    } else if (number.equals("0888") || number.equals("0889")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_smartfren);
                        opSlug = "";
                    } else if (number.equals("9990") || number.equals("9991") || number.equals("9992") || number.equals("9993")
                            || number.equals("9994") || number.equals("9995") || number.equals("9996") || number.equals("9997")
                            || number.equals("9998") || number.equals("9999")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_bolt);
                        opSlug = "";
                    } else {
                        provider.setVisibility(View.VISIBLE);
                        layPilihNom.setVisibility(View.GONE);
                        layPilihKontak.setVisibility(View.GONE);
                        imgProvider.setVisibility(View.GONE);
                        opSlug = "";
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,0,0);
                    }

                    if (!opSlug.equals("") && isLoad) {
                        getHarga(opSlug);
                        isLoad=false;
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
                                nomor.setText(karDepan+noHp);
                            } else {
                                nomor.setText(stringNumber);
                            }
                        }

                    }else{
                        Toast.makeText(PaketTelpSMS.this, "Nomor handphone tidak ditemukan", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(PaketTelpSMS.this, "Tidak ada data!", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void getHarga(String opSlug) {
        arrNominal.clear();
        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Retrofit retrofit = ClientAPI.getMyRetrofit();
        Call<ResponseBody> api_request;
        api = retrofit.create(hPulsaAPI.class);
        api_request = api.daftarHrgByOp(sv.publickey, sv.privatekey, "paket_telp_sms", opSlug);
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
                        gabunganAdapter = new gabunganAdapter(PaketTelpSMS.this, arrNominal);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaketTelpSMS.this);
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
        opSlug="";
        return;
    }
}
