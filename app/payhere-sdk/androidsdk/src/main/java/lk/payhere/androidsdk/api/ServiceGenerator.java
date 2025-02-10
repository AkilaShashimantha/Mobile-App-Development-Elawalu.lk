package lk.payhere.androidsdk.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lk.payhere.androidsdk.PHConfigs;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceGenerator {

    //Constants
    private static String BASE_URL = PHConfigs.LIVE_URL;

    //Instances
    private static OkHttpClient okHttpClient;
    private static Map<String, Object> clients = new HashMap<>();

    //primary data
    private static String accessToken = null;

    public static <T> T createService(Context context, Class<T> serviceClass) {
        Object client = clients.get(getKey(serviceClass));
        if (client == null) {
            client = initService(context, serviceClass, null);
            clients.put(getKey(serviceClass), client);
        }
        return (T) client;
    }

    public static <T> T createService(Context context, Class<T> serviceClass, String accessToken) {
        Object client = initService(context, serviceClass, accessToken);
        clients.put(getKey(serviceClass), client);
        return (T) client;
    }

    private static <T> T initService(Context context, Class<T> serviceClass, String accessToken) {

        if (PHConfigs.BASE_URL != null && !PHConfigs.BASE_URL.equals(""))
            BASE_URL = PHConfigs.BASE_URL;

        initHttpClient(context, accessToken);
        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(BaseFeature.class, new PostJsonDeserializer());
        Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm").create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.client(okHttpClient).build();
        T client = retrofit.create(serviceClass);
        clients.put(getKey(serviceClass), client);

        return client;
    }

    private static void initHttpClient(final Context context, String accessToken) {

        if (okHttpClient == null || (accessToken != null && (ServiceGenerator.accessToken == null ||
                !ServiceGenerator.accessToken.equals(accessToken)))) {
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            CustomInterceptor interceptor = new CustomInterceptor(context) {
                @Override
                public boolean isInternetAvailable() {
                    return ServiceGenerator.checkNetworkAvailability(context);
                }

                @Override
                public void onInternetUnavailable() {
                }
            };

            ServiceGenerator.accessToken = accessToken;
            if (ServiceGenerator.accessToken == null)
                httpClient.addInterceptor(interceptor);
            else
                httpClient.addInterceptor(interceptor.setAccessToken(ServiceGenerator.accessToken));

            //  httpClient.addInterceptor(loggingInterceptor);


//            httpClient.certificatePinner(getCertificate());
            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            okHttpClient = httpClient.build();
        }
    }

    private static <T> String getKey(Class<T> serviceClass) {
        return serviceClass.getSimpleName();
    }

    private static boolean checkNetworkAvailability(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isAvailable = false;
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

//    private static CertificatePinner getCertificate() {
//        CertificatePinner.Builder certPinnerBuilder = new CertificatePinner.Builder();
//        certPinnerBuilder.add("helakuru.lk", "sha256/grX4Ta9HpZx6tSHkmCrvpApTQGo67CYDnvprLg5yRME=");
//        return certPinnerBuilder.build();
//    }
}
