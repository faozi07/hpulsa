package android.hpulsa.com.hpulsanew.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;

public class TrialHabis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_habis);
        getSupportActionBar().setTitle("Waktu Trial Habis");
    }
}
