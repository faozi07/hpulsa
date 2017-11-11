package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.AlertDialog;
import android.content.Context;
import android.hpulsa.com.hpulsanew.fragment.FrRegisterActivity;
import android.hpulsa.com.hpulsanew.fragment.FrRiwayatDeposit;
import android.hpulsa.com.hpulsanew.fragment.FrRiwayatTrx;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;

public class RiwayatTransaksi extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private FloatingActionButton fabKetSttus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_transaksi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new RiwayatTransaksi.CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        fabKetSttus = (FloatingActionButton) findViewById(R.id.fabKetStatus);
        fabKetSttus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(RiwayatTransaksi.this);
                View dialog_layout = inflater.inflate(R.layout.ket_status, null);

                AlertDialog.Builder dialogBuatTes = new AlertDialog.Builder(RiwayatTransaksi.this);
                dialogBuatTes.setView(dialog_layout);
                final AlertDialog theDialog = dialogBuatTes.create();

                FancyButton btnOk = (FancyButton) dialog_layout.findViewById(R.id.btn_ok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theDialog.dismiss();
                    }
                });

                theDialog.show();
            }
        });
    }
    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments [] = {"TRANSAKSI","DEPOSIT"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FrRiwayatTrx();
                case 1:
                    return new FrRiwayatDeposit();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        FrRiwayatTrx.offset=0;
        FrRiwayatTrx.is_first=true;
        FrRiwayatDeposit.offset=0;
        FrRiwayatDeposit.is_first=true;
        return;
    }

}
