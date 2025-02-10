package lk.payhere.androidsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.R;

public class PaymentResultFragment extends Fragment implements PHMainActivity.OnActivityAction {

    //primary data
    private int sec = 5;

    private PHMainActivity activity;
    private boolean isAuto = false;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PHMainActivity) getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (PHMainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        long refNo = 0L;

        boolean isHoldOnCard = false;
        int status = 0;
        String message = null;
        if (getArguments() != null) {
            refNo = getArguments().getLong(PHConstants.INTENT_EXTRA_REFERENCE);
            isAuto = getArguments().getBoolean(PHConstants.INTENT_EXTRA_AUTO);
            isHoldOnCard = getArguments().getBoolean(PHConstants.INTENT_EXTRA_HOLD);
            status = getArguments().getInt(PHConstants.INTENT_EXTRA_STATUS);
            message = getArguments().getString(PHConstants.INTENT_EXTRA_MESSAGE);
        }

        final View view;
     //   if (!isAuto)
            view = inflater.inflate(R.layout.ph_fragment_payment_result, parent, false);
    //    else
     //       view = inflater.inflate(R.layout.ph_fragment_pre_approval_result, parent, false);




        TextView txtReference = view.findViewById(R.id.txt_result_id);
        TextView txtMessage = view.findViewById(R.id.txt_result_message);
        final TextView txtClose = view.findViewById(R.id.txt_result_close);
        TextView retryBtn = view.findViewById(R.id.retry_btn);
        TextView paymentStatusTxt = view.findViewById(R.id.txt_result_approved);
      //  TextView paymentIdTxt = view.findViewById(R.id.txt_result_approved);

        if(status == 3 || status == 2){
            view.findViewById(R.id.img_result_done).setVisibility(View.VISIBLE);
          //  ((TextView) view.findViewById(R.id.pay_with_text_title)).setText(R.string.label_paid);

            if(isAuto)
                paymentStatusTxt.setText(R.string.label_saved_message);
            else
                paymentStatusTxt.setText(R.string.label_payment_approved);
//            String pId = "Payment ID #"+refNo;
//            paymentIdTxt.setText(pId);

            String refText;
            if (!isAuto) {
                refText = getString(R.string.payment_reference);
                paymentStatusTxt.setText(R.string.label_payment_approved);
            }
            else {
                refText = getString(R.string.auto_reference);
                paymentStatusTxt.setText(R.string.label_saved_message);
            }

            txtReference.setText(String.format(Locale.getDefault(), refText, String.valueOf(refNo)));

            if(isHoldOnCard)
                txtMessage.setText(R.string.hold_card_success_msg);
            else if(isAuto)
                 txtMessage.setText(R.string.reference_message);
            else
                 txtMessage.setText(R.string.payment_message);

            txtReference.setTextColor(getResources().getColor(R.color.done_text_color));
        }
        else {
           // view.findViewById(R.id.img_result_done).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_result_done))
                    .setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.payment_declined_image));

            txtMessage.setText(R.string.decline_description_label);

            paymentStatusTxt.setText(R.string.label_declin_message);


            if(message != null && !message.equals("")){
                txtReference.setText(String.format("Reason: %s",message));
                txtReference.setTextColor(getResources().getColor(R.color.color_red));
            }
            else{
                txtReference.setText("Payment Error");
            }

            retryBtn.setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.done_txt)).setText("Cancel");

        }


        txtClose.setText(String.format(Locale.getDefault(), getString(R.string.window_close_msg), sec--));

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            txtClose.setText(String.format(Locale.getDefault(), getString(R.string.window_close_msg), sec--));
//                            if (sec > 0)
//                                handler.postDelayed(this, 5000);
//                        } catch (IllegalStateException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }, 5000);


        view.findViewById(R.id.done_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goBackToApp();
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               handleRetry();
            }
        });

        return view;
    }

    private void handleRetry() {
        activity.setCloseBottomSheet(false);
        if(isAuto)
        {
            String method = "VISA";
            activity.setPayDetailsView("CREDIT / DEBIT CARD", method);
        }
        else
            activity.setPayMethod();
    }

    @Override
    public boolean onActivityBack() {
        return true;
    }
}
