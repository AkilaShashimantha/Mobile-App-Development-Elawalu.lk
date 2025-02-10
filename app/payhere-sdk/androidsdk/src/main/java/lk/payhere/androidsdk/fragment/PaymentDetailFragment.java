package lk.payhere.androidsdk.fragment;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import lk.payhere.androidsdk.BuildConfig;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.R;
import lk.payhere.androidsdk.api.PayhereSDK;
import lk.payhere.androidsdk.api.ServiceGenerator;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentDetails;
import lk.payhere.androidsdk.model.PaymentInitResult;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import lk.payhere.androidsdk.model.StatusResponse;
import lk.payhere.androidsdk.util.NetworkHandler;
import lk.payhere.androidsdk.util.ObservableWebView;
import lk.payhere.androidsdk.util.Utils;
import lk.payhere.androidsdk.util.WebViewBottomSheetbehaviour;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailFragment extends Fragment implements PHMainActivity.OnActivityAction {

    private String TAG = PaymentDetailFragment.class.getSimpleName();

    private InitBaseRequest request;
    private Handler handlerForJavascriptInterface = new Handler();
    private PHMainActivity activity;

    private String orderKey;
    private String method;
    private StatusResponse lastResponse;
    private boolean dataLoading = false;
    private int initHeight;
    private boolean isCardSave = false;
   // private boolean isHelapay = false;

   // int count = 0;

    private NewInitResponse.Submission hela_pay;

    private View progressFrame;
    private ObservableWebView webView;
    private ImageView progressImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                request = (InitBaseRequest) getArguments().getSerializable(PHConstants.INTENT_EXTRA_DATA);
                method = getArguments().getString(PHConstants.INTENT_EXTRA_METHOD);
                initHeight = getArguments().getInt(PHConstants.INTENT_EXTRA_HEIGHT);
                isCardSave = getArguments().getBoolean(PHConstants.INTENT_EXTRA_AUTO);
                hela_pay = (NewInitResponse.Submission) getArguments().getSerializable(PHConstants.INTENT_EXTRA_HELA_PAY);
                orderKey = getArguments().getString(PHConstants.INTENT_EXTRA_ORDER_KEY);
            }
        } catch (Exception e) {
            Log.e(TAG, "Request data not found", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
       int width = Utils.screenHight(activity);
        View view = inflater.inflate(R.layout.ph_fragment_payment_details, parent, false);
       // view.setMinimumHeight((int) (width*0.5));

        progressFrame = view.findViewById(R.id.ph_progress_frame);
        webView = view.findViewById(R.id.ph_webview);
        progressImage = (ImageView)view.findViewById(R.id.progress_view);
        webView.setBackgroundColor(getResources().getColor(R.color.white));
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        webView.setVisibility(View.INVISIBLE);

        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.progress_color), android.graphics.PorterDuff.Mode.MULTIPLY);



        boolean networkAvailable = checkNetworkAvailability();
        if (!networkAvailable) {
            setError(new PHResponse(PHResponse.STATUS_ERROR_NETWORK, "Unable to connect to the internet"), true);
        } else {

            if(method.toUpperCase().equals("HELAPAY")){
                if(hela_pay != null && orderKey != null){
//                    displayProgress(true);
                    openWebBroser();
                   // getActualLink();
                }
                else {
                    Log.d(TAG,"order key or helapay paymenthoad null");
                    setError(new PHResponse(PHResponse.STATUS_ERROR_UNKNOWN, "Unable to process helapay payment"), true);
                }
            }
            else {
                setWebView(view);
                initPayment(view.getContext());
            }
        }

        return view;
    }

    //for load helapy link to external web browser
    private void openWebBroser() {

        Intent browserIntent  = new Intent(Intent.ACTION_VIEW,Uri.parse(hela_pay.getUrl()));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(browserIntent);
    }

    private void runStatusChecker(long wait){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new StatusChecker(false,false,true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,orderKey);
            }
        },wait);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (PHMainActivity) getActivity();
    }

    private void setWebView(View view) {

        if (isCardSave) {
            setWebViewHeight(initHeight, true);
        } else {
            setWebViewHeight(initHeight, true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (activity.getMethods() != null) {
                        NewInitResponse.PaymentMethod data = activity.getMethods().get(method);
                        if (data != null && data.getView().getWindowSize() != null)
                            setWebViewHeightAnimated(data.getView().getWindowSize().getHeight());
                    } else {
                        PHConfigs.setOnMethodReceivedListener(new PHConfigs.OnMethodReceivedListener() {
                            @Override
                            public void onMethodReturned(HashMap<String, PaymentMethodResponse.Data> methods) {
                                PaymentMethodResponse.Data data = methods.get(method);
                                if (data != null && data.getViewSize() != null)
                                    setWebViewHeightAnimated(data.getViewSize().getHeight());
                            }
                        });
                    }
                }
            }, 250);
        }

        displayProgress(true);

        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);

      //  webView.getSettings().setAppCacheEnabled(PHMainActivity.cacheEnabled);

        webView.getSettings().setSaveFormData(PHMainActivity.cacheEnabled);
        webView.getSettings().setSavePassword(PHMainActivity.cacheEnabled);
        webView.getSettings().setCacheMode(PHMainActivity.cacheEnabled ? WebSettings.LOAD_CACHE_ELSE_NETWORK : WebSettings.LOAD_NO_CACHE);

