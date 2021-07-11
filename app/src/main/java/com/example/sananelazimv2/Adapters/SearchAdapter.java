package com.example.sananelazimv2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sananelazimv2.Model.TumIlanlarModel;
import com.example.sananelazimv2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter implements Filterable {

    private List<TumIlanlarModel> tumIlanlarModels;
    private List<TumIlanlarModel> exListFull;

    Context context;

    public SearchAdapter(List<TumIlanlarModel> tumIlanlarModels, Context context) {
        this.tumIlanlarModels = tumIlanlarModels;
        this.context = context;
        exListFull = new ArrayList<>(tumIlanlarModels);
    }

    @Override
    public int getCount() {
        return tumIlanlarModels.size();
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

        lblBaslik.setText((String)tumIlanlarModels.get(position).getTitle());
        lblAciklama.setText((String)tumIlanlarModels.get(position).getDescription());
        lblFiyat.setText((String)tumIlanlarModels.get(position).getPrice());

        Picasso.with(context).load("http://192.168.1.6:80/snldb_files/" + tumIlanlarModels.get(position).getImage()).resize(1050,600).into(imageView);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TumIlanlarModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TumIlanlarModel item : exListFull) {
                    if (item.getTitle().toString().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tumIlanlarModels.clear();
            tumIlanlarModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
