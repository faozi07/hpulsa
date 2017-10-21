package android.hpulsa.com.hpulsanew.activity.pilihanmenu;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.util.Permissions;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.hpulsa.com.hpulsanew.adapter.listViewAdapter;
import android.hpulsa.com.hpulsanew.model.modNomPulsa;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PulsaHP extends AppCompatActivity {

    public static EditText nomor;
    public static TextView provider;
    public static String number = "";
    ArrayAdapter adapter;
    ListView listview;
    public static ViewStub stubList;
    private listViewAdapter listViewAdapter;
    private List<modNomPulsa> modNomPulsas;
    public static LinearLayout layPilihNom,layPilihKontak;

    boolean isPilihNominal = false;
    int animAktif=0;
    final int RQS_PICKCONTACT = 1;
    ImageView imgProvider,imgContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulsa_hp);

        declaration();
        action();

        getModNomPulsa();

        getSupportActionBar().setTitle("Pulsa Reguler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declaration() {
        layPilihKontak = (LinearLayout) findViewById(R.id.layPilihNoHp);
        layPilihNom = (LinearLayout) findViewById(R.id.layNominal);
        nomor = (EditText) findViewById(R.id.nomor);
        provider = (TextView) findViewById(R.id.provider);
        imgProvider = (ImageView) findViewById(R.id.imgProvider);
        imgContact = (ImageView) findViewById(R.id.imgPilihKontak);
        stubList = (ViewStub) findViewById(R.id.stub_list);
        listview = (ListView) findViewById(R.id.listNominal);
        listViewAdapter = new listViewAdapter(PulsaHP.this, R.layout.list_nominal,modNomPulsas);
    }

    private void action() {
        imgProvider.setVisibility(View.GONE);
        stubList.setVisibility(View.GONE);
        stubList.setEnabled(false);
        stubList.inflate();
//        listview.setAdapter(listViewAdapter);
        imgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {
                    if (Permissions.checkContactPermission(PulsaHP.this)) {
                        final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                        Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                        startActivityForResult(intentPickContact, RQS_PICKCONTACT);
                    } else {
                        ActivityCompat.requestPermissions(PulsaHP.this, new String[]{Manifest.permission.READ_CONTACTS,
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
                    if (animAktif==0) {

                        Animation anim = AnimationUtils.loadAnimation(PulsaHP.this, R.anim.zoomout);
                        layPilihNom.setAnimation(anim);
                        layPilihNom.setVisibility(View.GONE);

                        Animation animation = AnimationUtils.loadAnimation(PulsaHP.this, R.anim.zoomin);
                        layPilihKontak.setAnimation(animation);
                        layPilihKontak.setVisibility(View.VISIBLE);
                        animAktif=1;

                    } else {
                        layPilihNom.setVisibility(View.GONE);
                        layPilihKontak.setVisibility(View.VISIBLE);
                        animAktif=2;
                    }
                } else {
                    if (animAktif==2) {
                        Animation anim = AnimationUtils.loadAnimation(PulsaHP.this, R.anim.zoomin);
                        layPilihNom.setAnimation(anim);
                        layPilihNom.setVisibility(View.VISIBLE);

                        Animation animation = AnimationUtils.loadAnimation(PulsaHP.this, R.anim.zoomout);
                        layPilihKontak.setAnimation(animation);
                        layPilihKontak.setVisibility(View.GONE);
                        animAktif = 0;
                    } else {
                        layPilihNom.setVisibility(View.VISIBLE);
                        layPilihKontak.setVisibility(View.GONE);
                        animAktif=0;
                    }
                    number = nomor.getText().toString().substring(0, 4);
                    if (number.equals("0857") || number.equals("0856") || number.equals("0858") || number.equals("0815")
                            || number.equals("0816")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_indosat);
                    } else if (number.equals("0859") || number.equals("0878") || number.equals("0819") || number.equals("0877")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_xl);
                    } else if (number.equals("0812") || number.equals("0852") || number.equals("0853") || number.equals("0821")
                            || number.equals("0813") || number.equals("0822") || number.equals("0823")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_telkomsel);
                    } else if (number.equals("0896") || number.equals("0895") || number.equals("0899") || number.equals("0897")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_three);
                    } else if (number.equals("0838") || number.equals("0831") || number.equals("0832")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_axis);
                    } else if (number.equals("0888") || number.equals("0889")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_smartfren);
                    } else if (number.equals("9990") || number.equals("9991") || number.equals("9992") || number.equals("9993")
                            || number.equals("9994") || number.equals("9995") || number.equals("9996") || number.equals("9997")
                            || number.equals("9998") || number.equals("9999")) {
                        imgProvider.setVisibility(View.VISIBLE);
                        imgProvider.setImageResource(R.drawable.logo_bolt);
                    } else {
                        provider.setVisibility(View.VISIBLE);
                        layPilihNom.setVisibility(View.GONE);
                        layPilihKontak.setVisibility(View.GONE);
                        provider.setText("Data Tidak Ditemukan");
                        imgProvider.setVisibility(View.GONE);
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,0,0);
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
                        Toast.makeText(PulsaHP.this, "Nomor handphone tidak ditemukan", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(PulsaHP.this, "Tidak ada data!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public List<modNomPulsa> getModNomPulsa() {
        modNomPulsas = new ArrayList<>();
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        modNomPulsas.add(new modNomPulsa(""));
        return modNomPulsas;
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            /*if (position==0) {
                startActivity(new Intent(PulsaActivity.this,PulsaActivity.class));
            }*/
//            Toast.makeText(HomeActivity.this, modNomPulsa.get(position).getKetMenu(),Toast.LENGTH_SHORT).show();
        }
    };

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
}
