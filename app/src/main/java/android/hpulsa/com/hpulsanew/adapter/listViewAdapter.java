package android.hpulsa.com.hpulsanew.adapter;

import android.content.Context;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.activity.pilihanmenu.PulsaHP;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import android.hpulsa.com.hpulsanew.model.modNomPulsa;

public class listViewAdapter extends ArrayAdapter<modNomPulsa> {
    PulsaHP pa = new PulsaHP();
    private Context _context;
    public static ImageView imgMenu;
    public static int nom = 0, hrg = 0;
    String nominal = "", harga = "", provider;
    TextView tNominal, tHargaNom, teks;
    DecimalFormat kursIndonesia, kursKsng;
    int posisi;
    View v;
    public listViewAdapter(Context context, int resource, List<modNomPulsa> objects) {
        super(context, resource, objects);
        _context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = convertView;
        posisi = position;
        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_nominal, null);
        }
        modNomPulsa mE = getItem(position);
        tNominal = (TextView) v.findViewById(R.id.nominal);
        tHargaNom = (TextView) v.findViewById(R.id.harga);
        teks = (TextView) v.findViewById(R.id.teks);
        provider = PulsaHP.provider.getText().toString();
        kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursKsng = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        DecimalFormatSymbols formatKsng = new DecimalFormatSymbols();

        pa.nomor.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact,0,0,0);
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        formatKsng.setCurrencySymbol("");
        formatKsng.setMonetaryDecimalSeparator(',');
        formatKsng.setGroupingSeparator('.');
        hrg = nom + 2000;
        nominal = kursKsng.format(nom);
        harga = kursIndonesia.format(hrg);
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        kursKsng.setDecimalFormatSymbols(formatKsng);
        pa.nomor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pa.stubList.setEnabled(false);
                if (pa.nomor.length()<4) {
                    nom=0;
                    pa.provider.setText("");
                    Log.d("nominal ",String.valueOf(nom));

                    pa.provider.setVisibility(View.GONE);
                } else {
                    pa.number = pa.nomor.getText().toString().substring(0, 4);
                    if (pa.number.equals("0857") || pa.number.equals("0856") || pa.number.equals("0858") || pa.number.equals("0815")
                            || pa.number.equals("0816")) {
                        if (position==0) {nom=5000;}
                        else if (position==1) {nom=10000;}
                        else if (position==2) {nom=20000;}
                        else if (position==3) {nom=25000;}
                        else if (position==4) {nom=50000;}
                        else if (position==5) {nom=100000;}
                    } else if (pa.number.equals("0859") || pa.number.equals("0878") || pa.number.equals("0819") || pa.number.equals("0877")) {
                        if (position==0) {nom=5000;}
                        else if (position==1) {nom=10000;}
                        else if (position==2) {nom=15000;}
                        else if (position==3) {nom=25000;}
                        else if (position==4) {nom=30000;}
                        else if (position==5) {nom=50000;}
                    } else if (pa.number.equals("0812") || pa.number.equals("0852") || pa.number.equals("0853") || pa.number.equals("0821")
                            || pa.number.equals("0813") || pa.number.equals("0822")) {
                        if (position==0) {nom=5000;}
                        else if (position==1) {nom=10000;}
                        else if (position==2) {nom=20000;}
                        else if (position==3) {nom=25000;}
                        else if (position==4) {nom=50000;}
                        else if (position==5) {nom=100000;}
                    } else if (pa.number.equals("0896") || pa.number.equals("0895") || pa.number.equals("0899") || pa.number.equals("0897")) {
                        if (position==0) {nom=5000;}
                        else if (position==1) {nom=10000;}
                        else if (position==2) {nom=20000;}
                        else if (position==3) {nom=25000;}
                        else if (position==4) {nom=30000;}
                        else if (position==5) {nom=50000;}
                    } else if (pa.number.equals("0838") || pa.number.equals("0831") || pa.number.equals("0832")) {
                        if (position==0) {nom=5000;}
                        else if (position==1) {nom=10000;}
                        else if (position==2) {nom=20000;}
                        else if (position==3) {nom=25000;}
                        else if (position==4) {nom=30000;}
                        else if (position==5) {nom=50000;}
                    } else if (pa.number.equals("0888")) {
                        if (position==0) {nom=5000;}
                        else if (position==1) {nom=10000;}
                        else if (position==2) {nom=20000;}
                        else if (position==3) {nom=25000;}
                        else if (position==4) {nom=50000;}
                        else if (position==5) {nom=60000;}
                    } else if (pa.number.equals("9990") || pa.number.equals("9991") || pa.number.equals("9992") || pa.number.equals("9993")
                            || pa.number.equals("9994") || pa.number.equals("9995") || pa.number.equals("9996") || pa.number.equals("9997")
                            || pa.number.equals("9998") || pa.number.equals("9999")) {
                        if (position==0) {nom=25000;}
                        else if (position==1) {nom=50000;}
                        else if (position==2) {nom=100000;}
                        else if (position==3) {nom=150000;}
                        else if (position==4) {nom=200000;}
                    } else {
                        nom=0;
                        pa.provider.setVisibility(View.VISIBLE);
                        pa.layPilihNom.setVisibility(View.GONE);
                        pa.layPilihKontak.setVisibility(View.GONE);
                        pa.provider.setText("Data Tidak Ditemukan");
                    }
                    Log.d("harga ",String.valueOf(hrg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (position == 0) {
            if (provider.equals("BOLT")) {
                nom = 25000;
            } else if (provider.equals("Three")) {
                nom = 1000;
            } else if (provider.equals("")) {
                nom = 0;
            } else {
                nom = 5000;
            }
        } else if (position == 1) {
            if (provider.equals("")) {
                nom = 0;
            } else if (provider.equals("BOLT")) {
                nom = 50000;
            } else if (provider.equals("Three")) {
                nom = 2000;
            } else {
                nom = 10000;
            }
        } else if (position == 2) {
            if (provider.equals("")) {
                nom = 0;
            } else if (provider == "Indosat" || provider == "Smartfren" || provider == "Telkomsel") {
                nom = 20000;
            } else if (provider == "Three") {
                nom = 3000;
            } else if (provider == "Axis" || provider == "XL") {
                nom = 15000;
            } else {
                nom = 100000;
            }
        } else if (position == 3) {
            isiNomor();
            if (provider.equals("")) {
                nom = 0;
            } else if (provider == "Indosat" || provider == "Smartfren" || provider == "Telkomsel" || provider == "Axis" || provider == "XL") {
                nom = 25000;
            } else if (provider == "Three") {
                nom = 4000;
            } else {
                nom = 25000;
            }
        } else if (position == 4) {
            isiNomor();
            if (provider.equals("")) {
                nom = 0;
            } else if (provider == "Indosat" || provider == "Smartfren" || provider == "Telkomsel" || provider == "Axis" || provider == "XL") {
                nom = 25000;
            } else if (provider == "Three") {
                nom = 4000;
            } else {
                nom = 25000;
            }
        } else if (position == 5) {
            isiNomor();
            if (provider.equals("")) {
                nom = 0;
            } else if (provider == "Indosat" || provider == "Smartfren" || provider == "Telkomsel" || provider == "Axis" || provider == "XL") {
                nom = 25000;
            } else if (provider == "Three") {
                nom = 4000;
            } else {
                nom = 25000;
            }
        } else if (position == 6) {
            isiNomor();
            if (provider.equals("")) {
                nom = 0;
            } else if (provider == "Indosat" || provider == "Smartfren" || provider == "Telkomsel" || provider == "Axis" || provider == "XL") {
                nom = 25000;
            } else if (provider == "Three") {
                nom = 4000;
            } else {
                nom = 25000;
            }
        } else if (position == 7) {
            isiNomor();
            if (provider.equals("")) {
                nom = 0;
            } else if (provider == "Indosat" || provider == "Smartfren" || provider == "Telkomsel" || provider == "Axis" || provider == "XL") {
                nom = 25000;
            } else if (provider == "Three") {
                nom = 4000;
            } else {
                nom = 25000;
            }
        }
        hrg = nom + 2000;
        nominal = kursKsng.format(nom);
        harga = kursIndonesia.format(hrg);
        tNominal.setText(nominal);
        tHargaNom.setText(harga);

        Log.d("hargass ",String.valueOf(hrg));
        return v;
    }

    public void isiNomor() {

    }
}
