package Details.Portfolio;

import android.annotation.SuppressLint;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.app.Dialog;
import android.view.Window;
import android.text.TextWatcher;
import android.widget.Button;
import android.view.WindowManager;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import com.example.myapplication.R;
import java.text.DecimalFormat;
import Common.StorageForAndroid;
import Details.Common.Info;

public class DialogForTrade {
    private Integer stocks;
    private final EditText enterStocks;
    private final TextView stocksPriceOnDialog;
    private final Dialog dialog;
    private final OnActionHandler actionHandler;
    private final Info info;
    private static final DecimalFormat formatter = new DecimalFormat("'$'#0.00;-'$'#0.00");

    interface OnActionHandler {
        void onStockSell(Integer stocks);
        void onStockBuy(Integer stocks);
    }

    public DialogForTrade(Context context, Info info, OnActionHandler actionHandler) {
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.portfolio_trade_dialog);
        enterStocks = dialog.findViewById(R.id.enterNumberOfStocks);
        stocksPriceOnDialog = dialog.findViewById(R.id.stocks_value_tradeDialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        this.info = info;
        this.actionHandler = actionHandler;
        initializeDialogText();
    }

    public void showDialog() {
        stocks = null;
        enterStocks.setText("");
        initializeAmount();
        dialog.show();
    }

    private void initializeDialogText() {
        TextView titleView = dialog.findViewById(R.id.title_tradeDialog);
        titleView.setText(String.format("Trade %s shares", info.getDescription()));
        enterStocks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    stocks = null;
                } else {
                    try {
                        stocks = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        stocks = null;
                    }
                }
                getstockPrices();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String last_price = formatter.format(info.getLastPrice());
        int display_Stocks = stocks == null ? 0 : stocks;
        String stocksPriceString = formatter.format(display_Stocks * info.getLastPrice());
        stocksPriceOnDialog.setText(String.format("%d x %s/share = %s", display_Stocks, last_price, stocksPriceString));
        Button buy = dialog.findViewById(R.id.buyButton_tradeDialog);
        buy.setOnClickListener(v -> this.actionHandler.onStockBuy(stocks));
        Button sellButton = dialog.findViewById(R.id.sellButton_tradeDialog);
        sellButton.setOnClickListener(v -> this.actionHandler.onStockSell(stocks));
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void initializeAmount() {
        TextView availableAmnt = dialog.findViewById(R.id.cashBalance_tradeDialog);
        String balance = formatter.format(StorageForAndroid.getBalance());
        availableAmnt.setText(String.format("%s available to buy %s", balance, info.getTicker()));
    }

    @SuppressLint("DefaultLocale")
    private void getstockPrices() {
        String last_Price = formatter.format(info.getLastPrice());
        int display_Stocks = stocks == null ? 0 : stocks;
        String stocksPrice = formatter.format(display_Stocks * info.getLastPrice());
        stocksPriceOnDialog.setText(String.format("%d x %s/share = %s", display_Stocks, last_Price, stocksPrice));
    }

}

