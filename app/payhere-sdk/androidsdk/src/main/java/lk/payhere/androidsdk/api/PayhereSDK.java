package lk.payhere.androidsdk.api;

import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentDetails;
import lk.payhere.androidsdk.model.PaymentInitRequest;
import lk.payhere.androidsdk.model.PaymentInitResponse;
import lk.payhere.androidsdk.model.PaymentInitResult;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PayhereSDK {

    @POST(PHConstants.URL_INIT_PAYMENT)
    Call<PaymentInitResult> initPayment(@Body PaymentDetails details);

    @GET(PHConstants.URL_PAYMENT_METHODS)
    Call<PaymentMethodResponse> getPaymentMethods();

    @POST(PHConstants.URL_INIT_PAYMENT_V2)
    Call<NewInitResponse> initPaymentV2(@Body PaymentInitRequest details);

    @GET
    Call<ResponseBody>getUrl(@Url String url);
}
