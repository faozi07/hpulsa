package android.hpulsa.com.hpulsanew.adapter;

import android.content.Context;
import android.hpulsa.com.hpulsanew.R;
import android.hpulsa.com.hpulsanew.model.ItemSlideMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
 * Created by ozi on 07/10/2017.
 */


public class SlideMenuAdapter extends BaseAdapter {

    private Context context;
    private List<ItemSlideMenu> listItem;

    public SlideMenuAdapter(Context context, List<ItemSlideMenu> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View v = View.inflate(context, R.layout.item_list_menu, null);
        ImageView imgMenu = (ImageView) v.findViewById(R.id.imgMenu);
        ImageView imgPembatas = (ImageView) v.findViewById(R.id.pembatas);
        TextView tTitle = (TextView) v.findViewById(R.id.title);
        TextView tDana = (TextView) v.findViewById(R.id.dana);

        ItemSlideMenu item = listItem.get(position);
        imgMenu.setImageResource(item.getImgId());
        imgPembatas.setImageResource(item.getImgPembatas());
        tTitle.setText(item.getTitle());
        tDana.setText(item.getSaldo());

        if (position == 2) {
            imgPembatas.setVisibility(View.VISIBLE);
        }

        return v;
    }
}
