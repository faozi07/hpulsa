package android.hpulsa.com.hpulsanew.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.model.modHrgProduk;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozi on 08/10/2017.
 */

public class HargaProdukAdapter extends RecyclerView.Adapter {

    // http://android-pratap.blogspot.co.id/2015/06/endless-recyclerview-with-progress-bar.html

    public static Activity activity;
    public static ArrayList<modHrgProduk> items;
    modHrgProduk mrt;

    private final int VIEW_ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    StaticVars sv = new StaticVars();

    private int lastPosition = -1;

    public HargaProdukAdapter(Activity act, ArrayList<modHrgProduk> data){
        activity = act;
        this.items = data;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView jnsProduk,provider,kode,nominal,hrg,hrgJual,stok;
        CardView cardView;

        public BrandViewHolder(View v) {
            super(v);

            jnsProduk = (TextView) v.findViewById(R.id.tJnsProduk);
            provider = (TextView) v.findViewById(R.id.tProvider);
            kode = (TextView) v.findViewById(R.id.tKodeProduk);
            nominal = (TextView) v.findViewById(R.id.tNomProduk);
            hrg = (TextView) v.findViewById(R.id.tHrgProduk);
            hrgJual = (TextView) v.findViewById(R.id.tHrgJual);
            stok = (TextView) v.findViewById(R.id.tStok);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_harga_produk, parent, false);

            vh = new BrandViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BrandViewHolder) {

            final SharedPreferences spLahan = activity.getSharedPreferences(sv.token, Context.MODE_PRIVATE);

            mrt = items.get(position);

            double harga = Double.parseDouble(mrt.getHrg());
            double hargaJual = Double.parseDouble(mrt.getHrgJual());
            DecimalFormat kursInd = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');

            DecimalFormatSymbols formatNonRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');

            ((BrandViewHolder) holder).jnsProduk.setText(mrt.getJnsProduk());
            ((BrandViewHolder) holder).provider.setText(mrt.getProvider());
            ((BrandViewHolder) holder).kode.setText(mrt.getKode());
            ((BrandViewHolder) holder).nominal.setText(mrt.getNominal());
            ((BrandViewHolder) holder).hrg.setText(kursInd.format(harga));
            ((BrandViewHolder) holder).hrgJual.setText(kursInd.format(hargaJual));
            ((BrandViewHolder) holder).stok.setText(mrt.getStok());

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
