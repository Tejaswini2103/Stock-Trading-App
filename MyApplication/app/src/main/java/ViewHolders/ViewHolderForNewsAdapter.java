package ViewHolders;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.example.myapplication.R;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import android.widget.TextView;

public class ViewHolderForNewsAdapter extends RecyclerView.ViewHolder {
    final public ImageView image_newsSection;
    final public TextView publisher_newsSection;
    final public TextView publishedAt_newsSection;
    final public TextView title_newsSection;
    public ViewHolderForNewsAdapter(@NonNull View itemView) {
        super(itemView);
        image_newsSection = itemView.findViewById(R.id.news_image);
        publisher_newsSection = itemView.findViewById(R.id.news_publishedBy);
        publishedAt_newsSection = itemView.findViewById(R.id.news_publishedAt);
        title_newsSection = itemView.findViewById(R.id.news_title);
    }
}