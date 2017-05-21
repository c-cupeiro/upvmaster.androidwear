package notificaciones.example.com.notificaciones;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button wearButton = (Button) findViewById(R.id.boton1);
        wearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intención pendiente
                Intent intencionMapa = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=universidad+politecnica+valencia"));
                PendingIntent intencionPendienteMapa = PendingIntent.getActivity(MainActivity.this, 0, intencionMapa, 0);
                //Action llamar
                Intent intencionLlamar = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:555123456"));
                PendingIntent intencionPendientetLlamar = PendingIntent.getActivity(MainActivity.this, 0, intencionLlamar, 0);

                // Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action.Builder(R.mipmap.ic_action_call,
                        "llamar Wear", intencionPendientetLlamar).build();

                //Creamos una lista de acciones
                List<NotificationCompat.Action> acciones = new ArrayList<NotificationCompat.Action>(); acciones.add(accion);
                acciones.add(new NotificationCompat.Action(R.mipmap.ic_action_locate, "Ver mapa", intencionPendienteMapa));

                int notificacionId = 001;
                Notification notificacion = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Título").setContentText("Notificación Android Wear")
                        .setContentIntent(intencionPendienteMapa)
                        .addAction(R.mipmap.ic_action_call, "llamar", intencionPendientetLlamar)
                        //.extend(new NotificationCompat.WearableExtender().addAction( accion))
                        .extend(new NotificationCompat.WearableExtender().addActions(acciones))
                        .setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.escudo_upv))
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notificacionId, notificacion);
            }
        });
    }
}
