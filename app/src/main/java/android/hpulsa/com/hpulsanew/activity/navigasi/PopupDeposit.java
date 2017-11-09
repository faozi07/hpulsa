package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PopupTransfer;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import mehdi.sakout.fancybuttons.FancyButton;

public class PopupDeposit extends AppCompatActivity {

    public static TextView tJmlBayar,tNorek,tAtasnama,tTglDeposit;
    public static ImageView imgBank;
    FancyButton btnSimpanSS,btnBayar;
    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_deposit);

        declaration();
        action();

        getSupportActionBar().setTitle("Transfer Deposit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declaration() {
        StaticVars sv = new StaticVars();
        tJmlBayar = (TextView) findViewById(R.id.tJmlBayar);
        tNorek = (TextView) findViewById(R.id.tnoRek);
        tAtasnama = (TextView) findViewById(R.id.tAtasNama);
        tTglDeposit = (TextView) findViewById(R.id.tglDeposit);
        tTglDeposit.setText(sv.tglTopup);
        imgBank = (ImageView) findViewById(R.id.imgBank);
        double tagihan = Double.parseDouble(sv.jmlTopup);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        tJmlBayar.setText(kursIndonesia.format(tagihan));
        tNorek.setText("No. Rekening : "+sv.noRek);
        tAtasnama.setText("a/n "+sv.namaRek);
        if (sv.namaBank.equals("bank_bca")){
            imgBank.setImageResource(R.drawable.ic_logo_bca);
        } else if (sv.namaBank.equals("bank_bri")) {
            imgBank.setImageResource(R.drawable.ic_logo_bri);
        } else if (sv.namaBank.equals("bank_bni")) {
            imgBank.setImageResource(R.drawable.ic_logo_bni);
        } else if (sv.namaBank.equals("bank_mandiri")) {
            imgBank.setImageResource(R.drawable.ic_logo_mandiri);
        }

        btnSimpanSS = (FancyButton) findViewById(R.id.btnSimpanSS);
        btnBayar = (FancyButton) findViewById(R.id.btnBayar);

    }

    private void action() {
        btnSimpanSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanSS();
            }
        });
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void simpanSS(){
        Date now = new Date();
        DateFormat.format("yyyy-MM-dd_hh:mm:ss",now);
        try {
            String mPath = Environment.getExternalStorageDirectory().toString()+"/"+now+".jpg";

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imgFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imgFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG,quality,outputStream);
            outputStream.flush();
            outputStream.close();
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    dialogSaveSS("Berhasil simpan data pembayaran di galeri","Buka sekarang ?");
                } else {
                    ActivityCompat.requestPermissions(PopupDeposit.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                Toast.makeText(PopupDeposit.this,"Berhasil simpan data pembayaran di galeri",Toast.LENGTH_LONG);
                dialogSaveSS("Berhasil simpan data pembayaran di galeri","Buka sekarang ?");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1313: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission granted  ", "permission granted");
                } else {
                    Log.d("permission denied  ", "permission denied");
                }
                return;
            }
        }
    }

    private void openSS(File imgFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imgFile);
        intent.setDataAndType(uri,"image/*");
        startActivity(intent);
        finish();
    }

    private void dialogSaveSS(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PopupDeposit.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        dialog.dismiss();
                        openSS(imgFile);
                    }
                })
                .setNegativeButton("NANTI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        dialog.dismiss();
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
        return;
    }
}
