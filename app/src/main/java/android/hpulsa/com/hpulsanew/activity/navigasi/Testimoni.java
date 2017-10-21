package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hpulsa.com.hpulsanew.R;

public class Testimoni extends AppCompatActivity {

    FloatingActionButton fabBuatTesti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testimoni);
        getSupportActionBar().setTitle("Testimonial");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void declaration() {

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
