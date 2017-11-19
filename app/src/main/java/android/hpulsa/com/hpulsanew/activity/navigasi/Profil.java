package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profil extends AppCompatActivity {

    private TextView eNama, eUsername, eNoHp, eEmail, eSttsAkun, eTtlTrx, eJnsAkun, eTglMndftr, eSaldo,eAlamat,ejenkel,tLogout;
    private ImageView imgVerified;
    private LinearLayout laySaldo;
    StaticVars sv = new StaticVars();
    private SharedPreferences spProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        init();
        setComponent();
        action();
    }

    private void init() {
        eNama = (TextView) findViewById(R.id.eNama);
        eUsername = (TextView) findViewById(R.id.eUsername);
        eNoHp = (TextView) findViewById(R.id.eNoHp);
        eEmail = (TextView) findViewById(R.id.eEmail);
        eSaldo = (TextView) findViewById(R.id.eSaldo);
        eSttsAkun = (TextView) findViewById(R.id.eSttsAkun);
        eTtlTrx = (TextView) findViewById(R.id.eTtlTrx);
        eJnsAkun = (TextView) findViewById(R.id.eJnsAkun);
        eTglMndftr = (TextView) findViewById(R.id.eTglMendaftar);
        imgVerified = (ImageView) findViewById(R.id.imgVerif);
        ejenkel = (TextView) findViewById(R.id.eJenkel);
        eAlamat = (TextView) findViewById(R.id.eAlamat);
        laySaldo = (LinearLayout) findViewById(R.id.laySaldo);
        tLogout = (TextView) findViewById(R.id.tLogout);
    }

    private void setComponent() {
        spProfil = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        StaticVars sv = new StaticVars();
        double saldo = Double.parseDouble(spProfil.getString(sv.balance, ""));
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        eUsername.setText(spProfil.getString(sv.username, ""));
        eNama.setText(spProfil.getString(sv.name, ""));
        eEmail.setText(spProfil.getString(sv.email, ""));
        eNoHp.setText(spProfil.getString(sv.phone, ""));
        eSaldo.setText(kursIndonesia.format(saldo));
        if (spProfil.getString(sv.verifPhone, "").equals("1") && spProfil.getString(sv.verifEmail, "").equals("1")) {
            eSttsAkun.setText("Terverifikasi");
            eSttsAkun.setTextColor(getResources().getColor(R.color.green));
            imgVerified.setVisibility(View.VISIBLE);
        } else {
            eSttsAkun.setText("Belum Terverifikasi");
            imgVerified.setVisibility(View.GONE);
        }
        eTtlTrx.setText(spProfil.getString(sv.total_order, "")+" Transaksi sukses");
        eJnsAkun.setText(spProfil.getString(sv.reseller, ""));
/*
        Integer tglReg = Integer.valueOf(spProfil.getString(sv.reg_date, ""));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date tglRegis = sdf.parse(tglReg.toString());
            eTglMndftr.setText(tglRegis.toString());
        } catch (Exception e) {
            eTglMndftr.setText("-");
        }*/

        if (spProfil.getString(sv.gender,"").equals("male")) {
            ejenkel.setText("Laki-laki");
        } else { ejenkel.setText("Perempuan");}

        eAlamat.setText(spProfil.getString(sv.location,""));
    }

    private void action() {
        laySaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profil.this,TopupSaldo.class));
            }
        });
        tLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogout();
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
        startActivity(new Intent(Profil.this,MenuUtama.class));
        return;
    }

    private void dialogLogout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder
                .setMessage("Anda yakin ingin logout ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                        finish();
                        startActivity(new Intent(Profil.this, SignActivity.class));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticVars.token, "");
        editor.apply();
        Toast.makeText(Profil.this,"Berhasil logout",Toast.LENGTH_LONG).show();
    }
}
