package android.hpulsa.com.hpulsanew.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.model.modDeposit;
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
 * Created by ozi on 11/11/2017.
 */

public class depositAdapter extends RecyclerView.Adapter {

    // http://android-pratap.blogspot.co.id/2015/06/endless-recyclerview-with-progress-bar.html

    public static Activity activity;
    public static ArrayList<modDeposit> items;
    modDeposit mrt;

    private final int VIEW_ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    StaticVars sv = new StaticVars();

    private int lastPosition = -1;

    public depositAdapter(Activity act, ArrayList<modDeposit> data) {
        activity = act;
        this.items = data;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView tBank, tSttsDpst, tTglDpst, tJmlDeposit;
        CardView cardView;

        public BrandViewHolder(View v) {
            super(v);

            tBank = (TextView) v.findViewById(R.id.tBank);
            tSttsDpst = (TextView) v.findViewById(R.id.tSttsDeposit);
            tTglDpst = (TextView) v.findViewById(R.id.tTglDeposit);
            tJmlDeposit = (TextView) v.findViewById(R.id.tJmlDeposit);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_deposit, parent, false);

            vh = new depositAdapter.BrandViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof depositAdapter.BrandViewHolder) {

            final SharedPreferences spLahan = activity.getSharedPreferences(sv.token, Context.MODE_PRIVATE);

            mrt = items.get(position);
            int pos = position + 1;
            ((BrandViewHolder) holder).tBank.setText("#" + mrt.getBank());
            ((BrandViewHolder) holder).tJmlDeposit.setText(mrt.getJmlDeposit());
            if (mrt.getSttsDeposit().equals("pending")) {
                ((BrandViewHolder) holder).tSttsDpst.setBackgroundResource(R.drawable.bg_ip);
                ((BrandViewHolder) holder).tSttsDpst.setText("IP");
            } else if (mrt.getSttsDeposit().equals("sukses")) {
                ((BrandViewHolder) holder).tSttsDpst.setBackgroundResource(R.drawable.bg_ok);
                ((BrandViewHolder) holder).tSttsDpst.setText("OK");
            } else if (mrt.getSttsDeposit().equals("gagal")) {
                ((BrandViewHolder) holder).tSttsDpst.setBackgroundResource(R.drawable.bg_cl);
                ((BrandViewHolder) holder).tSttsDpst.setText("CL");
            }
            ((BrandViewHolder) holder).tTglDpst.setText(mrt.getTglDeposit());

            ((BrandViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*mrt = items.get(position);
                    DetilRiwayat dr = new DetilRiwayat();
                    dr.TrxID = mrt.getTrxID();
                    dr.JnsProduk = mrt.getOpProduk();
                    dr.Provider = mrt.getOpProduk();
                    dr.Nominal = mrt.getVoNominal();
                    dr.NoHp = mrt.getTrxNoHP();
                    dr.Harga = mrt.getTrxHarga();
                    dr.JnsPembayaran = mrt.getTrxPembayaran();
                    dr.TglPembelian = mrt.getTrxTgl();
                    dr.SttsPembayaran = mrt.getTrxSttsPembayaran();
                    dr.SttsTrx = mrt.getTrxStts();
                    activity.startActivity(new Intent(activity, DetilRiwayat.class));*/
                }
            });

            setAnimation(((depositAdapter.BrandViewHolder) holder).cardView, position);

        } else {
            ((depositAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
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

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.zoomin);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
