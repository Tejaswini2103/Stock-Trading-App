package Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import Favorites.FavoritesItem;
import Portfolio.PortfolioItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StorageForAndroid {
    private static boolean initialized;

    private static SharedPreferences preferences;

    private static final String PREFERENCES_FILE_NAME = "shared_pref";

    private static final String BALANCE_KEY = "balance";
    private static final double DEFAULT_BALANCE = 25000;

    private static final String FAVORITES_KEY = "favorites";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static final String PORTFOLIO_KEY = "portfolio";
    private static final Type favoritesItemsType = new TypeToken<List<FavoritesItem>>() {
    }.getType();

    private static final Type portfolioItemsType = new TypeToken<List<PortfolioItem>>() {
    }.getType();

    synchronized public static void initialize(Context context) {
        if (!initialized) {
            initialized = true;
            preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            initializeBalance();
            initializePortfolio();
            initializeFavorites();
            System.out.println("inside initialize preferences");
            System.out.println(preferences.getString("balance",null));
        }
    }

    @SuppressLint("ApplySharedPref")
    private static void updatePreference(String key, String json) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, json);
        editor.commit();
    }

    public static double getBalance() {
        if (preferences.contains(BALANCE_KEY)) {
            return gson.fromJson(preferences.getString(BALANCE_KEY, null), Double.class);
        }
        return DEFAULT_BALANCE;
    }

    synchronized public static void updateBalance(double balance) {
        String json = gson.toJson(balance, Double.class);
        updatePreference(BALANCE_KEY, json);
    }

    private static void initializeBalance() {
        double balance = getBalance();
        updateBalance(balance);
    }

    private static void initializeFavorites() {
        List<FavoritesItem> favoritesItems = getFavorites();
        updateFavorites(favoritesItems);
    }

    public static List<FavoritesItem> getFavorites() {
        if (preferences.contains(FAVORITES_KEY)) {
            return gson.fromJson(preferences.getString(FAVORITES_KEY, null), favoritesItemsType);
        }
        return Collections.emptyList();
    }

    synchronized public static void addToFavorites(FavoritesItem item) {
        List<FavoritesItem> items = getFavorites();
        items.add(item);
        updateFavorites(items);
    }

    public static boolean isFavorite(String ticker) {
        List<FavoritesItem> items = getFavorites();
        return items.stream().anyMatch(favoritesItem -> favoritesItem.getTicker().equals(ticker));
    }

    synchronized public static void removeFromFavorites(String ticker) {
        List<FavoritesItem> items = getFavorites()
                .stream()
                .filter(item -> !item.getTicker().equals(ticker))
                .collect(Collectors.toList());
        updateFavorites(items);
    }

    public static void updateFavorites(List<FavoritesItem> items) {
        String json = gson.toJson(items, favoritesItemsType);
        updatePreference(FAVORITES_KEY, json);
    }

    private static void initializePortfolio() {
        List<PortfolioItem> portfolioItems = getPortfolio();
        updatePortfolio(portfolioItems);
    }

    public static boolean isPresentInPortfolio(String ticker) {
        List<PortfolioItem> items = getPortfolio();
        return items.stream().anyMatch(portfolioItem -> portfolioItem.getTicker().equals(ticker));
    }

    public static PortfolioItem getPortfolioItem(String ticker) {
        List<PortfolioItem> items = getPortfolio();
        return items.stream().filter(item -> item.getTicker().equals(ticker)).findAny().orElse(null);
    }

    public static List<PortfolioItem> getPortfolio() {
        if (preferences.contains(PORTFOLIO_KEY)) {
            return gson.fromJson(preferences.getString(PORTFOLIO_KEY, null), portfolioItemsType);
        }
        return Collections.emptyList();
    }

    synchronized public static void addToPortfolio(PortfolioItem item) {
        List<PortfolioItem> items = getPortfolio();
        if(!items.contains(item)) {
            items.add(item);
            System.out.println("******item********");
            updatePortfolio(items);
        }
    }

    public static void updatePortfolio(List<PortfolioItem> items) {
        String json = gson.toJson(items, portfolioItemsType);
        updatePreference(PORTFOLIO_KEY, json);
    }

}

