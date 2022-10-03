package com.example.myapplication;
import android.widget.Filter;
import android.content.Context;
import android.widget.Filterable;
import java.util.List;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
public class AdapterForSearch extends ArrayAdapter<String> implements Filterable {
    private final List<String> itemList;
    public AdapterForSearch(Context context, int resource) {
        super(context, resource);
        itemList = new ArrayList<>();
    }
    @Override
    public int getCount() { return itemList.size(); }
    @Override
    public Filter getFilter() { return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = itemList;
                    filterResults.count = itemList.size();
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) { notifyDataSetChanged(); }
                else { notifyDataSetInvalidated();}
            }};}
    @Override
    public String getItem(int position) { return itemList.get(position); }
    public void setDataAndNotify(List<String> list) {
        itemList.clear();
        itemList.addAll(list);
        notifyDataSetChanged();
    }
    public void clearDataAndNotify() {
        itemList.clear();
        notifyDataSetChanged();
    }
}
