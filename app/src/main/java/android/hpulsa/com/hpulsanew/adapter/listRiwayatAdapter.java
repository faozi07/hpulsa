package android.hpulsa.com.hpulsanew.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozi on 08/10/2017.
 */

public class listRiwayatAdapter extends RecyclerView.Adapter {

    // http://android-pratap.blogspot.co.id/2015/06/endless-recyclerview-with-progress-bar.html

    public static Activity activity;
    public static ArrayList<modRiwayat> items;
    modRiwayat mrt;

    private final int VIEW_ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    StaticVars sv = new StaticVars();

    private int lastPosition = -1;

    public listRiwayatAdapter(Activity act, ArrayList<modRiwayat> data){
        activity = act;
        this.items = data;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView tNoTrx,tSttsTrx,tTglTrx,tJnsPmbayrn,tNoHp,tProvider,tVoucher,tHarga;
        CardView cardView;

        public BrandViewHolder(View v) {
            super(v);

            tNoTrx = (TextView) v.findViewById(R.id.tNoTransaksi);
            tSttsTrx = (TextView) v.findViewById(R.id.tSttsTrx);
            tTglTrx = (TextView) v.findViewById(R.id.tTglTransaksi);
            tJnsPmbayrn = (TextView) v.findViewById(R.id.tJnsPmbayaran);
            tNoHp = (TextView) v.findViewById(R.id.tNoHp);
            tProvider = (TextView) v.findViewById(R.id.tProvider);
            tVoucher = (TextView) v.findViewById(R.id.tVoucher);
            tHarga = (TextView) v.findViewById(R.id.tHarga);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_list_riwayat, parent, false);

            vh = new BrandViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BrandViewHolder) {

            final SharedPreferences spLahan = activity.getSharedPreferences(sv.token, Context.MODE_PRIVATE);
            final modRiwayat mr = items.get(position);

            mrt = items.get(position);
            int pos = position+1;
            ((BrandViewHolder) holder).tNoTrx.setText("#"+pos);
            ((BrandViewHolder) holder).tSttsTrx.setText(mrt.getTrxStts());
            if (mrt.getTrxStts().equals("IP")) {
                ((BrandViewHolder) holder).tSttsTrx.setBackgroundResource(R.drawable.bg_ip);
            } else if (mrt.getTrxStts().equals("OK")) {
                ((BrandViewHolder) holder).tSttsTrx.setBackgroundResource(R.drawable.bg_ok);
            } else if (mrt.getTrxStts().equals("CL")) {
                ((BrandViewHolder) holder).tSttsTrx.setBackgroundResource(R.drawable.bg_cl);
            }
            ((BrandViewHolder) holder).tTglTrx.setText(mrt.getTrxTgl());
            ((BrandViewHolder) holder).tJnsPmbayrn.setText(mrt.getTrxPembayaran());
            ((BrandViewHolder) holder).tNoHp.setText(mrt.getTrxNoHP());
            ((BrandViewHolder) holder).tProvider.setText(mrt.getOpNama());
            ((BrandViewHolder) holder).tVoucher.setText(mrt.getVoNominal());
            ((BrandViewHolder) holder).tHarga.setText(mrt.getTrxHarga());

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
