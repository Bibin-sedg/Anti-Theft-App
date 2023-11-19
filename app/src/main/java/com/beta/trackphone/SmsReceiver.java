package com.beta.trackphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        // PDU: “protocol data unit”
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0; i<pdus.length; i++){

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();



            String messageBody = smsMessage.getMessageBody();
            messageBody=messageBody.trim().toLowerCase();
            messageBody=messageBody.replaceAll("\\s", "");

            if(messageBody.equals("locatephone")) {
                mListener.messageReceived(sender);
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}