//        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(activity), "HtmlViewer");

        webView.setWebViewClient(new WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "URL Loading res:" + request.getUrl().toString());
               // System.out.println("======== : " + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "URL Loading url:" + url);

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted() :" + url);
                if (url.startsWith(PHConstants.PAYMENT_COMPLETE_URL) || url.startsWith(PHConstants.PAYMENT_COMPLETE_SANDBOX_URL)) {
//                    Uri uri = Uri.parse(url);
//                    orderKey = uri.getQueryParameter("order");
                    checkComplete(false, true);
                }
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                webView.setVisibility(View.VISIBLE);
//                view.loadUrl("javascript:window.HtmlViewer.showHTML" +
//                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');" +
//                        "javascript:(function() { " +
//                        "var meta = document.createElement('meta');" +
//                        "meta.name='viewport';" +
//                        "meta.content='width=device-width';" +
//                        "document.getElementsByTagName('head')[0].appendChild(meta);" +
//                        "})()");

                view.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                view.clearCache(true);
                view.clearFormData();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if("net::ERR_BLOCKED_BY_ORB".contains(error.getDescription())){
                        view.loadUrl(formatUrl(request.getUrl().toString()));
                        return;
                    }

                }

                if (!PaymentDetailFragment.this.request.isSandBox())
                    activity.goBackToApp();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

