package com.mason.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {

    private val TAG = SmsReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "XXXXX> onReceive: intent.action : ${intent.action}")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                // 從intent中取出所有的簡訊
                val pdus = bundle.get("pdus") as Array<*>

                // 解析每一個簡訊
                for (i in pdus.indices) {
                    val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)

                    val messageBody = smsMessage.messageBody
                    val messageFrom = smsMessage.originatingAddress

                    Log.i(TAG, "XXXXX> onReceive: SMS: messageBody = $messageBody, messageFrom = $messageFrom" +
                            "\n displayMessageBody: ${smsMessage.displayMessageBody}  " +
                            ", displayOriginatingAddress: ${smsMessage.displayOriginatingAddress} " +
                            ", timestampMillis = ${smsMessage.timestampMillis}")

                }
            }
        }
    }
}
