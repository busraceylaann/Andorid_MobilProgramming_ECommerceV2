package com.example.sananelazimv2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sananelazimv2.Model.FavoriListelemeModel;
import com.example.sananelazimv2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriIlanlarimListeAdapter extends BaseAdapter {

    List<FavoriListelemeModel> list;
    Context context;

    public FavoriIlanlarimListeAdapter(List<FavoriListelemeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.favoriilanlar_layout,parent,false); //LayoutInflaterxml düzenlerinizden birinden yeni View(veya Layout) bir nesne oluşturmak için kullanılır.

        ImageView imgFavori;
        TextView lblFavoriBaslik,lblFavoriAcıklama,lblFavoriFiyat;

        imgFavori = (ImageView)convertView.findViewById(R.id.imgFavori);
        lblFavoriBaslik = (TextView)convertView.findViewById(R.id.lblFavoriBaslik);
        lblFavoriAcıklama = (TextView)convertView.findViewById(R.id.lblFavoriAcıklama);
        lblFavoriFiyat = (TextView)convertView.findViewById(R.id.lblFavoriFiyat);

        String tl = " TL";

        lblFavoriBaslik.setText((String)list.get(position).getTitle());          //
        lblFavoriAcıklama.setText((String)list.get(position).getDescription());  // Burada get position ile aldığımız yani liste şeklinde tuttuğumuz modelimizin satırlarındaki verileri gerekli yerlere set ettik.
        lblFavoriFiyat.setText((String)list.get(position).getPrice()+tl);           //





        Picasso.with(context).load("http://192.168.1.6:80/snldb_files/" + list.get(position).getImage()).resize(1050,600).into(imgFavori); //Burada Da Gelen Resimlerin Urli ile set ettik.
        //Log.wtf("Testing valid URL", "|"+ "http://192.168.1.2:80/snldb_files/"+ilanlarimModelList.get(position).getImage()+"|");

        return convertView;
    }
}
