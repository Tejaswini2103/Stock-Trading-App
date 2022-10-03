package Details.News;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import Details.Common.NewsItem;
import android.content.Context;
import java.util.List;
import com.example.myapplication.R;

public class NewsRVInitializer implements NewsItemsAdapter.NewsHandler {
    private final NewsDialog newsDialog;
    private final Context context;
    public NewsRVInitializer(Activity activity, Context context, List<NewsItem> news) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView_newsInDetailsView);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        NewsItemsAdapter adapter = new NewsItemsAdapter(context, news, this);
        recyclerView.setAdapter(adapter);
        newsDialog = new NewsDialog(context);
        this.context = context;
    }
    @Override
    public void onNewsClick(NewsItem item) {
        newsDialog.show(item);
    }
}

