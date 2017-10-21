package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.content.Intent;
import android.hpulsa.com.hpulsanew.adapter.listRiwayatAdapter;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RiwayatTransaksi extends AppCompatActivity {

    public static EditText eNoHp;
    private ListView listRiwayat;
    private ImageView imgSearch;
    private FloatingActionButton fabKetSttus;

    private List<modRiwayat> modRiwayats;
    private listRiwayatAdapter listRiwayatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_transaksi);

        getSupportActionBar().setTitle("Riwayat Transaksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setComponent();
    }

    private void setComponent() {
        eNoHp = (EditText) findViewById(R.id.editNoHp);
        listRiwayat = (ListView) findViewById(R.id.list_riwayat);
        imgSearch = (ImageView) findViewById(R.id.imgSearh);
        fabKetSttus = (FloatingActionButton) findViewById(R.id.fabKetStatus);

        modRiwayats = new ArrayList<>();

        addItemListView();

        listRiwayat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(RiwayatTransaksi.this,DetilRiwayat.class));
            }
        });

    }

    private void addItemListView() {
        modRiwayats.add(new modRiwayat("Transaksi#1","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#2","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#3","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#3","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#5","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#6","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#7","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));
        modRiwayats.add(new modRiwayat("Transaksi#8","WP","085797511XXX","TSEL 5.000","02/10/2017 - 03:09","Rp. 5.650"));

        listRiwayatAdapter = new listRiwayatAdapter(this,modRiwayats);
        listRiwayat.setAdapter(listRiwayatAdapter);
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