//
                if("net::ERR_BLOCKED_BY_ORB".contains(description)){
                        view.loadUrl(formatUrl(failingUrl));
                        return;
                }


                if (!PaymentDetailFragment.this.request.isSandBox())
                    activity.goBackToApp();

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });

        webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
            @Override
            public void onScrollTopEnd(boolean value) {
                WebViewBottomSheetbehaviour.scrollEnabled = value;
            }
        });


    }

    private void setWebViewHeight(final int height, boolean directValue) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) webView.getLayoutParams();
        Log.d(TAG, "Height of set value - " + height);
        params.height =directValue ? height : getDeviceSpecHeight(height);; //(int) (Utils.screenHight(activity) * 0.//
        webView.setLayoutParams(params);
    }

    private void setWebViewHeightAnimated(int height) {
        int viewHeight = getDeviceSpecHeight(height);

        Log.d(TAG, "Height of input value - " + height);
        Log.d(TAG, "Height of webview value - " + webView.getMeasuredHeight());
        Log.d(TAG, "Height of view value - " + viewHeight);
        ValueAnimator anim = ValueAnimator.ofInt(webView.getMeasuredHeight(), viewHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setWebViewHeight((Integer) valueAnimator.getAnimatedValue(), true);
            }
        });
        anim.setDuration(200);
        anim.start();
    }

    private int getDeviceSpecHeight(int height) {

        int deviceHeight = getDeviceheight();
        int maxHeight = (int) (deviceHeight * 0.9); // 0.9
        if (height == 400 && isCardSave)
            height = height * deviceHeight / 800;
        int viewHeight = initHeight * height / 400;
        if (viewHeight > maxHeight)
            viewHeight = maxHeight;
        return viewHeight;
    }

    private void startProcess(String url) {
        String validate = validate();
        if (validate == null) {

            //check network
            boolean networkAvailable = checkNetworkAvailability();
            if (!networkAvailable) {
                setError(new PHResponse(PHResponse.STATUS_ERROR_NETWORK, "Unable to connect to the internet"), true);
                return;
            }

//            if("JUSTPAY".equals(method)){
                   // load(url);
//            }
//            else
         webView.loadUrl(url);
        }
        else {
            setError(new PHResponse(PHResponse.STATUS_ERROR_VALIDATION, validate), true);
        }
    }

    private String formatUrl(String url){
        return "<html>" +
                "<body>" +
                "<iframe src='"+url+"' width='100%' height='100%'></iframe>" +
                "</body>" +
                "</html>";
       // webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @Override
    public boolean onActivityBack() {
        return checkComplete(false, true);
    }

    class MyJavaScriptInterface {
        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(final String html) {
            //code to use html content here
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {
                    int refIndex = html.indexOf("reference_id");
                    if (refIndex >= 0) {
                        String valueStr = "value=\"";
                        int orderIndex = html.indexOf(valueStr, refIndex);
                        int start = orderIndex + valueStr.length();
                        String orderKey = html.substring(start, html.indexOf("\"", start));
//                        Toast.makeText(PHMainActivity.this, "Order Key=" + orderKey, Toast.LENGTH_LONG).show();
                        PaymentDetailFragment.this.orderKey = orderKey;

                        new StatusChecker(false, false,false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, orderKey);
                    }
                }
            });
        }
    }

    private boolean checkNetworkAvailability() {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isAvailable = false;
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    private boolean checkComplete(boolean background, boolean finishActivity) {
        if (dataLoading) {
            Toast.makeText(activity, "Please wait!!", Toast.LENGTH_LONG).show();
        } else if (!isStatusUpdated()) {
            new StatusChecker(background, finishActivity,false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, orderKey);
        }
        else {
            int paymentStatus = getStatusFromResponse();
            if (lastResponse.getStatusState() == StatusResponse.Status.SUCCESS) {
                setError(new PHResponse(getStatusFromResponse(), "Payment success. Check response data", lastResponse), finishActivity);
            } else if (lastResponse.getStatusState() == StatusResponse.Status.FAILED) {
                setError(new PHResponse(getStatusFromResponse(), "Payment failed. Check response data", lastResponse), finishActivity);
            } else {
                String stage = lastResponse.getStatusState() == StatusResponse.Status.INIT ? "initial" : (lastResponse.getStatusState() == StatusResponse.Status.PAYMENT) ? "payment selection" : "unknown";
                setError(new PHResponse(paymentStatus, "Payment in " + stage + " stage. Check response data", lastResponse), finishActivity);
            }
            return true;
        }
        return finishActivity;
    }

    class StatusChecker extends AsyncTask<String, Void, StatusResponse> {

        private boolean background;
        private boolean postActivityFinish;
        private final boolean isHelapayPayment;

        public StatusChecker(boolean background, boolean postActivityFinish,boolean isHelapayPayment) {
            this.background = background;
            this.postActivityFinish = postActivityFinish;
            this.isHelapayPayment = isHelapayPayment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!background) {
                // show progress
                displayProgress(true);
            }

        }

        @Override
        protected StatusResponse doInBackground(String... params) {
            dataLoading = true;
            Map<String, String> statusParams = new HashMap<>();
            statusParams.put("order_key", params[0]);
            try {
                String response = NetworkHandler.sendPost(PHConfigs.BASE_URL + PHConfigs.STATUS, statusParams);
                Gson gson = new GsonBuilder().create();
                StatusResponse statusResponse = gson.fromJson(response, StatusResponse.class);
                if (statusResponse != null)
                    System.out.println(statusResponse.toString());
                //validate response
//                boolean validate = validate(request, statusResponse);
//                return validate ? statusResponse : null;
                return statusResponse;


            } catch (Exception e) {
                Log.d(TAG, "Unable to get order status", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(StatusResponse statusResponse) {
            super.onPostExecute(statusResponse);
            lastResponse = statusResponse;
            if (statusResponse == null) {
                Toast.makeText(activity, "Illegal request", Toast.LENGTH_LONG).show();
                activity.finish();
                return;
            }

//            if(count == 3){
//                lastResponse = testRespons();
//
//            }

            //check time out
            if(isHelapayPayment && lastResponse.getStatusState() == StatusResponse.Status.INIT
                    && !PHMainActivity.DISCONNECTED ){
                runStatusChecker(5000);
               // count++;
                return;
            }


            if (lastResponse.getStatusState() == StatusResponse.Status.SUCCESS) {
                activity.setPayResultView(new PHResponse(getStatusFromResponse(), "Payment success. Check response data", lastResponse), request instanceof InitPreapprovalRequest, false);
            } else if (lastResponse.getStatusState() == StatusResponse.Status.HOLD) {
                    activity.setPayResultView(new PHResponse(getStatusFromResponse(), "Payment success. Check response data", lastResponse), request instanceof InitPreapprovalRequest, true);
            } else if (lastResponse.getStatusState() == StatusResponse.Status.FAILED) {
                activity.setPayResultView(new PHResponse(getStatusFromResponse(), "Payment failed. Check response data", lastResponse), request instanceof InitPreapprovalRequest, true);
               // setError(new PHResponse(getStatusFromResponse(), "Payment failed. Check response data", lastResponse), true);
            } else if (postActivityFinish) {
                String stage = lastResponse.getStatusState() == StatusResponse.Status.INIT ? "initial" : (lastResponse.getStatusState() == StatusResponse.Status.PAYMENT) ? "payment selection" : "unknown";
                setError(new PHResponse(getStatusFromResponse(), "Payment in " + stage + " stage. Check response data", lastResponse), true);
            }
            if (!background) {
                displayProgress(false);
            }
            dataLoading = false;
        }
    }

    private boolean isStatusUpdated() {
        //TODO create a mechanism to check data expiry and return false if expired
        return false;
    }

    private  int getStatusFromResponse() {
        return (lastResponse.getStatusState() == StatusResponse.Status.SUCCESS) ? PHResponse.STATUS_SUCCESS : PHResponse.STATUS_ERROR_PAYMENT;
    }

//    private boolean validate(InitBaseRequest request, StatusResponse response) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(request.getMerchantId());
//        sb.append(request.getOrderId());
//        if (request instanceof InitRequest) {
//            InitRequest initReq = (InitRequest) request;
//            sb.append(ParamHandler.convertAmount(initReq.getAmount()));
//            sb.append(initReq.getCurrency());
//        } else {
//            sb.append(ParamHandler.convertAmount(1.0f));
//            sb.append("LKR");
//        }
//        sb.append(response.getStatus());
//        String secret = request.getMerchantSecret();
//        if (secret != null) {
//            String secretMd5 = SecurityUtils.generateMD5Value(secret);
//            sb.append(secretMd5);
//        }
//        String md5Sig = SecurityUtils.generateMD5Value(sb.toString());
////        Log.d(TAG, "final key=" + sb.toString() + " md5Sig=" + md5Sig);
//        return md5Sig != null && md5Sig.equals(response.getSign());
//    }

    private void setError(PHResponse response, boolean finish) {
        Intent intent = new Intent();
        intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, response);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        if (finish) {
            //changed to finish
            activity.finish();
        }
    }

    private void displayProgress(boolean visible) {

        if (visible) {
            Glide.with(activity).asGif().load(R.drawable.spinner_circle).into(progressImage);
            progressFrame.setVisibility(View.VISIBLE);
        } else {
            progressFrame.setVisibility(View.GONE);
        }
    }

    private String validate() {
        if (PHConfigs.BASE_URL == null) {
            return "BASE_URL not set";
        }

        if (request instanceof InitRequest) {
            InitRequest initReq = (InitRequest) request;
            if (initReq.getAmount() <= 0) {
                return "Invalid amount";
            }
            if (initReq.getCurrency() == null || initReq.getCurrency().length() != 3) {
                return "Invalid currency";
            }
        }

        if (request.getMerchantId() == null || request.getMerchantId().length() == 0) {
            return "Invalid merchant ID";
        }
//        if (request.getMerchantSecret() == null || request.getMerchantSecret().length() == 0) {
//            return "Invalid merchant secret";
//        }
        return null;
    }

    private void initPayment(Context context) {
        displayProgress(true);

        final PaymentDetails paymentDetails = new PaymentDetails();
        if (request != null) {
            paymentDetails.setOrderId(request.getOrderId());
            paymentDetails.setMerchantId(request.getMerchantId());
            paymentDetails.setCurrency(request.getCurrency());
            paymentDetails.setAmount(request.getAmount());

            if (request instanceof InitRequest) {
                InitRequest initReq = (InitRequest) request;

                paymentDetails.setRecurrence(initReq.getRecurrence());
                paymentDetails.setDuration(initReq.getDuration());
                paymentDetails.setStartupFee(initReq.getStartupFee());
            }

            if (request.getItems() != null) {
                int i = 1;
                HashMap<String, String> itemMap = new HashMap<>();
                for (Item item : request.getItems()) {
                    itemMap.put("item_name_" + i, item.getName());
                    itemMap.put("item_number_" + i, item.getId());
                    itemMap.put("amount_" + i, String.valueOf(item.getAmount()));
                    itemMap.put("quantity_" + i, String.valueOf(item.getQuantity()));
                    i++;
                }
                paymentDetails.setItemsMap(itemMap);
            } else
                paymentDetails.setItemsMap(null);

            paymentDetails.setItemsDescription(request.getItemsDescription());
            paymentDetails.setFirstName(request.getCustomer().getFirstName());
            paymentDetails.setLastName(request.getCustomer().getLastName());
            paymentDetails.setEmail(request.getCustomer().getEmail());
            paymentDetails.setPhone(request.getCustomer().getPhone());
            paymentDetails.setAddress(request.getCustomer().getAddress().getAddress());
            paymentDetails.setCity(request.getCustomer().getAddress().getCity());
            paymentDetails.setCountry(request.getCustomer().getAddress().getCountry());
            paymentDetails.setDeliveryAddress(request.getCustomer().getDeliveryAddress().getAddress());
            paymentDetails.setDeliveryCity(request.getCustomer().getDeliveryAddress().getCity());
            paymentDetails.setDeliveryCountry(request.getCustomer().getDeliveryAddress().getCountry());

            paymentDetails.setCustom1(request.getCustom1());
            paymentDetails.setCustom2(request.getCustom2());
            paymentDetails.setReturnUrl(request.getReturnUrl() == null ? PHConstants.dummyUrl : request.getReturnUrl());
            paymentDetails.setCancelUrl(request.getCancelUrl() == null ? PHConstants.dummyUrl : request.getCancelUrl());
            paymentDetails.setNotifyUrl(request.getNotifyUrl() == null ? PHConstants.dummyUrl : request.getNotifyUrl());

            paymentDetails.setPlatform(PHConstants.PLATFORM);
            if (BuildConfig.DEBUG)
                paymentDetails.setReferer("lk.bhasha.helakuru");
            else
                paymentDetails.setReferer(activity.getPackageName());
            paymentDetails.setHash("");

            paymentDetails.setMethod(method);
            paymentDetails.setAuto(request instanceof InitPreapprovalRequest);
            paymentDetails.setAuthorize(request.isHoldOnCardEnabled());

        }

        ServiceGenerator.createService(context, PayhereSDK.class).initPayment(paymentDetails).enqueue(new Callback<PaymentInitResult>() {
            @Override
            public void onResponse(Call<PaymentInitResult> call, Response<PaymentInitResult> response) {
                PaymentInitResult result = response.body();
                if (result != null && result.getStatus() == 1) {
                    startProcess(result.getData().getRedirection().getUrl());
                    if (result.getData() != null && result.getData().getOrder() != null)
                        orderKey = result.getData().getOrder().getOrderKey();
                } else
                    setError(new PHResponse(PHResponse.STATUS_ERROR_UNKNOWN, result == null ? "Error Occurred" : result.getMsg()), true);
                displayProgress(false);
            }

            @Override
            public void onFailure(Call<PaymentInitResult> call, Throwable t) {
                displayProgress(false);
                String msg = "Error Occurred";
                if(t != null && t.getMessage() != null && !t.getMessage().equals(""))
                    msg = t.getMessage();
                setError(new PHResponse(PHResponse.STATUS_ERROR_UNKNOWN, msg), true);
            }
        });
    }

    private int getDeviceheight() {
//        Display mDisplay = activity.getWindowManager().getDefaultDisplay();
//        return mDisplay.getHeight();

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


//    private StatusResponse testRespons(){
//        StatusResponse statusResponse = new StatusResponse();
//        statusResponse.setStatus(StatusResponse.Status.SUCCESS.value());
//        statusResponse.setMessage("payment success");
//        statusResponse.setPaymentNo(1234567890);
//        return statusResponse;
//    }

}
