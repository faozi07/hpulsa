package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PaketBBM;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PaketInternet;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PaketTelpSMS;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PulsaHP;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.SaldoGojek;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.Tagihan;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.TokenPln;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.VoucherGame;
import android.hpulsa.com.hpulsanew.adapter.SlideMenuAdapter;
import android.hpulsa.com.hpulsanew.model.ItemSlideMenu;
import android.hpulsa.com.hpulsanew.adapter.gridViewAdapter;
import android.hpulsa.com.hpulsanew.model.modeKetMenu;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuUtama extends AppCompatActivity {

    private List<ItemSlideMenu> listMenu;
    private SlideMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView imgMenu;

    private ViewStub stubGrid;
    private GridView gridView;
    private gridViewAdapter gridViewAdapter;
    private List<modeKetMenu> modEarningList;
    public static TextView nama, noTelp, tSaldo;
    LinearLayout laySaldo;
    public static CircleImageView fotoProfil;
    StaticVars sv = new StaticVars();
    private Toolbar toolbar;
    private hPulsaAPI api;
    private SharedPreferences spLogin;
    private TextView tNoHpBeranda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);

        setComponent();
        addItemSliding();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupDrawerToggle();

        final LinearLayout layout = (LinearLayout) findViewById(R.id.appBar);
        final ImageView imgSaldo = (ImageView) findViewById(R.id.imgSaldo);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                if (width<=480) {
                    tNoHpBeranda.setTextSize(15);
                    tSaldo.setTextSize(12);
//                    imgSaldo.set
                } else {
                    tNoHpBeranda.setTextSize(17);
                    tSaldo.setTextSize(13);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listViewSliding.setItemChecked(0,true);
        drawerLayout.closeDrawer(listViewSliding);
        itemClickMenu();
    }

    private void setComponent(){
        api = hPulsaAPI.service.create(hPulsaAPI.class);
        listViewSliding = (ListView) findViewById(R.id.list_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);
        listMenu = new ArrayList<>();
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        stubGrid.inflate();
        gridView = (GridView) findViewById(R.id.mygridview);
        gridView.setOnItemClickListener(onItemClick);
        getModKetMenuList();
        gridViewAdapter = new gridViewAdapter(MenuUtama.this, R.layout.grid_item, modEarningList);
        gridView.setAdapter(gridViewAdapter);

        LayoutInflater inflater = getLayoutInflater();
        View navHeader = inflater.inflate(R.layout.header_profile, null, false);//header
        listViewSliding.addHeaderView(navHeader);
        nama = (TextView) navHeader.findViewById(R.id.namaProfil);
        noTelp = (TextView) navHeader.findViewById(R.id.noTelp);
        CircleImageView imgProfil = (CircleImageView) navHeader.findViewById(R.id.imageProfil);
        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MenuUtama.this,Profil.class));
                drawerLayout.closeDrawer(Gravity.START);
            }
        });
        nama.setText(sv.name);
        noTelp.setText(sv.phone);
        fotoProfil = (CircleImageView) navHeader.findViewById(R.id.imageProfil);
        tSaldo = (TextView) findViewById(R.id.tSaldo);
        double saldo = Double.parseDouble(sv.balance);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String saldo1 = kursIndonesia.format(saldo);
        if (saldo1.contains(",")) {
            saldo1 = saldo1.substring(0,saldo1.length()-3);
        }

        tSaldo.setText(saldo1);
        laySaldo = (LinearLayout) findViewById(R.id.laySaldo);
        laySaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUtama.this,TopupSaldo.class));
            }
        });
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        tNoHpBeranda = (TextView) findViewById(R.id.noHpBeranda);
        tNoHpBeranda.setText(sv.phone);
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            switch (position) {
                case 0:
                    startActivity(new Intent(MenuUtama.this, PulsaHP.class));break;
                case 1:
                    startActivity(new Intent(MenuUtama.this, PaketInternet.class));break;
                case 2:
                    startActivity(new Intent(MenuUtama.this, TokenPln.class));break;
                case 3:
                    startActivity(new Intent(MenuUtama.this, VoucherGame.class));break;
                case 4:
                    startActivity(new Intent(MenuUtama.this, PaketTelpSMS.class));break;
                case 5:
                    startActivity(new Intent(MenuUtama.this, PaketBBM.class));break;
                case 6:
                    startActivity(new Intent(MenuUtama.this, SaldoGojek.class));break;
                case 7:
                    startActivity(new Intent(MenuUtama.this, Tagihan.class));break;
            }
        }
    };

    private void dialogKeluar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setMessage("Anda ingin keluar dari HPulsa ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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

    public List<modeKetMenu> getModKetMenuList() {
        modEarningList = new ArrayList<>();
        modEarningList.add(new modeKetMenu("Pulsa"));
        modEarningList.add(new modeKetMenu("Paket Internet"));
        modEarningList.add(new modeKetMenu("Token PLN"));
        modEarningList.add(new modeKetMenu("Voucher Game"));
        modEarningList.add(new modeKetMenu("Paket Telpon\ndan SMS"));
        modEarningList.add(new modeKetMenu("Paket Blackberry"));
        modEarningList.add(new modeKetMenu("Saldo Gojek"));
        modEarningList.add(new modeKetMenu("Tagihan"));
        return modEarningList;
    }

    private void cekSaldo() {

        spLogin = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = spLogin.getString(sv.token,"");
        api.userProfil(sv.publickey,sv.privatekey,token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.code() == 200) {
                        SharedPreferences.Editor editor = spLogin.edit();

                        JsonObject body = response.body();
                        if (body.has("message")) {
                            Toast.makeText(MenuUtama.this, "Gagal update saldo, silahkan reload aplikasi", Toast.LENGTH_SHORT).show();
                        } else {
                            if (body.has("us_balance")) {
                                sv.balance = body.get("us_balance").getAsString();
                                editor.putString(sv.balance, body.get("us_balance").getAsString());
                            }
                            editor.apply();
                        }
                    } else if (response.code() == 400 || response.code() == 401) {
                        Toast.makeText(MenuUtama.this, "Gagal update saldo, silahkan reload aplikasi", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private void addItemSliding() {
        cekSaldo();
        listMenu.add(new ItemSlideMenu(R.drawable.ic_topup,R.drawable.ic_pembatas,"Topup Saldo",""));
        listMenu.add(new ItemSlideMenu(R.drawable.ic_affiliate,R.drawable.ic_pembatas,"Affiliate",""));
        listMenu.add(new ItemSlideMenu(R.drawable.ic_profile,R.drawable.ic_pembatas,"Profil",""));
        listMenu.add(new ItemSlideMenu(R.drawable.ic_verified,R.drawable.ic_pembatas,"Verifikasi Akun",""));
        listMenu.add(new ItemSlideMenu(R.drawable.ic_riwayat,R.drawable.ic_pembatas,"Riwayat",""));
        listMenu.add(new ItemSlideMenu(R.drawable.ic_notif,R.drawable.ic_pembatas,"Pemberitahuan",""));
        listMenu.add(new ItemSlideMenu(R.drawable.ic_testimoni,R.drawable.ic_pembatas,"Testimonial",""));

        adapter = new SlideMenuAdapter(this,listMenu);
        listViewSliding.setAdapter(adapter);
    }

    private void itemClickMenu() {
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 1:
                        startActivity(new Intent(MenuUtama.this, TopupSaldo.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        finish();
                        startActivity(new Intent(MenuUtama.this, Profil.class));
                        break;
                    case 4:
                        startActivity(new Intent(MenuUtama.this, Verifikasi.class));
                        break;
                    case 5:
                        startActivity(new Intent(MenuUtama.this, RiwayatTransaksi.class));
                        break;
                    case 6:
                        startActivity(new Intent(MenuUtama.this, Pemberitahuan.class));
                        break;
                    /*case 9:
                        startActivity(new Intent(MenuUtama.this, HargaProduct.class));
                        break;*/
                    case 7:
                        startActivity(new Intent(MenuUtama.this, Testimoni.class));
                        break;
                }
                listViewSliding.setItemChecked(position,true);
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            dialogKeluar();
        }
    }
}
