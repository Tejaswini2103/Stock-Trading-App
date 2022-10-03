package Favorites;
import com.example.myapplication.R;

public class FavoritesItem {

    private String ticker;
    private String name;
    private String colorOfText;
    public String getColorOfText() {
        return colorOfText;
    }
    public void setColorOfText(String colorOfText) {
        this.colorOfText = colorOfText;
    }
    private String allChange;
    public String getAllChange() {
        return allChange;
    }
    private double price;
    private transient Integer stocks;
    private transient Double lastPrice;
    private void setTicker(String ticker) { this.ticker = ticker; }
    private void setName(String name) { this.name = name; }
    private void setPrice(double price) { this.price = price; }
    public void setStocks(Integer stocks) { this.stocks = stocks; }
    public void setLastPrice(double lastPrice) { this.lastPrice = lastPrice; }
    public boolean hasLastPrice() { return this.lastPrice != null; }
    public String getTicker() { return ticker; }
    public String getName() { return name; }
    public Double getLastPrice() { return lastPrice; }
    public void setAllChange(String allChange) { this.allChange = allChange; }

    private FavoritesItem() { }

    public static FavoritesItem newItem(String ticker, String name, double price) {
        FavoritesItem item = new FavoritesItem();
        item.setPrice(price);
        item.setTicker(ticker);
        item.setName(name);
        return item;
    }

    public Integer getTrendingDrawable() {
        if(getColorOfText()=="RED") {
            return R.mipmap.trendingdownred;
        }
        if(getColorOfText()=="GREEN") {
            return R.mipmap.greentrendingup;
        }
        else {
            return null;
        }
    }

}

