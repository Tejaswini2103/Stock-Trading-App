package Details.News;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.content.Context;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
public class ImageShaper {
    public static void loadRounded(Context context, ImageView imageView, String url) {
        Picasso.get()
                .load(url)
                .transform(new RoundedCornersTransformation(40, 0))
                .fit()
                .into(imageView);
    }
}

