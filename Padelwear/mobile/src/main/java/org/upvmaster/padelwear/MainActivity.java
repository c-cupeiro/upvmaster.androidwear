package org.upvmaster.padelwear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String MI_GRUPO_DE_NOTIFIC = "mi_grupo_de_notific";

    public static final String EXTRA_RESPUESTA_POR_VOZ = "extra_respuesta_por_voz";

    public static final String EXTRA_MESSAGE="com.example.notificaciones.EXTRA_MESSAGE";
    public static final String ACTION_DEMAND="com.example.notificaciones.ACTION_DEMAND";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Notificaciones
        Bundle respuesta = RemoteInput.getResultsFromIntent(getIntent());
        if (respuesta != null) {
            CharSequence texto = respuesta.getCharSequence(EXTRA_RESPUESTA_POR_VOZ);
            ((TextView) findViewById(R.id.textViewRespuesta)).setText(texto);
        }
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
                List<NotificationCompat.Action> acciones = new ArrayList<NotificationCompat.Action>();
                acciones.add(accion);
                acciones.add(new NotificationCompat.Action(R.mipmap.ic_action_locate, "Ver mapa", intencionPendienteMapa));

                String s = "Texto largo con descripción detallada de la notificación. ";


                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle segundaPg = new NotificationCompat.BigTextStyle();
                segundaPg.setBigContentTitle("Página 2").bigText("Más texto.");
                // Creamos una notification para la segunda página
                Notification notificacionPg2 = new NotificationCompat.Builder(MainActivity.this).setStyle(segundaPg).build();

                // Creamos un BigTextStyle para la segunda página
                NotificationCompat.BigTextStyle terceraPg = new NotificationCompat.BigTextStyle();
                terceraPg.setBigContentTitle("Página 3").bigText("Más texto aún.");
                // Creamos una notification para la segunda página
                Notification notificacionPg3 = new NotificationCompat.Builder(MainActivity.this).setStyle(terceraPg).build();
                List lista_paginas = new ArrayList();
                lista_paginas.add(notificacionPg2);
                lista_paginas.add(notificacionPg3);


                // Creamos un WearableExtender para añadir funcionalidades para wearable
                NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true).setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv)).addActions(acciones)
                        //.addPage(notificacionPg2);
                        .addPages(lista_paginas);

                int notificacionId = 001;
                Notification notificacion = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Título").setContentText("Notificación Android Wear")
                        .setContentIntent(intencionPendienteMapa)
                        .addAction(R.mipmap.ic_action_call, "llamar", intencionPendientetLlamar)
                        //.extend(new NotificationCompat.WearableExtender().addAction( accion))
                        //.extend(new NotificationCompat.WearableExtender().addActions(acciones))
                        //.setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.escudo_upv))
                        .extend(wearableExtender)
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(s + s + s + s))
                        .setGroup(MI_GRUPO_DE_NOTIFIC)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notificacionId, notificacion);

                int idNotificacion2 = 002;
                Notification notificacion2 = new NotificationCompat.Builder(MainActivity.this).setContentTitle("Nueva Conferencia")
                        .setContentText("Los neutrinos").setSmallIcon(R.mipmap.ic_action_mail_add)
                        .setGroup(MI_GRUPO_DE_NOTIFIC).build();
                notificationManager.notify(idNotificacion2, notificacion2);

                // Creamos una notificacion resumen
                int idNotificacion3 = 003;
                Notification notificacion3 = new NotificationCompat.Builder(MainActivity.this)
                        .setContentTitle("2 notificaciones UPV").setSmallIcon(R.mipmap.ic_action_attach)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.escudo_upv))
                        .setStyle(new NotificationCompat.InboxStyle().addLine("Nueva Conferencia Los neutrinos")
                                .addLine("Nuevo curso Android Wear").setBigContentTitle("2 notificaciones UPV")
                                .setSummaryText("info@upv.es")).setNumber(2)
                        .setGroup(MI_GRUPO_DE_NOTIFIC).setGroupSummary(true).build();
                notificationManager.notify(idNotificacion3, notificacion3);


            }
        });

        Button butonVoz = (Button) findViewById(R.id.boton_voz);
        butonVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Creamos una intención de respuesta
                Intent intencion = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent intencionPendiente = PendingIntent.getActivity(MainActivity.this, 0, intencion, PendingIntent.FLAG_UPDATE_CURRENT);
                // Creamos la entrada remota para añadirla a la acción
                String[] opcRespuesta = getResources().getStringArray(R.array.opciones_respuesta);
// Creamos la entrada remota para añadirla a la acción
                RemoteInput entradaRemota = new RemoteInput.Builder(EXTRA_RESPUESTA_POR_VOZ)
                        .setLabel("respuesta por voz")
                        .setChoices(opcRespuesta)
                        .build();
// Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action
                        .Builder(android.R.drawable.ic_menu_set_as, "responder", intencionPendiente)
                        .addRemoteInput(entradaRemota).build();
// Creamos la notificación
                int idNotificacion = 002;
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Respuesta por Voz")
                        .setContentText("Indica una respuesta")
                        .extend(new NotificationCompat.WearableExtender().addAction(accion));
// Lanzamos la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(idNotificacion, notificationBuilder.build());
            }

        });

        Button butonVozBroadcast = (Button) findViewById(R.id.boton_voz_broadcast);
        butonVozBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Creamos una intención de respuesta
                Intent intencion = new Intent(MainActivity.this, WearReceiver.class)
                        .putExtra(EXTRA_MESSAGE, "alguna información relevante")
                        .setAction(ACTION_DEMAND);
                PendingIntent intencionPendiente = PendingIntent.getBroadcast(MainActivity.this, 0, intencion, 0);
                // Creamos la entrada remota para añadirla a la acción
                String[] opcRespuesta = getResources().getStringArray(R.array.opciones_respuesta);
// Creamos la entrada remota para añadirla a la acción
                RemoteInput entradaRemota = new RemoteInput.Builder(EXTRA_RESPUESTA_POR_VOZ)
                        .setLabel("respuesta por voz")
                        .setChoices(opcRespuesta)
                        .build();
// Creamos la acción
                NotificationCompat.Action accion = new NotificationCompat.Action
                        .Builder(android.R.drawable.ic_menu_set_as, "responder", intencionPendiente)
                        .addRemoteInput(entradaRemota).build();
// Creamos la notificación
                int idNotificacion = 002;
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Respuesta por Voz")
                        .setContentText("Indica una respuesta")
                        .extend(new NotificationCompat.WearableExtender().addAction(accion));
// Lanzamos la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(idNotificacion, notificationBuilder.build());
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.accion_contador) {
            startActivity(new Intent(this, Contador.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
