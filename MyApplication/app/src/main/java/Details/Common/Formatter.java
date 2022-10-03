package Details.Common;

import java.text.DecimalFormat;

public class Formatter {
    private static final DecimalFormat priceFormatter = new DecimalFormat("#0.00;-#0.00");
    public static String getPriceString(Double value) {
        if (value == null) { value = 0.00; }
        return priceFormatter.format(value);
    }
}