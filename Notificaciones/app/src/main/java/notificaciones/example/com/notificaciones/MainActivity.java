package notificaciones.example.com.notificaciones;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                int notificacionId = 001;
                Notification notificacion = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Título").setContentText("Notificación Android Wear")
                        .setContentIntent(intencionPendienteMapa)
                        .build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notificacionId, notificacion);
            }
        });
    }
}
