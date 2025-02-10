package lk.payhere.androidsdk.api;

import android.content.Context;

import java.io.IOException;

import lk.payhere.androidsdk.R;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class CustomInterceptor implements Interceptor {

    //instance
    private Context context;

    //primary data
    private String accessToken;

    public CustomInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (!isInternetAvailable()) {
            onInternetUnavailable();
            throw new IOException(context.getString(R.string.msg_no_internet));
        }

        try {
//            if (original.header("@headers") != null) {
//                switch (original.header("@headers")) {
//                    case "no":
//                        return chain.proceed(original.newBuilder().removeHeader("@headers").build());
//                    case "payhere":
//                        return chain.proceed(original.newBuilder()
//                                .addHeader("token", accessToken)
//                                .removeHeader("@headers")
//                                .build());
//                }
//            }

            return chain.proceed(original.newBuilder().addHeader("Content-Type", "application/json").build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return chain.proceed(original.newBuilder().addHeader("Content-Type", "application/json").build());
    }

    public abstract boolean isInternetAvailable();

    public abstract void onInternetUnavailable();

    CustomInterceptor setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
