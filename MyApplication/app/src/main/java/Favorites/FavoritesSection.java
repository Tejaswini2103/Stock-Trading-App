package Favorites;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import ViewHolders.ViewHolderForFavorites;
import java.util.List;
import java.util.Map;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import java.util.ArrayList;
import java.util.Collections;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder;

import com.example.myapplication.AllStockDetailsActivity;
import com.example.myapplication.R;
import Details.Common.Formatter;

public class FavoritesSection extends Section {
    private final Context context;
    private List<FavoritesItem> favoritesStocks;

    public FavoritesSection(Context context) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.items_section_favorites)
                .headerResourceId(R.layout.headersection_favorites)
                .build());
        this.favoritesStocks = new ArrayList<>();
        this.context = context;

    }

    public void updateFavoriteStocks(Map<String, Double> lastPrices, Map<String, Integer> stocks,Map<String, Double> changeSinceLastClose,Map<String, Double> percentChangeSinceLastClose) {
        favoritesStocks.forEach(item -> {
            String ticker = item.getTicker();
            item.setLastPrice(lastPrices.get(item.getTicker()));
            item.setStocks(stocks.get(ticker));
            String changeAndPercentChange = "$ "+String.format(Formatter.getPriceString(Math.round(changeSinceLastClose.get(item.getTicker())*100.00)/100.00))+" ( "+String.format(Formatter.getPriceString(Math.round(percentChangeSinceLastClose.get(item.getTicker())*100.00)/100.00))+" % )";
            item.setAllChange(changeAndPercentChange);
            if(changeSinceLastClose.get(item.getTicker())<0) {
                item.setColorOfText("RED");
            }
            if(changeSinceLastClose.get(item.getTicker())>0) {
                item.setColorOfText("GREEN");
            }
            if(changeSinceLastClose.get(item.getTicker())==0) {
                item.setColorOfText("BLACK");
            }
        });
    }

    public List<FavoritesItem> getItems() {
        return favoritesStocks;
    }
    public void setItems(List<FavoritesItem> items) {
        this.favoritesStocks = items;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println("************************");
        System.out.println("**************");
        System.out.println("**********************");
        System.out.println("inside favorites section");
        ViewHolderForFavorites viewHolder = (ViewHolderForFavorites) holder;
        FavoritesItem item = favoritesStocks.get(position);
        viewHolder.tickerinFavs.setText(item.getTicker());
        viewHolder.descriptionView.setText(item.getName());
        if (item.hasLastPrice()) {

            viewHolder.lastPriceinFavs.setText(Formatter.getPriceString(item.getLastPrice()));
            viewHolder.changeInFavs.setText(item.getAllChange());

            if(item.getColorOfText().equals("RED")) {
                viewHolder.changeInFavs.setTextColor(context.getColor(R.color.red));
            }
            if(item.getColorOfText().equals("GREEN")) {
                viewHolder.changeInFavs.setTextColor(context.getColor(R.color.green));
            }
            if(item.getColorOfText().equals("BLACK")) {
                viewHolder.changeInFavs.setTextColor(context.getColor(R.color.black));
            }

            viewHolder.trendingInFavs.setImageResource(item.getTrendingDrawable());
            viewHolder.trendingInFavs.setVisibility(View.VISIBLE);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String ticker = item.getTicker();
                Intent intent = new Intent(context, AllStockDetailsActivity.class);
                intent.putExtra("ticker", ticker);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getContentItemsTotal() {
        return favoritesStocks.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ViewHolderForFavorites(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new EmptyViewHolder(view);
    }
}
