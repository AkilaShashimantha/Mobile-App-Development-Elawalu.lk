package lk.payhere.androidsdk.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.payhere.androidsdk.BuildConfig;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.R;
import lk.payhere.androidsdk.adapter.MethodAdapter;
import lk.payhere.androidsdk.api.PayhereSDK;
import lk.payhere.androidsdk.api.ServiceGenerator;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentInitRequest;
import lk.payhere.androidsdk.model.PaymentInitResponse;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import lk.payhere.androidsdk.util.ListItemDecore;
import lk.payhere.androidsdk.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodFragment extends Fragment implements PHMainActivity.OnActivityAction,
MethodAdapter.OnPaymentMethodClick{

    private String TAG = PaymentMethodFragment.class.getSimpleName();
    private InitBaseRequest request;

    private PHMainActivity activity;

    private RecyclerView cardPaymentList,otherPaymentListView;
    private ImageView progressBar;
    private View view;

    //adapters
    private MethodAdapter cardAdapter;
    private MethodAdapter otherMethodAdapter;

    //data instance
    private final List<NewInitResponse.PaymentMethod> cardList = new ArrayList<>();
    private final List<NewInitResponse.PaymentMethod> otherPaymentList = new ArrayList<>();
    private NewInitResponse.PaymentMethod helaPayMethod;
    private String orderKey;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            request = (InitBaseRequest) getArguments().getSerializable(PHConstants.INTENT_EXTRA_DATA);
        }
        else
            Log.d(TAG,"Argument is null");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        final int screenWith = Utils.screenHight(activity);

        view = inflater.inflate(R.layout.ph_fragment_payment_method, parent, false);

        NestedScrollView scrollView = view.findViewById(R.id.scroller_view);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
        params.height =(int) (screenWith * 0.5);


        scrollView.setLayoutParams(params);

        cardPaymentList = view.findViewById(R.id.card_payment_list);
        otherPaymentListView = view.findViewById(R.id.other_payment_list);
        progressBar = view.findViewById(R.id.progress_view);

//
        Glide.with(activity).asGif()
                .load(R.drawable.spinner_circle).into(progressBar);

          createMethodAdapters();
          initRequest(request);
        getPaymentmethods();
