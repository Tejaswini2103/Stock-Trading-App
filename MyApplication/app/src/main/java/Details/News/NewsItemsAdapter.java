package Details.News;

import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import com.example.myapplication.R;
import ViewHolders.ViewHolderForNewsAdapter;
import Details.Common.NewsItem;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class NewsItemsAdapter extends RecyclerView.Adapter<ViewHolderForNewsAdapter> {
    private static final int VIEW_NEWS_ITEM_BIG = 0;
    private static final int VIEW_NEWS_ITEM_SMALL = 1;
    private final NewsHandler actionHandler;
    private final List<NewsItem> newsItems;
    private final Context context;
    public NewsItemsAdapter(Context context, List<NewsItem> newsItems, NewsHandler actionHandler) {
        this.context = context;
        this.newsItems = newsItems;
        this.actionHandler = actionHandler;
    }
    public interface NewsHandler {
        void onNewsClick(NewsItem item);
    }
    @NonNull
    @Override
    public ViewHolderForNewsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewType) {
            case VIEW_NEWS_ITEM_BIG: {
                layoutId = R.layout.top_news_item;
                break;
            }
            case VIEW_NEWS_ITEM_SMALL: {
                layoutId = R.layout.news_item_display;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        View news_view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        news_view.setFocusable(true);
        return new ViewHolderForNewsAdapter(news_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForNewsAdapter newsViewHolder, int pos) {
        NewsItem item = newsItems.get(pos);
        ImageShaper.loadRounded(context, newsViewHolder.image_newsSection, item.getUrlToImage());
        newsViewHolder.title_newsSection.setText(item.getTitle());
        newsViewHolder.publishedAt_newsSection.setText(item.getPublishedAt());
        newsViewHolder.publisher_newsSection.setText(item.getPublisher());
        newsViewHolder.itemView.setOnClickListener(v -> actionHandler.onNewsClick(item));

    }
    @Override
    public int getItemCount() { return newsItems.size(); }
    @Override
    public int getItemViewType(int position) { return position == 0 ? VIEW_NEWS_ITEM_BIG : VIEW_NEWS_ITEM_SMALL; }
}
