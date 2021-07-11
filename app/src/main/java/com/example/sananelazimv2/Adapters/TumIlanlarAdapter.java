package com.example.sananelazimv2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.sananelazimv2.Model.TumIlanlarModel;
import com.example.sananelazimv2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TumIlanlarAdapter extends BaseAdapter {
    //İlanlarımAdepterdeki Gibi Listeleme İşlemi Yaptık.
    List<TumIlanlarModel> tumIlanlarModels;
    Context context;

    public TumIlanlarAdapter(List<TumIlanlarModel> tumIlanlarModels, Context context) {
        this.tumIlanlarModels = tumIlanlarModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tumIlanlarModels.size();//modelimizin list şeklinde sizenı döndürdük.
    }

    @Override
    public Object getItem(int position) {
        return tumIlanlarModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.tumilanlar_layout,parent,false);

        TextView lblBaslik = (TextView)convertView.findViewById(R.id.lblIlanBaslikTum);
        TextView lblAciklama = (TextView)convertView.findViewById(R.id.lblIlanAciklamaTum);
        TextView lblFiyat = (TextView)convertView.findViewById(R.id.lblIlanFiyatTum);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imgIlanlarimIlanResimTum);

        String tl = " TL";

        lblBaslik.setText((String)tumIlanlarModels.get(position).getTitle());
        lblAciklama.setText((String)tumIlanlarModels.get(position).getDescription());
        lblFiyat.setText((String)tumIlanlarModels.get(position).getPrice()+tl);

        Picasso.with(context).load("http://192.168.1.6:80/snldb_files/" + tumIlanlarModels.get(position).getImage()).resize(1050,600).into(imageView);
        return convertView;
    }
}
