package Details.Portfolio;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.widget.Button;
import android.view.WindowManager;
import android.view.Window;
import com.example.myapplication.R;

public class TradeSuccessDialog {
    private final String stockticker;
    private final Dialog trade_dialog;
    private final TextView trade_message;
    public TradeSuccessDialog(Context context, String ticker) {
        trade_dialog = new Dialog(context);
        trade_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        trade_dialog.setContentView(R.layout.portfolio_trade_complete);
        trade_message = trade_dialog.findViewById(R.id.trade_complete_message);
        Button button = trade_dialog.findViewById(R.id.done_buttonOfTrade);
        button.setOnClickListener(v -> trade_dialog.dismiss());
        Window window = trade_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        this.stockticker = ticker;
    }
    public void show(PortfolioController.TradeType typeOfTrade, int stocks) {
        String trade = typeOfTrade.equals(PortfolioController.TradeType.BUY) ? "bought" : "sold";
        String stocks_str = String.valueOf(stocks);
        String shares_str = stocks < 2 ? "share" : "shares";
        trade_message.setText(String.format(
                "You have successfully %s %s %s of %s", trade, stocks_str, shares_str, stockticker));
        trade_dialog.show();
    }
}