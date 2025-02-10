package lk.payhere.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class MainActivity extends AppCompatActivity {

    private final static int PAYHERE_REQUEST = 11010;
    private final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
    }

    public void startClick(View view) {

//        if(((CheckBox)findViewById(R.id.m_code_check)).isChecked()){
//            startClickPreApprovalLive();
//            return;
//        }


      // InitPreapprovalRequest req = new InitPreapprovalRequest();

//        //Checkout
        InitBaseRequest req;
        if(view.getId() == R.id.btn_pre_approval && ((CheckBox)findViewById(R.id.m_code_check)).isChecked()){
            startClickPreApprovalLive();
        }
        else if(view.getId() == R.id.btn_pre_approval){
            sstartPreApprovalSandbox();
        }
        else if(view.getId() == R.id.button && ((CheckBox)findViewById(R.id.m_code_check)).isChecked()){
            startCheckoutLive();
        }

        else{
           startCheckoutSandbox();
        }


//       // req.setAmount(50.00);
//      // req.setHoldOnCardEnabled(true); //Hold on card
//
////        //recurring payment
////      InitRequest req = new InitRequest();
////        req.setRecurrence("1 Month");
////        req.setDuration("Forever");
////        req.setStartupFee(50);
////        req.setAmount(55.00);
//
//      //  Pre-Approval
//       // InitPreapprovalRequest req = new InitPreapprovalRequest();
//       req.setAmount(50.00);
//        //General Code
//     // req.setMerchantId("210251"); //LIVE 210251  215012
//
//        //if(((CheckBox)findViewById(R.id.m_code_check)).isChecked())
//            req.setMerchantId("1210251");
////        else
////            req.setMerchantId("1210251"); //SANDBOX
//
//        req.setOrderId("ABCDWXYZ");
//        req.setCurrency("LKR");
//        req.setItemsDescription("1 Greeting Card");
//        req.setCustom1("This is the custom 1 message");
//        req.setCustom2("This is the custom 2 message");
//        req.getCustomer().setFirstName("Nuwan");
//        req.getCustomer().setLastName("Kumara");
//        req.getCustomer().setEmail("sabrirauf@email.com");
//        req.getCustomer().setPhone("+94700000000");
//        req.getCustomer().getAddress().setAddress("No 43, Galle Road,");
//        req.getCustomer().getAddress().setCity("Colombo");
//        req.getCustomer().getAddress().setCountry("Sri Lanka");
//        req.getCustomer().getDeliveryAddress().setAddress("No 100, Kandy Road,");
//        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
//        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
//        req.getItems().add(new Item(null, "Greeting card", 1, 50.00));
//        req.getItems().add(new Item(null, "Birthday card", 2, 50.00));
//        req.setNotifyUrl("https://helakuru.lk/modules/echannel/api/2.0/Remote/paymentnotify");
//
//        Intent intent = new Intent(this, PHMainActivity.class);
//        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
//       // intent.putExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, true); //To skip the result view
//        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    private void startCheckoutSandbox() {

        InitRequest req = new InitRequest();
        req.setAmount(55.00);
        //General Code
        // req.setMerchantId("210251"); //LIVE 210251  215012

//        if(((CheckBox)findViewById(R.id.m_code_check)).isChecked())
//            req.setMerchantId("210251");
//        else
        req.setMerchantId("1225830"); //SANDBOX

        req.setOrderId("ABCDWXYZ");
        req.setCurrency("LKR");
        req.setItemsDescription("1 Greeting Card");
        req.setCustom1("This is the custom 1 message");
        req.setCustom2("This is the custom 2 message");
        req.getCustomer().setFirstName("Nuwan");
        req.getCustomer().setLastName("Kumara");
        req.getCustomer().setEmail("sabrirauf@email.com");
        req.getCustomer().setPhone("+94700000000");
        req.getCustomer().getAddress().setAddress("No 43, Galle Road,");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");
        req.getCustomer().getDeliveryAddress().setAddress("No 100, Kandy Road,");
        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Greeting card", 1, 50.00));
        req.getItems().add(new Item(null, "Birthday card", 2, 50.00));
        req.setNotifyUrl("https://helakuru.lk/modules/echannel/api/2.0/Remote/paymentnotify");

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        // intent.putExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, true); //To skip the result view
        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    private void startCheckoutLive() {
         InitRequest req = new InitRequest();
        req.setAmount(50.00);
        //General Code
        // req.setMerchantId("210251"); //LIVE 210251  215012

//        if(((CheckBox)findViewById(R.id.m_code_check)).isChecked())
//            req.setMerchantId("210251");
//        else
        req.setMerchantId("210251"); //SANDBOX

        req.setOrderId("ABCDWXYZ");
        req.setCurrency("LKR");
        req.setItemsDescription("1 Greeting Card");
        req.setCustom1("This is the custom 1 message");
        req.setCustom2("This is the custom 2 message");
        req.getCustomer().setFirstName("Nuwan");
        req.getCustomer().setLastName("Kumara");
        req.getCustomer().setEmail("sabrirauf@email.com");
        req.getCustomer().setPhone("+94700000000");
        req.getCustomer().getAddress().setAddress("No 43, Galle Road,");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");
        req.getCustomer().getDeliveryAddress().setAddress("No 100, Kandy Road,");
        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Greeting card", 1, 50.00));
        req.getItems().add(new Item(null, "Birthday card", 2, 50.00));
        req.setNotifyUrl("https://helakuru.lk/modules/echannel/api/2.0/Remote/paymentnotify");

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        intent.putExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, true); //To skip the result view
        intent.putExtra(PHConstants.INTENT_EXTRA_CACHE_ENABLE,true); // for enable webview cache set true default value false
        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    public void startClickPreApprovalLive() {

      //  InitBaseRequest req; //= new InitPreapprovalRequest();




        // req.setAmount(50.00);
        // req.setHoldOnCardEnabled(true); //Hold on card

//        //recurring payment
        // InitRequest req = new InitRequest();
//        req.setRecurrence("1 Month");
//        req.setDuration("Forever");
//        req.setStartupFee(50);
//        req.setAmount(55.00);

        //  Pre-Approval
         InitPreapprovalRequest req = new InitPreapprovalRequest();
        req.setAmount(50.00);
        //General Code
        // req.setMerchantId("210251"); //LIVE 210251  215012

//        if(((CheckBox)findViewById(R.id.m_code_check)).isChecked())
//            req.setMerchantId("210251");
//        else
            req.setMerchantId("210251"); //SANDBOX

        req.setOrderId("ABCDWXYZ");
        req.setCurrency("LKR");
        req.setItemsDescription("1 Greeting Card");
        req.setCustom1("This is the custom 1 message");
        req.setCustom2("This is the custom 2 message");
        req.getCustomer().setFirstName("Nuwan");
        req.getCustomer().setLastName("Kumara");
        req.getCustomer().setEmail("sabrirauf@email.com");
        req.getCustomer().setPhone("+94700000000");
        req.getCustomer().getAddress().setAddress("No 43, Galle Road,");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");
        req.getCustomer().getDeliveryAddress().setAddress("No 100, Kandy Road,");
        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Greeting card", 1, 50.00));
        req.getItems().add(new Item(null, "Birthday card", 2, 50.00));
        req.setNotifyUrl("https://helakuru.lk/modules/echannel/api/2.0/Remote/paymentnotify");

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        // intent.putExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, true); //To skip the result view
        startActivityForResult(intent, PAYHERE_REQUEST);
    }


    public void sstartPreApprovalSandbox() {

        //  InitBaseRequest req; //= new InitPreapprovalRequest();




        // req.setAmount(50.00);
        // req.setHoldOnCardEnabled(true); //Hold on card

//        //recurring payment
        // InitRequest req = new InitRequest();
//        req.setRecurrence("1 Month");
//        req.setDuration("Forever");
//        req.setStartupFee(50);
//        req.setAmount(55.00);

        //  Pre-Approval
        InitPreapprovalRequest req = new InitPreapprovalRequest();
        req.setAmount(50.00);
        //General Code
        // req.setMerchantId("210251"); //LIVE 210251  215012

//        if(((CheckBox)findViewById(R.id.m_code_check)).isChecked())
//            req.setMerchantId("210251");
//        else
        req.setMerchantId("1225830"); //SANDBOX

        req.setOrderId("ABCDWXYZ");
        req.setCurrency("LKR");
        req.setItemsDescription("1 Greeting Card");
        req.setCustom1("This is the custom 1 message");
        req.setCustom2("This is the custom 2 message");
        req.getCustomer().setFirstName("Nuwan");
        req.getCustomer().setLastName("Kumara");
        req.getCustomer().setEmail("sabrirauf@email.com");
        req.getCustomer().setPhone("+94700000000");
        req.getCustomer().getAddress().setAddress("No 43, Galle Road,");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");
        req.getCustomer().getDeliveryAddress().setAddress("No 100, Kandy Road,");
        req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
        req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
        req.getItems().add(new Item(null, "Greeting card", 1, 50.00));
        req.getItems().add(new Item(null, "Birthday card", 2, 50.00));
        req.setNotifyUrl("https://helakuru.lk/modules/echannel/api/2.0/Remote/paymentnotify");

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        // intent.putExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, true); //To skip the result view
        startActivityForResult(intent, PAYHERE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess())
                        msg = "Activity result:" + response.getData().toString();
                    else
                        msg = "Result:" + response.toString();
                else
                    msg = "Result: no response";
                Log.d(TAG, msg);
                textView.setText(msg);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null)
                    textView.setText(response.toString());
                else
                    textView.setText("User canceled the request");
            }
        }
    }
}
