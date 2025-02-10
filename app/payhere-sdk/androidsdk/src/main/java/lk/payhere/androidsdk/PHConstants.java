package lk.payhere.androidsdk;

import java.util.Locale;

/**
 * Created by chamika on 9/18/16.
 */

public class PHConstants {
    public final static String INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA";
    public final static String INTENT_EXTRA_RESULT = "INTENT_EXTRA_RESULT";
    public final static String INTENT_EXTRA_SKIP_RESULT = "INTENT_EXTRA_SKIP_RESULT";
    public final static String INTENT_EXTRA_REFERENCE = "INTENT_EXTRA_REFERENCE";
    public final static String INTENT_EXTRA_AUTO = "INTENT_EXTRA_AUTO";
    public final static String INTENT_EXTRA_HOLD = "INTENT_EXTRA_HOLD";
    public final static String INTENT_EXTRA_METHOD = "INTENT_EXTRA_METHOD";
    public final static String INTENT_EXTRA_HEIGHT = "INTENT_EXTRA_HEIGHT";
    public static final String INTENT_EXTRA_STATUS = "INTENT_EXTRA_STATUS";
    public static final String INTENT_EXTRA_MESSAGE = "INTENT_EXTRA_MESSAGE";
    public static final String INTENT_EXTRA_HELA_PAY = "INTENT_EXTRA_HELA_PAY";
    public static final String INTENT_EXTRA_ORDER_KEY = "INTENT_EXTRA_ORDER_KEY";
    public static final String INTENT_EXTRA_CACHE_ENABLE = "INTENT_EXTRA_CACHE_ENABLE";

    public final static String PLATFORM = "android";

    public static final String dummyUrl = "https://www.payhere.lk/complete/android";
    public static final String PAYMENT_COMPLETE_URL = "https://www.payhere.lk/pay/payment/complete";
    public static final String PAYMENT_COMPLETE_SANDBOX_URL = "https://sandbox.payhere.lk/pay/payment/complete";
//    public static final String BASE_URL = PHConfigs.BASE_URL;
    public static final String URL_INIT_PAYMENT = "api/payment/initAndSubmit";
    public static final String URL_PAYMENT_METHODS = "api/data/paymentMethods";

    public static final Locale DEFAULT_LOCALE = Locale.US;

    //public static final String PAYHERE_VERSION = BuildConfig.VERSION_NAME;

    public static final String URL_INIT_PAYMENT_V2 = "api/payment/v2/init";

    public static final String METHOD_BANK = "BANK";
    public static final String METHOD_CARD = "CARD";
    public static final String METHOD_OTHER = "OTHER";

    public static final String SUMBITION_CODE_HELAPAY = "HELAPAY";
}
