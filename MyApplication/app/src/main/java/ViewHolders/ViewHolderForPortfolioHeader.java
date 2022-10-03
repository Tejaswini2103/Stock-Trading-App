package ViewHolders;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

public class ViewHolderForPortfolioHeader extends RecyclerView.ViewHolder {
    final public TextView netWorthView;
    final public TextView cashBalanceView;
    public ViewHolderForPortfolioHeader(View view) {
        super(view);
        netWorthView = (TextView) view.findViewById(R.id.netWorth_value);
        cashBalanceView = (TextView) view.findViewById(R.id.cash_balance_value);
    }
}