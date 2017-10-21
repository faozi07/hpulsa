package android.hpulsa.com.hpulsanew.adapter;

import android.content.Context;
import android.hpulsa.com.hpulsanew.R;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.hpulsa.com.hpulsanew.model.modeKetMenu;
import com.squareup.picasso.Picasso;

import java.util.List;

public class gridViewAdapter extends ArrayAdapter<modeKetMenu> {
    private Context _context;
    public static ImageView imgMenu;
    public gridViewAdapter(Context context, int resource, List<modeKetMenu> objects) {
        super(context, resource, objects);
        _context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item,null);
        }
        modeKetMenu mE = getItem(position);
        TextView tKetMenu = (TextView) v.findViewById(R.id.tket);
        imgMenu = (ImageView) v.findViewById(R.id.imgMenuUtama);
        if (position==0) {
            Picasso.with(_context)
                    .load(R.drawable.ic_pulsa)
                    .into(imgMenu);
        } else if (position==1) {
            Picasso.with(_context)
                    .load(R.drawable.ic_paketinternet)
                    .into(imgMenu);
        } else if (position==2) {
            Picasso.with(_context)
                    .load(R.drawable.ic_pln)
                    .into(imgMenu);
        } else if (position==3) {
            Picasso.with(_context)
                    .load(R.drawable.ic_game)
                    .into(imgMenu);
        } else if (position==4) {
            Picasso.with(_context)
                    .load(R.drawable.ic_paketsms)
                    .into(imgMenu);
        } else if (position==5) {
            Picasso.with(_context)
                    .load(R.drawable.ic_bbm)
                    .into(imgMenu);
        } else if (position==6) {
            Picasso.with(_context)
                    .load(R.drawable.ic_gojek)
                    .into(imgMenu);
        } else if (position==7) {
            Picasso.with(_context)
                    .load(R.drawable.ic_tagihan)
                    .into(imgMenu);
        }
        tKetMenu.setText(mE.getKetMenu());

        return v;
    }
}
