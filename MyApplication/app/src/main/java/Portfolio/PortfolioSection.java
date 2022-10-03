package Portfolio;

import ViewHolders.ViewHolderForPortfolioHeader;
import ViewHolders.ViewHolderForPortfolioItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.AllStockDetailsActivity;
import com.example.myapplication.R;
import Details.Common.Formatter;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;


public class PortfolioSection extends Section {
    private final Context context;
    private Double balance;
    private List<PortfolioItem> portfolioStocks;

    public PortfolioSection(Context context) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.items_section_portfolio)
                .headerResourceId(R.layout.headersection_portfolio)
                .build());
        this.context = context;
        this.balance = null;
        this.portfolioStocks = new ArrayList<>();
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setItems(List<PortfolioItem> items) {
        this.portfolioStocks = items;
    }
    public void updateItems(Map<String, Double> lastPrices) {
        System.out.println(lastPrices);
        portfolioStocks.forEach(item -> item.setLastPrice(lastPrices.get(item.getTicker())));
        System.out.println(portfolioStocks);
    }
    public List<PortfolioItem> getPortfolioStocks() {
        return portfolioStocks;
    }
    @Override
    public int getContentItemsTotal() {
        return portfolioStocks.size();
    }
    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ViewHolderForPortfolioItem(view);
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new ViewHolderForPortfolioHeader(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println("inside onBind");
        ViewHolderForPortfolioItem viewHolder = (ViewHolderForPortfolioItem) holder;
        PortfolioItem item = portfolioStocks.get(position);
        viewHolder.ticker_portfolio.setText(item.getTicker());
        viewHolder.description_portfolio.setText(item.getDescription());
        if (item.hasLastPrice()) {
            viewHolder.lastPrice_portfolio.setText(Formatter.getPriceString(item.getLastPrice()));
            viewHolder.change_portfolio.setText("$ "+Formatter.getPriceString(item.getChange())+" ( "+Formatter.getPriceString(item.getChangePercent())+" % )");
            viewHolder.change_portfolio.setTextColor(context.getColor(item.getChangeColor()));
            if (item.hasPriceChanged()) {
                viewHolder.trending_portfolio.setImageResource(item.getTrending());
                viewHolder.trending_portfolio.setVisibility(View.VISIBLE);
            } else {
                viewHolder.trending_portfolio.setVisibility(View.INVISIBLE);
            }
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
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        System.out.println("net worth is *****************");
        System.out.println("net worth is **************");
        System.out.println("net worth is *********JKHdshs******");
        System.out.println("net worth is *********dsjhjshd********");
        ViewHolderForPortfolioHeader viewHolder = (ViewHolderForPortfolioHeader) holder;
        Double netWorth = getNetWorth();
        if (netWorth != null) {
            viewHolder.netWorthView.setText(Formatter.getPriceString(netWorth));
            viewHolder.cashBalanceView.setText(Formatter.getPriceString(balance));
        }
    }

    private Double getNetWorth() {
        if (balance == null) {
            return null;
        }
        double netWorth = balance;
        System.out.println("net worth is *****************");
        System.out.println(netWorth);
        for (PortfolioItem item: portfolioStocks) {
            if (!item.hasLastPrice()) {
                return null;
            }
            System.out.println("inside portfolio item *****************");
            System.out.println(item.getWorth());
            System.out.println(item.getTicker());
            System.out.println("netWorth is "+ netWorth);
            System.out.println("item.getWorth() is " +item.getWorth());
            System.out.println(netWorth+item.getWorth());
            netWorth+=item.getWorth();
            System.out.println(netWorth);
        }
        return netWorth;
    }
}