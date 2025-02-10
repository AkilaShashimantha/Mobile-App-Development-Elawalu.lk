package lk.payhere.androidsdk.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentInitResponse;

public class Utils {

    public static int screenHight(Activity context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static String getPayMethod(NewInitResponse.PaymentMethod method){
        if(method.getMethod().toUpperCase().equals("HELA_PAY"))
            return PHConstants.METHOD_BANK;
        else if(method.getSubmissionCode().toUpperCase().equals("MASTER") || method.getSubmissionCode().toUpperCase().equals("VISA")
                || method.getSubmissionCode().toUpperCase().equals("AMEX"))
            return PHConstants.METHOD_CARD;
        else
            return PHConstants.METHOD_OTHER;
    }
}
