package android.hpulsa.com.hpulsanew.activity.navigasi;

import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.adapter.NotifAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class Pemberitahuan extends AppCompatActivity {

    SwipeRefreshLayout swipeRefresh;
    RecyclerView rvNotif;
    NotifAdapter notifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pemberitahuan);

        declaration();
        action();
    }

    private void declaration() {
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipRefresh);
        rvNotif = (RecyclerView) findViewById(R.id.rvPemberitahuan);
    }

    private void action() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
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