//


        view.findViewById(R.id.helapay_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPayDetailsView(helaPayMethod,request, orderKey);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (PHMainActivity) getActivity();
    }


    @Override
    public boolean onActivityBack() {
        return true;
    }

    private static int getWidth(Context mContext) {
        int width;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();

        return width;
    }

    private static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void setImageSize(ImageView imgView, int size) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imgView.getLayoutParams();
        params.width = size;
        params.height = size;
        imgView.setLayoutParams(params);
    }

    private void getPaymentmethods() {
        ServiceGenerator.createService(activity, PayhereSDK.class).getPaymentMethods().enqueue(new Callback<PaymentMethodResponse>() {
            @Override
            public void onResponse(Call<PaymentMethodResponse> call, Response<PaymentMethodResponse> response) {
                PaymentMethodResponse result = response.body();
            }

            @Override
            public void onFailure(Call<PaymentMethodResponse> call, Throwable t) {
            }
        });
    }

    private PaymentInitRequest getPaymentDetails(){
        final PaymentInitRequest paymentDetails = new PaymentInitRequest();
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

            //  paymentDetails.setMethod(method);
            paymentDetails.setAuto(request instanceof InitPreapprovalRequest);
//            paymentDetails.setAuthorize(request.isHoldOnCardEnabled());

        }

        return paymentDetails;
    }

    public void initRequest(InitBaseRequest initBaseRequest){

        ServiceGenerator.createService(activity, PayhereSDK.class).initPaymentV2(getPaymentDetails())
                .enqueue(new Callback<NewInitResponse>() {
                    @Override
                    public void onResponse(Call<NewInitResponse> call, Response<NewInitResponse> response) {
                        if(response.body() != null && response.body().getStatus() == 1) {
                            Log.d(TAG, "Init Payment method success");
                            readInitResponse(response.body().getData());
                        }
                        else {
                            String errorMessage = "Init Payment method call failed ";
                            if(response.body() != null && response.body().getMsg() != null && !response.body().getMsg().equals("") )
                                errorMessage = response.body().getMsg();

                            Log.d(TAG,"Init Payment method call failed ");
                            setError( new PHResponse(PHResponse.STATUS_ERROR_UNKNOWN,errorMessage,null), true);
                        }
                    }

                    @Override
                    public void onFailure(Call<NewInitResponse> call, Throwable t) {
                        Log.d(TAG,"Init payment method call throw exception " + t.getMessage());
                        String errorMessage = "Init Payment method call failed ";

                        if(t.getMessage() != null && !t.getMessage().equals(""))
                            errorMessage = "payment init exception : " + t.getMessage();


                        setError(new PHResponse(PHResponse.STATUS_ERROR_UNKNOWN,errorMessage,null),true);
                    }
                });


//        ServiceGenerator.createService(activity, PayhereSDK.class).initPaymentV2(getPaymentDetails())
//                .enqueue(new Callback<PaymentInitResponse>() {
//                    @Override
//                    public void onResponse(Call<PaymentInitResponse> call, Response<PaymentInitResponse> response) {
//                        if(response.body() != null && response.body().getStatus() == 1){
//                            Log.d(TAG,"Init Payment method success");
//                            readInitResponse(response.body().getData());
//                        }
//                        else {
//                            Log.d(TAG,"Init Payment method call failed ");
//                            setError(null, true);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<PaymentInitResponse> call, Throwable t) {
//                        Log.d(TAG,"Init payment method call throw exception " + t.getMessage());
//                        setError(null,true);
//                    }
//                });
    }


    private void readInitResponse(NewInitResponse.Data data) {

        if(data.getPaymentMethods() != null && !data.getPaymentMethods().isEmpty()){
            otherPaymentList.clear();
            cardList.clear();

            orderKey = data.getOrder().getOrderKey();
            HashMap<String,NewInitResponse.PaymentMethod> methods = new HashMap<>();
            for(NewInitResponse.PaymentMethod method : data.getPaymentMethods()){
                methods.put(method.getMethod(),method);
                if(method.getSubmissionCode().toUpperCase().equals("MASTER") || method.getSubmissionCode().toUpperCase().equals("VISA")
                        || method.getSubmissionCode().toUpperCase().equals("AMEX")){
                    cardList.add(method);

                }
                else if(method.getSubmissionCode().toUpperCase().equals("HELAPAY")){
                    helaPayMethod = method;
                    //helaPayMethod.getSubmission().setUrl("https://www.helapay.lk/p1/ABv7GRNh/Bhasha%20Lanka%20(Pvt)/5000/aa2");
                   // helaPayMethod.getSubmission().setUrl("https://hela.page.link/?link=https://www.helapay.lk/p2/O7wnYxuoy/Gunasena/372900/02k&apn=lk.bhasha.helakuru&afl=https://www.helapay.lk/p2/O7wanYxuoy/Gunasena/372900/02k");
                }
                else{
                   otherPaymentList.add(method);
                }
            }
            activity.setMethods(methods);
            updateView();
        }
    }

    private void updateView(){
        progressBar.setVisibility(View.GONE);
       // view.findViewById(R.id.paywith_header).setVisibility(View.VISIBLE);
        if(helaPayMethod == null){
            view.findViewById(R.id.helapay_image).setVisibility(View.GONE);
            view.findViewById(R.id.bank_account_subtitle_txt).setVisibility(View.GONE);
        }
        else{
            view.findViewById(R.id.helapay_image).setVisibility(View.VISIBLE);
            view.findViewById(R.id.bank_account_subtitle_txt).setVisibility(View.VISIBLE);
        }

        if(cardList.isEmpty()){
            view.findViewById(R.id.card_payment_subtitle_txt).setVisibility(View.GONE);
            view.findViewById(R.id.card_payment_list).setVisibility(View.GONE);
        }
        else{
            view.findViewById(R.id.card_payment_subtitle_txt).setVisibility(View.VISIBLE);
            view.findViewById(R.id.card_payment_list).setVisibility(View.VISIBLE);
            cardAdapter.notifyDataSetChanged();
        }

        if(otherPaymentList.isEmpty()){
            view.findViewById(R.id.other_payment_subtitle_txt).setVisibility(View.GONE);
            view.findViewById(R.id.other_payment_list).setVisibility(View.GONE);
        }
        else{
            view.findViewById(R.id.other_payment_subtitle_txt).setVisibility(View.VISIBLE);
            view.findViewById(R.id.other_payment_list).setVisibility(View.VISIBLE);
            otherMethodAdapter.notifyDataSetChanged();
        }


    }

    private void createMethodAdapters(){
        cardAdapter = new MethodAdapter(activity,cardList);
        otherMethodAdapter = new MethodAdapter(activity,otherPaymentList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false);

        cardPaymentList.setLayoutManager(layoutManager);
        otherPaymentListView.setLayoutManager(layoutManager1);

        cardPaymentList.addItemDecoration(new ListItemDecore(15));
        otherPaymentListView.addItemDecoration(new ListItemDecore(15));

        cardPaymentList.setAdapter(cardAdapter);
        otherPaymentListView.setAdapter(otherMethodAdapter);

        cardAdapter.setOnPaymentMethodClick(this);
        otherMethodAdapter.setOnPaymentMethodClick(this);

    }

    private void setError(PHResponse response, boolean finish) {
        Intent intent = new Intent();
        intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, response);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        if (finish) {
            //changed to finish
            activity.finish();
        }
    }

    @Override
    public void onclick(NewInitResponse.PaymentMethod paymentMethod) {
        activity.setPayDetailsView(paymentMethod,request, orderKey);
    }
}
