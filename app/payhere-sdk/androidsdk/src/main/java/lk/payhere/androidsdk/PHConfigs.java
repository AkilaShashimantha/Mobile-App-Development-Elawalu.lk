package lk.payhere.androidsdk;

import java.util.HashMap;

import lk.payhere.androidsdk.api.PayhereSDK;
import lk.payhere.androidsdk.api.ServiceGenerator;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chamika on 9/19/16.
 */

public class PHConfigs {

    public static final String LIVE_URL = "https://www.payhere.lk/pay/";
    public static final String SANDBOX_URL = "https://sandbox.payhere.lk/pay/";
    public static final String LOCAL_URL = "http://localhost:8080//pay/";

    public static final String CHECKOUT =  "checkout";
    public static final String STATUS =  "order_status";

    private static OnMethodReceivedListener listener;

    public static String BASE_URL = null;

    public static void setBaseUrl(String url){
        BASE_URL = url;
    }

    public static String getBaseUrl(){
        return BASE_URL;
    }

    public static void readMethods(final PHMainActivity activity) {
        ServiceGenerator.createService(activity, PayhereSDK.class).getPaymentMethods().enqueue(new Callback<PaymentMethodResponse>() {
            @Override
            public void onResponse(Call<PaymentMethodResponse> call, Response<PaymentMethodResponse> response) {
                PaymentMethodResponse result = response.body();
                if (result != null && result.getStatus() == 1) {
                   // activity.setMethods(result.getData());
                    if(listener != null)
                        listener.onMethodReturned(result.getData());
                }
            }

            @Override
            public void onFailure(Call<PaymentMethodResponse> call, Throwable t) {
            }
        });
    }

    public static void setOnMethodReceivedListener(OnMethodReceivedListener listener) {
        PHConfigs.listener = listener;
    }

    public interface OnMethodReceivedListener {
        void onMethodReturned(HashMap<String, PaymentMethodResponse.Data> methods);
    }
}
