package Details.Common;
import com.example.myapplication.R;

public class Info {
    private String ticker;
    private String name;
    private String description;
    private String industry;
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getWeburl() { return weburl; }
    public void setWeburl(String weburl) { this.weburl = weburl; }
    public String getIpostartDate() { return ipostartDate; }
    public String getName() { return name; }
    public void setIpostartDate(String ipostartDate) { this.ipostartDate = ipostartDate; }
    private String weburl;
    private String ipostartDate;
    private Double change;
    private Double highPrice;
    private Double lowPrice;
    private Double openPrice;
    private Double bidPrice;
    public void setTicker(String ticker) { this.ticker = ticker; }
    public void setDescription(String description) { this.description = description; }
    public void setLastPrice(Double lastPrice) { this.lastPrice = lastPrice; }
    public void setChange(Double change) { this.change = change; }
    public void setHighPrice(Double highPrice) { this.highPrice = highPrice; }
    public void setLowPrice(Double lowPrice) { this.lowPrice = lowPrice; }
    public void setOpenPrice(Double openPrice) { this.openPrice = openPrice; }
    public void setBidPrice(Double bidPrice) { this.bidPrice = bidPrice; }
    public Double getLastPrice() { return lastPrice; }
    public Double getChange() { return change; }
    public Double getHighPrice() { return highPrice; }
    public Double getLowPrice() { return lowPrice; }
    public Double getOpenPrice() { return openPrice; }
    public Double getBidPrice() { return bidPrice; }
    private Double lastPrice;
    public String getTicker() { return ticker; }
    public String getDescription() { return description; }
    public int getChangeColor() {
        if (Math.abs(change) < 0.01) {
            return R.color.darkgray;
        }
        return change < 0 ? R.color.red : R.color.green;
    }
}
