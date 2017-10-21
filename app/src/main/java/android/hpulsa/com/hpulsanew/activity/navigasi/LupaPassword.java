package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.captcha.TextCaptcha;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

public class LupaPassword extends AppCompatActivity {

    FancyButton btnKirim;
    EditText eUsername, eEmail, eCaptcha;
    ImageView captcha,imgRefCaptcha;
    TextCaptcha textCaptcha;
    LinearLayout llayout;

    StaticVars sv = new StaticVars();
    hPulsaAPI api;

    String username, email, message, tCaptcha;
    ProgressDialog pLoading;
    int numberOfCaptchaFalse = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_pass);

        getSupportActionBar().setTitle("Lupa Kata Sandi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setComponent();
        init();
    }

    private void setComponent() {
        llayout = (LinearLayout) findViewById(R.id.llayout);
        btnKirim = (FancyButton) findViewById(R.id.btnKirim);
        eUsername = (EditText) findViewById(R.id.eUsername);
        eEmail = (EditText) findViewById(R.id.eEmail);
        captcha = (ImageView) findViewById(R.id.Captcha);
        imgRefCaptcha = (ImageView) findViewById(R.id.imgRefrCaptcha);
        eCaptcha = (EditText) findViewById(R.id.teksCaptcha);
        textCaptcha = new TextCaptcha(600, 150, 4, TextCaptcha.TextOptions.NUMBERS_ONLY);
    }

    private void init() {
        api = hPulsaAPI.service.create(hPulsaAPI.class);
        pLoading = new ProgressDialog(this);
        pLoading.setMessage("Mengirim kode verifikasi . . .");
        pLoading.setCancelable(true);
        captcha.setImageBitmap(textCaptcha.getImage());
        imgRefCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOfCaptchaFalse++;
                textCaptcha = new TextCaptcha(600, 150, 5, TextCaptcha.TextOptions.NUMBERS_ONLY);
                captcha.setImageBitmap(textCaptcha.getImage());
            }
        });
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = eUsername.getText().toString();
                email = eEmail.getText().toString();
                tCaptcha = eCaptcha.getText().toString();
                if (username.equals("") || email.equals("") || tCaptcha.equals("")) {
                    dialFail("Isi dengan lengkap", "Inputan tidak boleh kosong");
                } else if (!textCaptcha.checkAnswer(tCaptcha.trim())) {
                    dialFail("Captcha tidak cocok", "Periksa kembali inputan captcha Anda");
                    eCaptcha.setText("");
                } else {
                    cekKoneksi();
                }
            }
        });
    }

    private void cekKoneksi() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            Snackbar.make(llayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
        } else {

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
        return;
    }

    private void dialFail(String title, String content) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LupaPassword.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
