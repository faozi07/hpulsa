package android.hpulsa.com.hpulsanew.adapter;

import android.content.Context;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.model.modRiwayat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ozi on 08/10/2017.
 */

public class listRiwayatAdapter extends BaseAdapter {

    private Context context;
    private List<modRiwayat> listRiwayat;

    public listRiwayatAdapter(Context context, List<modRiwayat> listRiwayat) {
        this.context = context;
        this.listRiwayat = listRiwayat;
    }

    @Override
    public int getCount() {
        return listRiwayat.size();
    }

    @Override
    public Object getItem(int position) {
        return listRiwayat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.item_list_riwayat, null);
        TextView tNoTrx = (TextView) v.findViewById(R.id.tNoTransaksi);
        TextView tSttsTrx = (TextView) v.findViewById(R.id.tSttsTrx);
        TextView tNoHp = (TextView) v.findViewById(R.id.tNoHp);
        TextView tNominal = (TextView) v.findViewById(R.id.tNominal);
        TextView tTglTrx = (TextView) v.findViewById(R.id.tTglTransaksi);
        TextView tHarga = (TextView) v.findViewById(R.id.tHarga);
        final LinearLayout layoutList = (LinearLayout) v.findViewById(R.id.layItemListView);

        modRiwayat item = listRiwayat.get(position);
        tNoTrx.setText(item.getNoTrx());
        tSttsTrx.setText(item.getStatusTrx());
        tNoHp.setText(item.getNoHp());
        tNominal.setText(item.getNominal());
        tTglTrx.setText(item.getTglTrx());
        tHarga.setText(item.getHarga());

        return v;
    }
}
