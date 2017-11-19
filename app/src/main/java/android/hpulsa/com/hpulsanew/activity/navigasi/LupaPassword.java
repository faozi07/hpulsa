package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.API.ClientAPI;
import android.hpulsa.com.hpulsanew.API.hPulsaAPI;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PaketTelpSMS;
import android.hpulsa.com.hpulsanew.adapter.LupaPassPagerAdapter;
import android.hpulsa.com.hpulsanew.adapter.gabunganAdapter;
import android.hpulsa.com.hpulsanew.captcha.TextCaptcha;
import android.hpulsa.com.hpulsanew.model.modNomPulsa;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.hpulsa.com.hpulsanew.util.ViewLupaPassPager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LupaPassword extends AppCompatActivity {

    static ViewLupaPassPager viewLupaPassPager;
    LupaPassPagerAdapter lupaPassPagerAdapter;

    static RoundCornerProgressBar progressView;
    public static int currentPage = 0;

    public static String tokenreset = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_pass);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lupa Kata Sandi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupComponent();
        nextPage(0);
    }

    private void setupComponent(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        lupaPassPagerAdapter = new LupaPassPagerAdapter(fragmentManager);

        viewLupaPassPager = (ViewLupaPassPager) findViewById(R.id.viewpager_lupapass);
        viewLupaPassPager.setAdapter(lupaPassPagerAdapter);

        progressView = (RoundCornerProgressBar) findViewById(R.id.lupapass_progress_bar);
    }


    public static void nextPage(int goTo){
        viewLupaPassPager.setScrollDurationFactor(3);
        viewLupaPassPager.setCurrentItem(goTo, true);

        setProgress();
        currentPage = currentPage+1;
    }

    public static void previosPage(int goTo){
        viewLupaPassPager.setScrollDurationFactor(3);
        viewLupaPassPager.setCurrentItem(goTo-1, true);

        setProgressDown();
        currentPage = currentPage-1;
    }

    public static void setProgress(){
        int percentage = ((currentPage+1)*100)/3;
        progressView.setProgress(percentage);
    }

    public static void setProgressDown(){
        int percentage = ((currentPage-1)*100)/3;
        progressView.setProgress(percentage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        dialogBack();
    }

    private void dialogBack() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LupaPassword.this);
        alertDialogBuilder.setTitle("Perhatian");
        alertDialogBuilder
                .setMessage("Apa Anda yakin ingin keluar dari proses ini ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        previosPage(currentPage-1);
                        if (currentPage<=0) {
                            finish();
                        }
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
}
