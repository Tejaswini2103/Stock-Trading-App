package Common;
import android.widget.Toast;
import android.content.Context;

public class ToastManager {
    private final Context context;
    private Toast toast;
    public ToastManager(Context context) { this.context = context; }
    public void showToast(String message) {
        this.toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        this.toast.show();
    }
}
