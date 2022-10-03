package ViewHolders;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.example.myapplication.R;
import android.widget.TextView;

public class ViewHolderForFavorites extends RecyclerView.ViewHolder {
    final public TextView lastPriceinFavs;
    final public ImageView trendingInFavs;
    final public TextView tickerinFavs;
    final public TextView changeInFavs;
    final public ImageView arrowinFavs;
    final public TextView descriptionView;

    public ViewHolderForFavorites(View view) {
        super(view);
        lastPriceinFavs = (TextView) view.findViewById(R.id.lastPrice_favsView);
        trendingInFavs = (ImageView) view.findViewById(R.id.rightArrow_favsViewView);
        tickerinFavs = (TextView) view.findViewById(R.id.tickerinFavorites);
        changeInFavs = (TextView) view.findViewById(R.id.changeValue_favsView);
        arrowinFavs = (ImageView) view.findViewById(R.id.rightArrow_favsView);
        descriptionView = (TextView) view.findViewById(R.id.description_favs_view);

    }
}

