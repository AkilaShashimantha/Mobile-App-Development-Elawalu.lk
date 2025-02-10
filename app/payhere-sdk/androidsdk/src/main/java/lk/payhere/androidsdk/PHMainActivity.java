package lk.payhere.androidsdk;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import lk.payhere.androidsdk.fragment.PaymentDetailFragment;
import lk.payhere.androidsdk.fragment.PaymentMethodFragment;
import lk.payhere.androidsdk.fragment.PaymentResultFragment;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentInitResponse;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import lk.payhere.androidsdk.model.StatusResponse;
import lk.payhere.androidsdk.util.NetworkHandler;
import lk.payhere.androidsdk.util.Utils;
import lk.payhere.androidsdk.util.WebViewBottomSheetbehaviour;

public class PHMainActivity extends PayHereBaseActivity {

    //constants
    private final int VIEW_METHOD = 1;
    private final int VIEW_DETAILS = 2;
    private final int VIEW_RESULT = 3;

    //instance
    // private View imgBack;
    private BottomSheetBehavior sheetBehavior;
    private OnActivityAction listener;
    private Bundle bundle;
    // private HashMap<String, PaymentMethodResponse.Data> methods;
    private HashMap<String, NewInitResponse.PaymentMethod> methods;
    private PHResponse<StatusResponse> statusResponse;

    //primary data
    private int view = VIEW_METHOD;
    private int viewheight;
    private boolean isSaveCard = false;
    private int peekHeight = 0;
    private int prevState = BottomSheetBehavior.STATE_COLLAPSED;
    private boolean isMinimize = false;

    //enable webview cache
    public static boolean cacheEnabled = false;

    //views
    private TextView txtTitle;
    private View bottomSheet;
    private boolean skipResult = false;
    private boolean closeBottomSheet = true;
    private boolean isHelapayPayment = false;

    private View titleBar ,backView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int width = Utils.screenHight(this);

        if (!getIntent().hasExtra(PHConstants.INTENT_EXTRA_DATA)) {
            PHResponse response = new PHResponse(PHResponse.STATUS_ERROR_DATA, PHConstants.INTENT_EXTRA_DATA + " not found");
            Intent intent = new Intent();
            intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, response);
            setResult(Activity.RESULT_CANCELED, intent);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (getIntent().hasExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT)) {
            skipResult = getIntent().getBooleanExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, false);
        }

        if (getIntent().hasExtra(PHConstants.INTENT_EXTRA_CACHE_ENABLE)) {
            cacheEnabled = getIntent().getBooleanExtra(PHConstants.INTENT_EXTRA_CACHE_ENABLE, false);
        }


        try {
            setContentView(R.layout.ph_activity_phmain);
        } catch (IllegalStateException ex) {
            setTheme(R.style.phtransparent_windowTitle_fix);
            setContentView(R.layout.ph_activity_phmain);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bottomSheet = findViewById(R.id.bottom_sheet);

        //bottomSheet.setMinimumHeight((int) (width * 0.5));

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setHideable(false);
        WebViewBottomSheetbehaviour.scrollEnabled = true;

        Serializable data = getIntent().getSerializableExtra(PHConstants.INTENT_EXTRA_DATA);
        bundle = new Bundle();
        bundle.putSerializable(PHConstants.INTENT_EXTRA_DATA, data);

        //  imgBack = findViewById(R.id.img_main_back);
        titleBar = bottomSheet.findViewById(R.id.bottom_sheet_hedder);
        txtTitle = findViewById(R.id.pay_with_text_title);

        titleBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
                    if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    else {
//                        if(isSaveCard)
//                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        else
//                            onBackClicked();

                    }
                }
                return true;
            }
        });

        final InitBaseRequest request = ((InitBaseRequest) data);
        if (request instanceof InitRequest) {
            InitRequest checkoutReq = (InitRequest) request;
            isSaveCard = !(checkoutReq.getDuration() == null || checkoutReq.getDuration().equals("") || checkoutReq.getRecurrence().equals(""));
        } else {
            if (request instanceof InitPreapprovalRequest) {
                request.setHoldOnCardEnabled(false);
                Log.d(TAG, "hold on card not effect for pre approval request,If request is InitPreapprovalRequest always hold on card disabled");
            }

            isSaveCard = true;
        }

        //Set Pay request base URL (SandBox or Live) per merchant ID
        String baseUrl = PHConfigs.LIVE_URL;
        if (request != null && request.getMerchantId() != null && !request.getMerchantId().equals("")) {
            if (request.getMerchantId().toCharArray()[0] == '1') {
                bottomSheet.findViewById(R.id.debug_value).setVisibility(View.VISIBLE);
                request.setSandBox(true);
                baseUrl = PHConfigs.SANDBOX_URL;
            } else if (request.getMerchantId().toCharArray()[0] == '2') {
                bottomSheet.findViewById(R.id.debug_value).setVisibility(View.GONE);
                baseUrl = PHConfigs.LIVE_URL;
            } else if (request.getMerchantId().toCharArray()[0] == '0')
                baseUrl = PHConfigs.LOCAL_URL;
        }
        PHConfigs.setBaseUrl(baseUrl);

        if (isSaveCard) {
//            String method = PHConfigs.BASE_URL.equals(PHConfigs.SANDBOX_URL) ? "TEST" : "VISA";
            String method = "VISA";
            setPayDetailsView("CREDIT / DEBIT CARD", method);
        } else
            setPayMethod();

        backView = findViewById(R.id.main_back);

        findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetBehavior.setPeekHeight(peekHeight, true);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

