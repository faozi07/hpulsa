package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profil extends AppCompatActivity {

    private TextView eNama, eUsername, eNoHp, eEmail, eSttsAkun, eTtlTrx, eJnsAkun, eTglMndftr, eSaldo;
    private Button btnTbhSaldo;
    private ImageView imgVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        btnTbhSaldo = (Button) findViewById(R.id.btnTbhSaldo);
        imgVerified = (ImageView) findViewById(R.id.imgVerif);
    }

    private void setComponent() {
        SharedPreferences spProfil = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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
        eTtlTrx.setText(spProfil.getString(sv.total_order, ""));
        eJnsAkun.setText(spProfil.getString(sv.reseller, ""));

        Integer tglReg = Integer.valueOf(spProfil.getString(sv.reg_date, ""));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date tglRegis = sdf.parse(tglReg.toString());
            eTglMndftr.setText(tglRegis.toString());
        } catch (Exception e) {
            eTglMndftr.setText("-");
        }
    }

    private void action() {
        btnTbhSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profil.this,TopupSaldo.class));
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
}
