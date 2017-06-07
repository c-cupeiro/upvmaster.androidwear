package com.example.notificacionesauto.notificacionesauto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

public class MyMessageReplyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int thisConversationId = intent.getIntExtra("conversation_id", -1);
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            CharSequence replyText = remoteInput
                    .getCharSequence(MainActivity.EXTRA_RESPUESTA_POR_VOZ);
            Log.d("NotificacionesAuto",
                    "Respuesta a la conversación [" + thisConversationId + "]: " + replyText);
        }
    }
}