//                if (view == VIEW_METHOD || view == VIEW_DETAILS)
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                else if (view == VIEW_RESULT) {
//                    Intent intent = new Intent();
//                    intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, statusResponse);
//                    setResult(Activity.RESULT_CANCELED, intent);
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                } else if (view != VIEW_DETAILS) {
//                    setUserCanceledError();
//                }
            }
        });

//        findViewById(R.id.view_click_area).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackClicked();
//            }
//        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int i) {

                if (view == VIEW_METHOD && i == BottomSheetBehavior.STATE_COLLAPSED && !isMinimize) {
                    sheetBehavior.setPeekHeight(peekHeight, true);
                    isMinimize = true;
                }

//                else {
//                    if (isSaveCard && view == VIEW_DETAILS && i == BottomSheetBehavior.STATE_COLLAPSED)
//                        setUserCanceledError();
//                    else if (isHelapayPayment && view == VIEW_DETAILS && i == BottomSheetBehavior.STATE_COLLAPSED) {
//                        setUserCanceledError();
//                    } else if (view == VIEW_DETAILS && i == BottomSheetBehavior.STATE_COLLAPSED) {
//                        setUserCanceledError();
//                    } else if (view == VIEW_METHOD && i == BottomSheetBehavior.STATE_COLLAPSED) {
//                        setUserCanceledError();
//                    } else {
//
//                        if (i == BottomSheetBehavior.STATE_SETTLING && (PHMainActivity.this.view != VIEW_DETAILS || isSaveCard)) {
//                            if (prevState != BottomSheetBehavior.STATE_COLLAPSED) {
//                                int colorFrom = getResources().getColor(R.color.bottom_sheet_back);
//                                int colorTo = getResources().getColor(android.R.color.transparent);
//
//                                colorFadeAnimation(backView, colorFrom, colorTo, PHMainActivity.this.view == VIEW_METHOD || PHMainActivity.this.view == VIEW_RESULT);
//                            } else {
//                                int colorFrom = getResources().getColor(android.R.color.transparent);
//                                int colorTo = getResources().getColor(R.color.bottom_sheet_back);
//                                colorFadeAnimation(backView, colorFrom, colorTo, false);
//                            }
//                        }
//                    }
//
//                    prevState = i;
//                }

                Log.d(PHMainActivity.class.getSimpleName(), "Bottom Sheet state : " + i);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        titleBar.post(new Runnable() {
            @Override
            public void run() {
                peekHeight = titleBar.getHeight();
            }
        });

        findViewById(R.id.bottom_sheet_back_icon)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        peekHeight = 0;
                        onBackClicked();
//                        isMinimize = false;
                    }
                });


