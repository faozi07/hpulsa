package android.hpulsa.com.hpulsanew.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.model.modNotif;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ozi on 08/10/2017.
 */

public class NotifAdapter extends RecyclerView.Adapter {

    // http://android-pratap.blogspot.co.id/2015/06/endless-recyclerview-with-progress-bar.html

    public static Activity activity;
    public static ArrayList<modNotif> items;
    modNotif mrt;

    private final int VIEW_ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    StaticVars sv = new StaticVars();

    private int lastPosition = -1;

    public NotifAdapter(Activity act, ArrayList<modNotif> data){
        activity = act;
        this.items = data;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView judul,konten,tanggal;
        CardView cardView;

        public BrandViewHolder(View v) {
            super(v);

            judul = (TextView) v.findViewById(R.id.tJudul);
            konten = (TextView) v.findViewById(R.id.tKonten);
            tanggal = (TextView) v.findViewById(R.id.tTglNotif);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_notif, parent, false);

            vh = new BrandViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BrandViewHolder) {

            final SharedPreferences spLahan = activity.getSharedPreferences(sv.token, Context.MODE_PRIVATE);

            mrt = items.get(position);

            ((BrandViewHolder) holder).judul.setText(mrt.getJudul());
            ((BrandViewHolder) holder).konten.setText(mrt.getKonten());
            ((BrandViewHolder) holder).tanggal.setText(mrt.getTanggal());

            setAnimation(((BrandViewHolder) holder).cardView, position);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);

        }
    }

    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.zoomin);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
