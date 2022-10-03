package Details.News;
import Details.Common.NewsItem;
import android.app.Dialog;
import android.content.Context;
import com.example.myapplication.R;
import android.view.View;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.widget.ImageButton;

public class NewsDialog {
    private final Context context;
    private NewsItem item;
    private final TextView news_titleView;
    private final Dialog dialog;
    private final TextView news_date;
    private final TextView news_summary;
    private final TextView news_title;

    public NewsDialog(Context context) {
        this.context = context;

        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_news);
        news_titleView = dialog.findViewById(R.id.news_dialog_tv_title);
        news_date = dialog.findViewById(R.id.news_dialog_tv_title2);
        news_title = dialog.findViewById(R.id.news_dialog_titleactual);
        news_summary = dialog.findViewById(R.id.news_summary);
        Twitter();
        Chrome();
        facebook();
    }

    public void show(NewsItem item) {
        news_titleView.setText(item.getPublisher());
        news_date.setText("April 8, 2022");
        news_title.setText(item.getTitle());
        news_summary.setText(item.getSummary());
        System.out.println("inside show*********************");
        System.out.println(item.getUrl());
        this.item = item;
        dialog.show();
    }

    private void Twitter() {
        ImageButton twitterButton = dialog.findViewById(R.id.news_dialog_b_view);
        System.out.println("inside twitterButton view");
        twitterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                System.out.println("******************************");
                System.out.println(item.getUrl());
                String url = getTwitterURL(item.getUrl());
                openInNewView(url);
            }
        });
    }
    private String getTwitterURL(String url) {
        return (new Uri.Builder()).scheme("https").encodedAuthority("twitter.com").encodedPath("intent/tweet").appendQueryParameter("text", "Check out this Link:").appendQueryParameter("url", url).appendQueryParameter("hashtags", "Stocks").build().toString();
    }
    private void openInNewView(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
    public void Chrome() {
        ImageButton viewButton = dialog.findViewById(R.id.news_dialog_b_share);
        viewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                openInNewView(item.getUrl());
            }
        });
    }
    public void facebook() {
        ImageButton viewButton = dialog.findViewById(R.id.news_dialog_b_view2);
        viewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                System.out.println(item.getUrl());
                String url = "https://www.facebook.com/sharer/sharer.php?u="+item.getUrl();
                openInNewView(url);
            }
        });
    }

}