//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackClicked();
//            }
//        });

        PHConfigs.readMethods(this);
    }

    private void colorFadeAnimation(final View view, int colorFrom, int colorTo, final boolean isFinish) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFinish) {
                    setUserCanceledError();
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        colorAnimation.start();
    }

    private void onBackClicked() {
        if (view == VIEW_METHOD || view == VIEW_RESULT) {

//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            peekHeight = titleBar.getHeight();
            finishWithBack();

        } else {
            if (isSaveCard)
//                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                finishWithBack();
            else
                setPayMethod();
        }

//        switch (view) {
//            case VIEW_METHOD:
//            case VIEW_RESULT:
//                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                break;
//            case VIEW_DETAILS:
//                if (isSaveCard)
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                else
//                    setPayMethod();
//                break;
//        }
    }

    private void finishWithBack() {
        if (isSaveCard && view == VIEW_DETAILS)
            setUserCanceledError();
        else if (isHelapayPayment && view == VIEW_DETAILS) {
            setUserCanceledError();
        } else if (view == VIEW_DETAILS) {
            setUserCanceledError();
        } else if (view == VIEW_METHOD) {
            setUserCanceledError();
        } else {
//            if (prevState != BottomSheetBehavior.STATE_COLLAPSED) {
            int colorFrom = getResources().getColor(R.color.bottom_sheet_back);
            int colorTo = getResources().getColor(android.R.color.transparent);

            colorFadeAnimation(backView, colorFrom, colorTo, PHMainActivity.this.view == VIEW_METHOD || PHMainActivity.this.view == VIEW_RESULT);
//            } else {
//                int colorFrom = getResources().getColor(android.R.color.transparent);
//                int colorTo = getResources().getColor(R.color.bottom_sheet_back);
//                colorFadeAnimation(backView, colorFrom, colorTo, false);
//            }

        }
    }

    public void setCloseBottomSheet(boolean val) {
        this.closeBottomSheet = val;
    }

    public void setPayMethod() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewheight = bottomSheet.findViewById(R.id.frame_main_fragment_container).getMeasuredHeight();
