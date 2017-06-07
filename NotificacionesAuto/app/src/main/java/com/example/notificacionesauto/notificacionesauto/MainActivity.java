package com.example.notificacionesauto.notificacionesauto;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_RESPUESTA_POR_VOZ = "voice_reply_key";
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNotificaciones = (Button) findViewById(R.id.boton1);
        btnNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidcurso.com/"));
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                int thisConverstionID = 42;
                Intent msgHeardIntent = new Intent()
                        .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                        .setAction("com.example.notificacionesauto.notificacionesauto.MY_ACTION_MESSAGE_HEARD")
                        .putExtra("conversation_id", thisConverstionID);

                PendingIntent msgHeardPendingIntent = PendingIntent
                        .getBroadcast(getApplicationContext(),
                                thisConverstionID, msgHeardIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent msgReplyIntent = new Intent()
                        .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                        .setAction("com.example.android.notificacionesauto.notificacionesauto.MY_ACTION_MESSAGE_REPLY")
                        .putExtra("conversation_id", thisConverstionID);
                PendingIntent msgReplyPendingIntent = PendingIntent
                        .getBroadcast(getApplicationContext(),
                                thisConverstionID, msgReplyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                RemoteInput remoteInput = new RemoteInput
                        .Builder(EXTRA_RESPUESTA_POR_VOZ)
                        .setLabel("Respuesta por voz").build();

                String conversationName = "Android Curso";
                NotificationCompat.CarExtender.UnreadConversation.Builder unreadConvBuilder =
                        new NotificationCompat.CarExtender.UnreadConversation
                                .Builder(conversationName)
                                .setReadPendingIntent(msgHeardPendingIntent)
                                .setReplyAction(msgReplyPendingIntent, remoteInput);

                unreadConvBuilder.addMessage("Hola, ¿Cómo estás?")
                        .setLatestTimestamp(System.currentTimeMillis());

                NotificationCompat.Builder builder = new
                        NotificationCompat.Builder(MainActivity.this);
                builder.extend(new NotificationCompat.CarExtender()
                        .setUnreadConversation(unreadConvBuilder.build()));
                builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                builder.setContentTitle("Notificaciones Básicas");
                builder.setContentText("Aprendiendo a crear notificaciones para Android Auto!");
                builder.setSubText("Pulsa para ir a la página del curso.");
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        });
    }
}
