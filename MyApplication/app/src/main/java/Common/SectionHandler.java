package Common;
import Portfolio.PortfolioItem;
import Portfolio.PortfolioSection;
import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import Favorites.FavoritesSection;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import com.example.myapplication.R;
import Favorites.FavoritesItem;

public class SectionHandler{
    private final Context context;
    private final SectionedRecyclerViewAdapter adapter;
    private final PortfolioSection section_portfolio;
    private final FavoritesSection section_favorites;
    private final SectionAdapter adapter_portfolio;
    private final SectionAdapter adapter_favorites;

    public SectionHandler(Activity activity, Context context) {
        this.context = context;
        adapter = new SectionedRecyclerViewAdapter();
        section_portfolio = new PortfolioSection(context);
        section_favorites = new FavoritesSection(context);
        adapter.addSection(section_portfolio);
        adapter.addSection(section_favorites);
        adapter_portfolio = adapter.getAdapterForSection(section_portfolio);
        adapter_favorites = adapter.getAdapterForSection(section_favorites);
        initializeRecyclerView(activity);
    }

    private void initializeRecyclerView(Activity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerViewDisplay);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void initializeAllSections() {
        section_portfolio.setBalance(StorageForAndroid.getBalance());
        section_portfolio.setItems(StorageForAndroid.getPortfolio());
        section_favorites.setItems(StorageForAndroid.getFavorites());
        adapter.notifyDataSetChanged();
    }

    private void updatePortfolio(Map<String, Double> lastPrices) {
        System.out.println("inside updatePortfolio");
        section_portfolio.updateItems(lastPrices);
        adapter_portfolio.notifyAllItemsChanged();
        adapter_portfolio.notifyHeaderChanged();
    }

    public void updateAllSections(Map<String, Double> lastPrices,Map<String, Double> changeSinceLastClose,Map<String, Double> percentChangeSinceLastClose) {
        System.out.println("inside updatesections");
        updatePortfolio(lastPrices);
        Map<String, Integer> stocks = section_portfolio.getPortfolioStocks().stream().collect(
                Collectors.toMap(PortfolioItem::getTicker, PortfolioItem::getStocks));
        System.out.println("stocks are ********************");
        System.out.println("stocks are *******************00*");
        System.out.println(stocks);
        updateFavorites(lastPrices, stocks,changeSinceLastClose,percentChangeSinceLastClose);
    }

    private Set<String> getPortfolioTickers() {
        return section_portfolio.getPortfolioStocks().stream().map(PortfolioItem::getTicker).collect(Collectors.toSet());
    }

    private void updateFavorites(Map<String, Double> lastPrices, Map<String, Integer> stocks,Map<String, Double> changeSinceLastClose,Map<String, Double> percentChangeSinceLastClose) {
        System.out.println("inside update favorites");
        System.out.println(stocks);
        System.out.println("inside update faorites");
        section_favorites.updateFavoriteStocks(lastPrices, stocks,changeSinceLastClose,percentChangeSinceLastClose);
        adapter_favorites.notifyAllItemsChanged();
    }

    private Set<String> getFavoritesTickers() {
        return section_favorites.getItems().stream().map(FavoritesItem::getTicker).collect(Collectors.toSet());
    }

    public List<String> getAllTickers() {
        Set<String> tickers = getPortfolioTickers();
        tickers.addAll(getFavoritesTickers());
        return new ArrayList<>(tickers);
    }
}
