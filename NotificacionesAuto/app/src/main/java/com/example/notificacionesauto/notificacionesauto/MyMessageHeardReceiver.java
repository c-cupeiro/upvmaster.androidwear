package com.example.notificacionesauto.notificacionesauto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyMessageHeardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int thisConversationId = intent.getIntExtra("conversation_id", -1);
        Log.d("NotificacionesAuto", "Se ha leído la conversación con id=" + thisConversationId);
    }
}
