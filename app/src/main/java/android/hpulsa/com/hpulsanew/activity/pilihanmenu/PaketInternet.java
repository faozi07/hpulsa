package android.hpulsa.com.hpulsanew.activity.pilihanmenu;

import android.hpulsa.com.hpulsanew.R;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import android.hpulsa.com.hpulsanew.adapter.listViewAdapter;
import android.hpulsa.com.hpulsanew.model.modNomPulsa;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PaketInternet extends AppCompatActivity {

    public static EditText nomor;
    public static TextView provider;
    public static String number = "";
    ArrayAdapter adapter;
    ListView listview;
    String[] listArray = {"5.000", "10.000", "20.000", "25.000", "50.000", "100.000", "500.000", "1.000.000"};
    public static ViewStub stubList;
    private listViewAdapter listViewAdapter;
    private List<modNomPulsa> modNomPulsas;
    public static LinearLayout layPilihNom,layPilihKontak;

    boolean isPilihNominal = false;
    int animAktif=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulsa_hp);

        layPilihKontak = (LinearLayout) findViewById(R.id.layPilihNoHp);
        layPilihNom = (LinearLayout) findViewById(R.id.layNominal);
        nomor = (EditText) findViewById(R.id.nomor);
        provider = (TextView) findViewById(R.id.provider);
//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listArray);
//        listview.setAdapter(adapter);
        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubList.setVisibility(View.GONE);
        stubList.setEnabled(false);
        stubList.inflate();
        getModNomPulsa();
//        listview.setOnItemClickListener(onItemClick);
        listview = (ListView) findViewById(R.id.listNominal);
        listViewAdapter = new listViewAdapter(PaketInternet.this, R.layout.list_nominal,modNomPulsas);
        listview.setAdapter(listViewAdapter);
        getSupportActionBar().setTitle("Paket Internet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nomor.length()<4) {
                    nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,0,0);

                    provider.setVisibility(View.GONE);
                    layPilihNom.setVisibility(View.GONE);
                    layPilihKontak.setVisibility(View.VISIBLE);
                    if (animAktif==0) {

                        Animation anim = AnimationUtils.loadAnimation(PaketInternet.this, R.anim.zoomout);
                        layPilihNom.setAnimation(anim);
                        layPilihNom.setVisibility(View.GONE);

                        Animation animation = AnimationUtils.loadAnimation(PaketInternet.this, R.anim.zoomin);
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
                        Animation anim = AnimationUtils.loadAnimation(PaketInternet.this, R.anim.zoomin);
                        layPilihNom.setAnimation(anim);
                        layPilihNom.setVisibility(View.VISIBLE);

                        Animation animation = AnimationUtils.loadAnimation(PaketInternet.this, R.anim.zoomout);
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
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_indosat,0);
                    } else if (number.equals("0859") || number.equals("0878") || number.equals("0819") || number.equals("0877")) {
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_xl,0);
                    } else if (number.equals("0812") || number.equals("0852") || number.equals("0853") || number.equals("0821")
                            || number.equals("0813") || number.equals("0822")) {
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_telkomsel,0);
                    } else if (number.equals("0896") || number.equals("0895") || number.equals("0899") || number.equals("0897")) {
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_three,0);
                    } else if (number.equals("0838") || number.equals("0831") || number.equals("0832")) {
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_axis,0);
                    } else if (number.equals("0888")) {
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_smartfren,0);
                    } else if (number.equals("9990") || number.equals("9991") || number.equals("9992") || number.equals("9993")
                            || number.equals("9994") || number.equals("9995") || number.equals("9996") || number.equals("9997")
                            || number.equals("9998") || number.equals("9999")) {
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,R.drawable.logo_bolt,0);
                    } else {
                        provider.setVisibility(View.VISIBLE);
                        layPilihNom.setVisibility(View.GONE);
                        layPilihKontak.setVisibility(View.GONE);
                        provider.setText("Data Tidak Ditemukan");
                        nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,0,0);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*nomor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                stubList.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stubList.setEnabled(false);
                if (nomor.getText().toString().length() >= 4) {
                    number = nomor.getText().toString().substring(0, 4);
                    if (number.equals("0857") || number.equals("0856") || number.equals("0858")) {
                        provider.setText("Indosat");
                        stubList.setVisibility(View.VISIBLE);
                        stubList.setLayoutResource(R.layout.child);
                        stubList.setLayoutInflater(getLayoutInflater());
                    } else if (number.equals("0859") || number.equals("0878") || number.equals("0819") || number.equals("0877")) {
                        provider.setText("XL");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0812") || number.equals("0852") || number.equals("0853") || number.equals("0821")
                            || number.equals("0813") || number.equals("0822")) {
                        provider.setText("Telkomsel");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0896") || number.equals("0895") || number.equals("0899") || number.equals("0897")) {
                        provider.setText("Three");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0838") || number.equals("0831") || number.equals("0832")) {
                        provider.setText("Axis");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0888")) {
                        provider.setText("Smartfren");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("9990") || number.equals("9991") || number.equals("9992") || number.equals("9993")
                            || number.equals("9994") || number.equals("9995") || number.equals("9996") || number.equals("9997")
                            || number.equals("9998") || number.equals("9999")) {
                        provider.setText("BOLT");
                        stubList.setVisibility(View.VISIBLE);
                    }
                } else {
                    provider.setText("");
                    stubList.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                stubList.setEnabled(true);
                if (nomor.getText().toString().length() >= 4) {
                    number = nomor.getText().toString().substring(0, 4);
                    if (nomor.getText().toString().substring(0, 4).equals("0857") || number.equals("0856") || number.equals("0858")) {
                        provider.setText("Indosat");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0878") || number.equals("0819") || number.equals("0877")) {
                        provider.setText("XL");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0812") || number.equals("0852") || number.equals("0853") || number.equals("0821")
                            || number.equals("0813")) {
                        provider.setText("Telkomsel");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0896") || number.equals("0895") || number.equals("0899")) {
                        provider.setText("Three");
                        stubList.setVisibility(View.VISIBLE);
                    } else if (number.equals("0838")) {
                        provider.setText("Axis");
                        stubList.setVisibility(View.VISIBLE);
                    }
                } else {
                    provider.setText("");
                    stubList.setVisibility(View.GONE);
                }
            }
        });*/
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