//                    int titleBarHeight = titleBar.getMeasuredHeight();
//                sheetBehavior.setPeekHeight(peekHeight);
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }, 250);

        // imgBack.setVisibility(View.GONE);
        txtTitle.setText(getString(R.string.pay_with_text));
        findViewById(R.id.bottom_sheet_back_icon).setVisibility(View.VISIBLE);
        view = VIEW_METHOD;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentMethodFragment fragment = new PaymentMethodFragment();
        fragment.setArguments(bundle);
        listener = fragment;
        ft.replace(R.id.frame_main_fragment_container, fragment);
        ft.commit();

    }

    public void setPayDetailsView(String title, String method) {
        if (isSaveCard) {
            txtTitle.setText(getString(R.string.pay_with_card_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(View.VISIBLE);
            sheetBehavior.setPeekHeight(peekHeight);
            viewheight = (int) (Utils.screenHight(this) * 0.6);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }, 250);

        //  imgBack.setVisibility(View.VISIBLE);
        //  txtTitle.setText(getString(R.string.pay_with_text));

        view = VIEW_DETAILS;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentDetailFragment fragment = new PaymentDetailFragment();
        bundle.putString(PHConstants.INTENT_EXTRA_METHOD, method);
        bundle.putInt(PHConstants.INTENT_EXTRA_HEIGHT, viewheight);
        bundle.putBoolean(PHConstants.INTENT_EXTRA_AUTO, isSaveCard);


        fragment.setArguments(bundle);
        listener = fragment;
        ft.replace(R.id.frame_main_fragment_container, fragment);
        ft.commit();
    }

    /**
     * set details fragment for helapay method
     * set session time out time (60000*2)
     */
    public void setPayDetailsView(final NewInitResponse.PaymentMethod paymentMethod, final InitBaseRequest request, final String orderKey) {
        isHelapayPayment = false;
        view = VIEW_DETAILS;
        if (paymentMethod.getSubmissionCode().equals(PHConstants.SUMBITION_CODE_HELAPAY)) {
            isHelapayPayment = true;
            PayHereBaseActivity.DISCONNECT_TIMEOUT = DISCONNECT_TIMEOUT_HELAPAY;
            resetDisconnectTimer();
            openHelakuruIntent(paymentMethod.getSubmission().getUrl());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handlePaymentResponse(null, request, orderKey, true);
                }
            }, 5000);
            return;
        }

        viewheight = (int) (Utils.screenHight(this) * 0.6);
        if (isSaveCard) {
            sheetBehavior.setPeekHeight(peekHeight);
            // viewheight = (int) (Utils.screenHight(this) *0.6);
            Log.d(TAG, "view height " + viewheight);
            txtTitle.setText(getString(R.string.pay_with_card_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }, 250);

        //  imgBack.setVisibility(View.VISIBLE);

        if (Utils.getPayMethod(paymentMethod).equals(PHConstants.METHOD_CARD)) {
            txtTitle.setText(getString(R.string.pay_with_card_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(View.VISIBLE);
        } else if (Utils.getPayMethod(paymentMethod).equals(PHConstants.METHOD_OTHER)) {
            txtTitle.setText(getString(R.string.pay_with_other_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(View.VISIBLE);
        }

        view = VIEW_DETAILS;

        //set time out

        resetDisconnectTimer();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentDetailFragment fragment = new PaymentDetailFragment();
        bundle.putString(PHConstants.INTENT_EXTRA_METHOD, paymentMethod.getSubmissionCode());
        bundle.putInt(PHConstants.INTENT_EXTRA_HEIGHT, viewheight);
        bundle.putBoolean(PHConstants.INTENT_EXTRA_AUTO, isSaveCard);
        bundle.putSerializable(PHConstants.INTENT_EXTRA_HELA_PAY, paymentMethod.getSubmission());
        bundle.putString(PHConstants.INTENT_EXTRA_ORDER_KEY, orderKey);


        fragment.setArguments(bundle);
        listener = fragment;
        ft.replace(R.id.frame_main_fragment_container, fragment);
        ft.commit();
    }

    public void setPayResultView(final PHResponse<StatusResponse> statusResponse, final boolean isAuto, final boolean isHoldCard) {
        closeBottomSheet = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.bottom_sheet_back_icon).setVisibility(View.GONE);


                PHMainActivity.this.statusResponse = statusResponse;
                view = VIEW_RESULT;
                sheetBehavior.setSkipCollapsed(true);

                if (!skipResult) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewheight = bottomSheet.findViewById(R.id.frame_main_fragment_container).getMeasuredHeight();
                            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                        }
                    }, 250);
                    //  imgBack.setVisibility(View.GONE);


                    if (statusResponse.getData().getStatus() == 3 || statusResponse.getData().getStatus() == 2) {
                        if (isAuto)
                            txtTitle.setText(getString(R.string.label_save));
                        else
                            txtTitle.setText(getString(R.string.label_paid));
                    } else
                        txtTitle.setText(getString(R.string.label_declined));

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    PaymentResultFragment fragment = new PaymentResultFragment();
                    bundle.putLong(PHConstants.INTENT_EXTRA_REFERENCE, statusResponse != null ? statusResponse.getData().getPaymentNo() : 0L);
                    bundle.putBoolean(PHConstants.INTENT_EXTRA_AUTO, isAuto);
                    bundle.putBoolean(PHConstants.INTENT_EXTRA_HOLD, isHoldCard);
                    bundle.putInt(PHConstants.INTENT_EXTRA_STATUS, statusResponse != null ? statusResponse.getData().getStatus() : -2);
                    bundle.putString(PHConstants.INTENT_EXTRA_MESSAGE, statusResponse != null ? statusResponse.getData().getMessage() : "");
                    fragment.setArguments(bundle);
                    listener = fragment;
                    ft.replace(R.id.frame_main_fragment_container, fragment);
                    ft.commitAllowingStateLoss();
                    // ft.commit();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (closeBottomSheet)
                                goBackToApp();
                        }
                    }, 10000);
                } else
                    goBackToApp();


            }
        });


    }

    public void goBackToApp() {
//        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        finishWithBack();
    }

    private void setUserCanceledError() {
//        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Intent intent = new Intent();
        if (statusResponse != null && statusResponse.getData() != null && (statusResponse.getData()
                .getStatusState() == StatusResponse.Status.SUCCESS || statusResponse.getData()
                .getStatusState() == StatusResponse.Status.HOLD || statusResponse.getData()
                .getStatusState() == StatusResponse.Status.FAILED)) {

            intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, PHMainActivity.this.statusResponse);
            setResult(Activity.RESULT_OK, intent);
        } else {
            PHResponse response = new PHResponse(PHResponse.STATUS_ERROR_CANCELED, "User canceled the request");
            intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, response);
            setResult(Activity.RESULT_CANCELED, intent);

        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (listener != null)
            if (listener.onActivityBack()) {
                finishWithBack();
//                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

    }

