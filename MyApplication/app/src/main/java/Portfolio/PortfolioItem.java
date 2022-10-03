package Portfolio;
import com.example.myapplication.R;
public class PortfolioItem {
    private String ticker;
    private int stocks;
    private double price;
    private transient Double lastPrice;
    private double totalPrice;
    public static PortfolioItem newItem(String ticker, int stocks, double price) {
        PortfolioItem item = new PortfolioItem();
        item.setTicker(ticker);
        item.setStocks(stocks);
        item.setPrice(price);
        item.setTotalPrice(stocks*price);
        return item;
    }
    private void setTicker(String ticker) {
        this.ticker = ticker;
    }
    private void setStocks(int stocks) {
        this.stocks = stocks;
    }
    private void setPrice(double price) {
        this.price = price;
    }
    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }
    public boolean hasLastPrice() {
        return this.lastPrice != null;
    }
    public String getTicker() {
        return ticker;
    }
    public String getDescription() {
        String sharesString = stocks < 2 ? "share" : "shares";
        return stocks+" "+sharesString;
    }

    public int getStocks() {
        return stocks;
    }
    public Double getPrice() {
        return price;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Double getLastPrice() {
        return stocks*lastPrice;
    }
    public Double getChange() {
        if (!hasLastPrice()) {
            return null;
        }
        if(lastPrice-price !=0 )
            return Math.round(stocks*(lastPrice - price)*100.00)/100.00;

        return 0.00;
    }
    public Boolean hasPriceChanged() {
        if (!hasLastPrice()) {
            return false;
        }
        return Math.abs(lastPrice - price) >= 0.01;
    }
    public Double getWorth() {
        if (!hasLastPrice()) {
            return null;
        }
        return stocks * lastPrice;
    }
    public Integer getTrending() {
        if (!hasPriceChanged()) {
            return null;
        }
        return getChange() < 0 ? R.mipmap.trendingdownred : R.mipmap.greentrendingup;
    }
    public Double getChangePercent() {
        if (!hasLastPrice()) { return null; }
        if(lastPrice-price !=0 ) {
            Double d = (getChange() / (stocks * price)) * 100;
            return Math.round(d*100.00)/100.00;
        }
        return 0.00;
    }
    public int getChangeColor() {
        if (!hasPriceChanged()) {
            return android.R.color.darker_gray;
        }
        return getChange() < 0 ? R.color.red : R.color.green;
    }
    public void buy(int stocks, double price) {
        int totalStocks = this.stocks + stocks;
        double totalPrice = ((this.stocks * this.price) + (stocks * price)) / totalStocks;
        System.out.println("inside buy ......");
        this.totalPrice = (this.stocks * this.price) + (stocks * price);
        this.stocks = totalStocks;
        this.price = totalPrice;
    }
    public boolean sell(int stocks) {
        this.stocks -= stocks;
        System.out.println("inside sell ......");
        System.out.println("inside sell ....");
        System.out.println(this.totalPrice);
        this.totalPrice = this.totalPrice - (stocks*this.price);
        System.out.println(this.totalPrice);
        this.price = (this.totalPrice)/ (this.stocks);
        return this.stocks == 0;
    }
}