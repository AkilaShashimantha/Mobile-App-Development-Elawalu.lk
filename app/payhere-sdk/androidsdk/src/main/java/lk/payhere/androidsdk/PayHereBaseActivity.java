package lk.payhere.androidsdk;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

class PayHereBaseActivity extends AppCompatActivity {

    //constants
    public static final String TAG = PayHereBaseActivity.class.getSimpleName();
    public static long DISCONNECT_TIMEOUT = 60000 * 5; // 60 sec = 60 * 1000 ms
    public static final long DISCONNECT_TIMEOUT_HELAPAY = 600000;//600 (10 min) sec for time out

    public static boolean DISCONNECTED = false;

    //primary data
    private long backgroundTimer = System.currentTimeMillis();
    private boolean isBackground = false;
    private static boolean isDisabled = false;

    private final Handler disconnectHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });

    private final Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            if (isBackground)
                return;
            terminateSession();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        enableTimer();
    }

    private void terminateSession() {
        Log.d(TAG, "Session timer Finished");

        if (isDisabled)
            return;

        showDisconnectedDialog();
    }

    private void showDisconnectedDialog() {
        DISCONNECTED = true;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayHereBaseActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Session timeout");
        alertDialog.setMessage("Your session has expired");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
//                    finish();
                finishActivity();
            }
        });


        try {
            alertDialog.create().show();
        } catch (Exception ex) {
            Log.d(PayHereBaseActivity.class.getSimpleName(), ex.getMessage() != null ? ex.getMessage() : "Error on Session popup");
        }
    }

    public void resetDisconnectTimer() {

        if (isDisabled)
            return;

        Log.d(TAG, "Session timer Restarted");
        disconnectHandler.removeCallbacks(disconnectCallback);

        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        Log.d(TAG, "Session timer Stopped");
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

//        public void disableTimer() {
//            Log.d(TAG, "Session timer disabled");
//            isDisabled = true;
//            stopDisconnectTimer();
//        }

    public void enableTimer() {
        Log.d(TAG, "Session timer enabled");
        isDisabled = false;
        resetDisconnectTimer();
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        isBackground = false;
        if (System.currentTimeMillis() - backgroundTimer < DISCONNECT_TIMEOUT)
            resetDisconnectTimer();
        else
            terminateSession();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isBackground = true;
        backgroundTimer = System.currentTimeMillis();
        stopDisconnectTimer();

        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM != null && myKM.inKeyguardRestrictedInputMode()) {
            terminateSession();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  //on lock session timeout removed for testing
        if (!pm.isScreenOn())
            terminateSession();
    }


    private void finishActivity() {
        final PHResponse phResponse = new PHResponse(PHResponse.STATUS_SESSION_TIME_OUT, "session has expired", null);
        Intent intent = new Intent();
        intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, phResponse);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