//        this.methods = methods;
//    }
//
//    public HashMap<String, PaymentMethodResponse.Data> getMethods() {
//        return methods;//    public void setMethods(HashMap<String, PaymentMethodResponse.Data> methods) {
//        this.methods = methods;
//    }
//
//    public HashMap<String, PaymentMethodResponse.Data> getMethods() {
//        return methods;
//    }

    public void setMethods(HashMap<String, NewInitResponse.PaymentMethod> data) {
        this.methods = data;
    }

    public HashMap<String, NewInitResponse.PaymentMethod> getMethods() {
        return this.methods;
    }

    public interface OnActivityAction {
        boolean onActivityBack();
    }


    //check helapay success reponse
    private void checkHelapayStatus(final String orderKey, final InitBaseRequest request) {

        Executors.newSingleThreadExecutor()
                .execute(new Runnable() {
                    @Override
                    public void run() {

                        Map<String, String> statusParams = new HashMap<>();
                        statusParams.put("order_key", orderKey);
                        try {
                            String response = NetworkHandler.sendPost(PHConfigs.BASE_URL + PHConfigs.STATUS, statusParams);
                            Gson gson = new GsonBuilder().create();
                            StatusResponse statusResponse = gson.fromJson(response, StatusResponse.class);
                            if (statusResponse != null) {
                                System.out.println(statusResponse.toString());
                                handlePaymentResponse(statusResponse, request, orderKey, false);
                            }

                        } catch (Exception e) {
                            Log.d(TAG, "Unable to get order status", e);
                        }
                    }
                });
    }

    private void handlePaymentResponse(final StatusResponse statusResponse, final InitBaseRequest request, final String orderKey, final boolean force) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {


                if (force || statusResponse != null && statusResponse.getStatusState() == StatusResponse.Status.INIT
                        && !PHMainActivity.DISCONNECTED) {
                    checkHelapayStatus(orderKey, request);
                } else {
                    if (statusResponse.getStatusState() == StatusResponse.Status.SUCCESS) {
                        setPayResultView(new PHResponse(getStatusFromResponse(statusResponse), "Payment success. Check response data", statusResponse), request instanceof InitPreapprovalRequest, false);
                    } else if (statusResponse.getStatusState() == StatusResponse.Status.HOLD) {
                        setPayResultView(new PHResponse(getStatusFromResponse(statusResponse), "Payment success. Check response data", statusResponse), request instanceof InitPreapprovalRequest, true);
                    } else if (statusResponse.getStatusState() == StatusResponse.Status.FAILED) {
                        setError(new PHResponse(getStatusFromResponse(statusResponse), "Payment failed. Check response data", statusResponse), true);
//            } else if (postActivityFinish) {
//                String stage = statusResponse.getStatusState() == StatusResponse.Status.INIT ? "initial" : (statusResponse.getStatusState() == StatusResponse.Status.PAYMENT) ? "payment selection" : "unknown";
//                setError(new PHResponse(getStatusFromResponse(statusResponse), "Payment in " + stage + " stage. Check response data", statusResponse), true);
//            }
                    }
                }


            }
        }, 5000);


    }


    private void setError(final PHResponse response, boolean finish) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPayResultView(response, false, false);
            }
        });
//        Intent intent = new Intent();
//        intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, response);
//        setResult(Activity.RESULT_CANCELED, intent);
//        if (finish) {
//            //changed to finish
//            finish();
//        }
    }

    private int getStatusFromResponse(StatusResponse lastResponse) {
        return (lastResponse.getStatusState() == StatusResponse.Status.SUCCESS) ? PHResponse.STATUS_SUCCESS : PHResponse.STATUS_ERROR_PAYMENT;
    }


    private void openHelakuruIntent(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(browserIntent);
    }

}
