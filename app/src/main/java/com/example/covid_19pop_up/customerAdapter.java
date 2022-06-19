package com.example.covid_19pop_up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class customerAdapter extends ArrayAdapter<CountrtModel> {
    private Context context;
    private List<CountrtModel> countrtModelsList;
    private List<CountrtModel> countrtModelsListFiltered;
    public customerAdapter( Context context, List<CountrtModel> countrtModelsList) {
        super(context,R.layout.list_country_list,countrtModelsList);

        this.context=context;
        this.countrtModelsList=countrtModelsList;
        this.countrtModelsListFiltered=countrtModelsList;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_country_list,null,true);

        TextView tvCountryName=view.findViewById(R.id.tvCountryName);
        ImageView imageView=view.findViewById(R.id.imageFlag);

        tvCountryName.setText(countrtModelsListFiltered.get(position).getCountry());
        Glide.with(context).load(countrtModelsListFiltered.get(position).getFlag()).into(imageView);
        return view;
    }

    @Override
    public int getCount() {
        return countrtModelsListFiltered.size();
    }

    @Nullable
    @Override
    public CountrtModel getItem(int position) {
        return countrtModelsListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
               FilterResults filterResults=new FilterResults();
               if (constraint==null || constraint.length()==0){
                   filterResults.count= countrtModelsList.size();
                   filterResults.values=countrtModelsList;
               }else {
                   List<CountrtModel> resultsModel = new ArrayList<>();
                   String searchStr = constraint.toString().toLowerCase();

                   for (CountrtModel itemsModel:countrtModelsList){
                       if (itemsModel.getCountry().toLowerCase().contains(searchStr)){
                           resultsModel.add(itemsModel);
                       }
                       filterResults.count=resultsModel.size();
                       filterResults.values=resultsModel;
                   }
               }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                  countrtModelsListFiltered=(List<CountrtModel>) results.values;
                  AffectedCountries.countrtModelList=(List<CountrtModel>) results.values;
                choice_your_country.countrtModelList=(List<CountrtModel>) results.values;

                notifyDataSetChanged();
            }
        };
      return filter;
    }
}











