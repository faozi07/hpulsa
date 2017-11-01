package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.view.View;
import android.widget.TextView;

public class DetilRiwayat extends AppCompatActivity {

    private TextView tJnsProduk,tProvider,tNominal,tNoHp,tHarga,tJnsPembayaran,tTglPembelian,tSttsPembayaran,tSttsTrx;
    public static String JnsProduk,Provider,Nominal,NoHp,Harga,JnsPembayaran,TglPembelian,SttsPembayaran,SttsTrx,TrxID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detil_riwayat);

        declaration();
        setKonten();

        getSupportActionBar().setTitle("Detil Transaksi #"+TrxID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declaration() {
        tJnsProduk = (TextView) findViewById(R.id.tJnsProduk);
        tProvider = (TextView) findViewById(R.id.tProvider);
        tNominal = (TextView) findViewById(R.id.tNominal);
        tNoHp = (TextView) findViewById(R.id.tNoHp);
        tHarga = (TextView) findViewById(R.id.tHarga);
        tJnsPembayaran = (TextView) findViewById(R.id.tJnsPmbayaran);
        tTglPembelian = (TextView) findViewById(R.id.tTglPembelian);
        tSttsPembayaran = (TextView) findViewById(R.id.tSttsPembayaran);
        tSttsTrx = (TextView) findViewById(R.id.tSttsTrx);
    }

    private void setKonten() {
        tJnsProduk.setText(JnsProduk);
        tProvider.setText(Provider);
        tNominal.setText(Nominal);
        tNoHp.setText(NoHp);
        tHarga.setText(Harga);
        tJnsPembayaran.setText(JnsPembayaran);
        tTglPembelian.setText(TglPembelian);
        tSttsPembayaran.setText(SttsPembayaran);
        tSttsTrx.setText(SttsTrx);
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
