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
import android.widget.TextView;

public class Profil extends AppCompatActivity {

    private TextView eNama, eUsername, eNoHp, eEmail, eSttsAkun, eTtlTrx, eJnsAkun, eTglMndftr, eSaldo;
    private Button btnTbhSaldo;

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
    }

    private void setComponent() {
        SharedPreferences spProfil = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        StaticVars sv = new StaticVars();
        eUsername.setText(spProfil.getString(sv.username, ""));
        eNama.setText(spProfil.getString(sv.name, ""));
        eEmail.setText(spProfil.getString(sv.email, ""));
        eNoHp.setText(spProfil.getString(sv.phone, ""));
        eSaldo.setText("Rp. "+spProfil.getString(sv.balance, ""));
        eSttsAkun.setText(spProfil.getString(sv.verified, ""));
        eTtlTrx.setText(spProfil.getString(sv.total_order, ""));
        eJnsAkun.setText(spProfil.getString(sv.reseller, ""));
        eTglMndftr.setText(spProfil.getString(sv.reg_date, ""));
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
