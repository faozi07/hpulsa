package android.hpulsa.com.hpulsanew.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.navigasi.Verifikasi;
import android.hpulsa.com.hpulsanew.model.modNomPulsa;
import android.hpulsa.com.hpulsanew.util.StaticVars;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by ozi on 29/10/2017.
 */

public class pulsaAdapter extends RecyclerView.Adapter {

    public static Activity activity;
    public static ArrayList<modNomPulsa> items;
    modNomPulsa mrt;

    private final int VIEW_ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading,isTersedia = true;;
    StaticVars sv = new StaticVars();

    private int lastPosition = -1;
    double nominal, harga;
    DecimalFormat kursInd,nonKurs;
    DecimalFormatSymbols formatRp,formatNonRp;

    public pulsaAdapter(Activity act, ArrayList<modNomPulsa> data){
        activity = act;
        this.items = data;
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView tNominal,tHarga;
        CardView cardView;
        ImageView imgTersedia;

        public BrandViewHolder(View v) {
            super(v);

            tNominal = (TextView) v.findViewById(R.id.tNominal);
            tHarga = (TextView) v.findViewById(R.id.tHarga);
            imgTersedia = (ImageView) v.findViewById(R.id.imgTersedia);
            cardView = (CardView) v.findViewById(R.id.cardView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_nominal, parent, false);

            vh = new pulsaAdapter.BrandViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof pulsaAdapter.BrandViewHolder) {

            final SharedPreferences spLahan = activity.getSharedPreferences(sv.token, Context.MODE_PRIVATE);

            mrt = items.get(position);

            nominal = Double.parseDouble(mrt.getHrg());
            harga = Double.parseDouble(mrt.getHrgJual());
            kursInd = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursInd.setDecimalFormatSymbols(formatRp);

            nonKurs = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            formatNonRp = new DecimalFormatSymbols();
            formatNonRp.setCurrencySymbol("");
            formatNonRp.setMonetaryDecimalSeparator(',');
            formatNonRp.setGroupingSeparator('.');
            nonKurs.setDecimalFormatSymbols(formatNonRp);

            ((BrandViewHolder) holder).tNominal.setText(nonKurs.format(nominal));
            ((BrandViewHolder) holder).tHarga.setText(kursInd.format(harga));

            if (mrt.getStok().equals("0")) {
                ((BrandViewHolder) holder).imgTersedia.setImageResource(R.drawable.ic_stop);
                ((BrandViewHolder) holder).cardView.setBackgroundResource(R.color.abu);
                isTersedia = false;
            } else {
                ((BrandViewHolder) holder).imgTersedia.setImageResource(R.drawable.ic_plus);
                ((BrandViewHolder) holder).cardView.setBackgroundResource(R.color.putih);
                isTersedia = true;
            }
   
            setAnimation(((pulsaAdapter.BrandViewHolder) holder).cardView, position);

            ((BrandViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isTersedia) {
                        LayoutInflater inflater = LayoutInflater.from(activity);
                        View v = inflater.inflate(R.layout.popup_bayar, null);

                        AlertDialog.Builder dialogBayar = new AlertDialog.Builder(activity);
                        dialogBayar.setView(v);
                        final AlertDialog theDialog = dialogBayar.create();

                        final TextView tJmlBayar = (TextView) v.findViewById(R.id.tJmlBayar);
                        tJmlBayar.setText(kursInd.format(harga));

                        FancyButton btnBayar = (FancyButton) v.findViewById(R.id.btnBayar);
                        btnBayar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                theDialog.dismiss();
                            }
                        });

                        theDialog.show();
                    }
                }
            });

        } else {
            ((pulsaAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
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
