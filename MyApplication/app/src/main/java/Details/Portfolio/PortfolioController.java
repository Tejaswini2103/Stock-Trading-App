package Details.Portfolio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import Details.Common.Formatter;
import Common.StorageForAndroid;
import Common.ToastManager;
import Details.Common.Info;
import Portfolio.PortfolioItem;

import java.util.List;

public class PortfolioController implements DialogForTrade.OnActionHandler {
    public enum TradeType {
        BUY, SELL
    }
    private final TextView stocks_portfolioDetails;
    private final TextView avgCost_portfolioDetails;
    private final TextView totalCost_portfolioDetails;
    private final TextView change_portfolioDetails;
    private final TextView marketPrice_portfolioDetails;
    private final DialogForTrade tradeDialog;
    private final TradeSuccessDialog tradeSuccessDialog;
    private final ToastManager toastManager;
    private final Info allinfo;

    public PortfolioController(Activity activity, ToastManager toast, Info allinfo) {
        stocks_portfolioDetails = activity.findViewById(R.id.stocksOwned_Portfoliodetailsview);
        marketPrice_portfolioDetails = activity.findViewById(R.id.marketValue_Portfoliodetailsview);
        avgCost_portfolioDetails = activity.findViewById(R.id.avg_cost_per_share);
        totalCost_portfolioDetails = activity.findViewById(R.id.total_cost_per_share);
        change_portfolioDetails = activity.findViewById(R.id.change_value);
        tradeDialog = new DialogForTrade(activity, allinfo, this);
        Button tradeButton = activity.findViewById(R.id.tradeButton_detailsview);
        tradeButton.setOnClickListener(v -> tradeDialog.showDialog());
        tradeSuccessDialog = new TradeSuccessDialog(activity, allinfo.getTicker());
        this.toastManager = toast;
        this.allinfo = allinfo;
    }

    @SuppressLint("SetTextI18n")
    public void show() {
        String stocksText = null;
        String marketPriceText = null;
        String avgCostText = null;
        String chnageText = null;
        String tcText = null;
        String ticker = allinfo.getTicker();

        if (StorageForAndroid.isPresentInPortfolio(ticker)) {
            PortfolioItem item = StorageForAndroid.getPortfolioItem(ticker);
            stocksText = String.valueOf(item.getStocks());
            item.setLastPrice(allinfo.getLastPrice());
            marketPriceText = String.format(Formatter.getPriceString(item.getWorth()));
            avgCostText = String.format(Formatter.getPriceString(item.getPrice()));
            chnageText = String.format(Formatter.getPriceString(item.getChange()));
            tcText = String.format(Formatter.getPriceString(item.getTotalPrice()));
            stocks_portfolioDetails.setText(stocksText);
            marketPrice_portfolioDetails.setText("$"+marketPriceText);
            avgCost_portfolioDetails.setText("$"+avgCostText);
            totalCost_portfolioDetails.setText("$"+tcText);
            change_portfolioDetails.setText("$"+chnageText);
            System.out.println("************************");
            System.out.println(item.getStocks());
            System.out.println("*******************");
            if(item.getChange()>0) {
                change_portfolioDetails.setTextColor(Color.rgb(0, 128, 0));
                marketPrice_portfolioDetails.setTextColor(Color.rgb(0, 128, 0));
            }
            else if(item.getChange()<0) {
                change_portfolioDetails.setTextColor(Color.RED);
                marketPrice_portfolioDetails.setTextColor(Color.RED);
            }
            if(item.getChange()==0) {
                change_portfolioDetails.setTextColor(Color.BLACK);
                marketPrice_portfolioDetails.setTextColor(Color.BLACK);
            }

        } else {
            stocksText = "0";
            marketPriceText = "$0.00";
            avgCostText = "$0.00";
            tcText = "$0.00";
            chnageText = "$0.00";
            stocks_portfolioDetails.setText(stocksText);
            marketPrice_portfolioDetails.setText(marketPriceText);
            avgCost_portfolioDetails.setText(avgCostText);
            totalCost_portfolioDetails.setText(tcText);
            change_portfolioDetails.setText(chnageText);
            change_portfolioDetails.setTextColor(Color.BLACK);
            marketPrice_portfolioDetails.setTextColor(Color.BLACK);
        }

    }

    @Override
    public void onStockBuy(Integer stocks) {
        if (stocks == null) {
            toastManager.showToast("Please enter valid amount");
        } else if (stocks == 0) {
            toastManager.showToast("Cannot buy less than 0 shares");
        } else {
            double balance = StorageForAndroid.getBalance();
            double stocksPrice = stocks*allinfo.getLastPrice();
            if (stocksPrice > balance) {
                toastManager.showToast("Not enough money to buy");
            } else {
                buyStocks(stocks);
                tradeDialog.dismiss();
                System.out.println("agian");
                show();
                tradeSuccessDialog.show(TradeType.BUY, stocks);
            }
        }
    }

    private void buyStocks(Integer stocks) {
        String ticker = allinfo.getTicker();
        double boughtPrice = allinfo.getLastPrice();

        PortfolioItem item;
        if (StorageForAndroid.isPresentInPortfolio(ticker)) {
            List<PortfolioItem> items = StorageForAndroid.getPortfolio();

            item = items.stream().filter(pitem -> pitem.getTicker().equals(ticker)).findAny().orElse(null);
            item.buy(stocks, boughtPrice);
            StorageForAndroid.updatePortfolio(items);
        } else {
            item = PortfolioItem.newItem(ticker, stocks, boughtPrice);
            StorageForAndroid.addToPortfolio(item);
        }

        System.out.println("after buy**************************");
        System.out.println(StorageForAndroid.getBalance());
        StorageForAndroid.updateBalance(StorageForAndroid.getBalance() - stocks*boughtPrice);
        System.out.println("after buy**************************");
        System.out.println(StorageForAndroid.getBalance());
    }

    @Override
    public void onStockSell(Integer stocks) {
        if (stocks == null) {
            toastManager.showToast("Please enter valid amount");
        } else if (stocks == 0) {
            toastManager.showToast("Cannot sell less than 0 shares");
        } else {
            String ticker = allinfo.getTicker();
            int portfolioStocks;
            if (StorageForAndroid.isPresentInPortfolio(ticker)) {
                PortfolioItem item = StorageForAndroid.getPortfolioItem(ticker);
                portfolioStocks = item.getStocks();
            } else {
                portfolioStocks = 0;
            }
            if (stocks > portfolioStocks) {
                toastManager.showToast("Not enough shares to sell");
            } else {
                double lastPrice = allinfo.getLastPrice();

                List<PortfolioItem> items = StorageForAndroid.getPortfolio();
                PortfolioItem item = items.stream().filter(pitem -> pitem.getTicker().equals(ticker)).findAny().orElse(null);
                if (item.sell(stocks)) {
                    items.remove(item);
                }
                StorageForAndroid.updatePortfolio(items);
                StorageForAndroid.updateBalance(StorageForAndroid.getBalance() + stocks*lastPrice);
                tradeDialog.dismiss();
                System.out.println("agian");
                show();
                tradeSuccessDialog.show(TradeType.SELL, stocks);
            }
        }
    }

}
