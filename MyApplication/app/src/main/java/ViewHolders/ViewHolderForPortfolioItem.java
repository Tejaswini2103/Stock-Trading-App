package ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ViewHolderForPortfolioItem extends RecyclerView.ViewHolder {

    final public TextView ticker_portfolio;
    final public TextView description_portfolio;
    final public TextView lastPrice_portfolio;
    final public TextView change_portfolio;
    final public ImageView trending_portfolio;
    final public ImageView arrow_portfolio;

    public ViewHolderForPortfolioItem(View view) {
        super(view);
        ticker_portfolio = (TextView) view.findViewById(R.id.ticker_portfolioView);
        description_portfolio = (TextView) view.findViewById(R.id.descrption_portfolioView);
        lastPrice_portfolio = (TextView) view.findViewById(R.id.lastPrice_portfolioView);
        change_portfolio = (TextView) view.findViewById(R.id.changeValue_portfolio);
        trending_portfolio = (ImageView) view.findViewById(R.id.trending_portfolioView);
        arrow_portfolio = (ImageView) view.findViewById(R.id.rightArrow_portfolioView);
    }

}

