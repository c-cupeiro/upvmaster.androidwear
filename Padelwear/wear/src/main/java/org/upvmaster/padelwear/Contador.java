package org.upvmaster.padelwear;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Carlos on 02/06/2017.
 */

public class Contador extends WearableActivity {

    private static final String WEAR_ARRANCAR_ACTIVIDAD = "/arrancar_actividad_movil";
    private GoogleApiClient apiClient;
    private org.upvmaster.comun.Partida partida;
    private TextView misPuntos, misJuegos, misSets, susPuntos, susJuegos, susSets, hora;
    private DismissOverlayView dismissOverlay;
    private Vibrator vibrador;
    private long[] vibrEntrada = {0l, 500};
    private long[] vibrDeshacer = {0l, 500, 500, 500};

    private Typeface fuenteNormal = Typeface.create("sans-serif", 0);
    private Typeface fuenteFina = Typeface.create("sans-serif-thin", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAmbientEnabled();
        setContentView(R.layout.contador);
        apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mandarMensaje(WEAR_ARRANCAR_ACTIVIDAD, "");
        partida = new org.upvmaster.comun.Partida();
        vibrador = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        misPuntos = (TextView) findViewById(R.id.misPuntos);
        susPuntos = (TextView) findViewById(R.id.susPuntos);
        misJuegos = (TextView) findViewById(R.id.misJuegos);
        susJuegos = (TextView) findViewById(R.id.susJuegos);
        misSets = (TextView) findViewById(R.id.misSets);
        susSets = (TextView) findViewById(R.id.susSets);
        hora = (TextView) findViewById(R.id.hora);
        dismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        dismissOverlay.setIntroText("Para salir de la aplicación, haz una pulsación larga");
        dismissOverlay.showIntroIfNecessary();
        actualizaNumeros();
        View fondo = findViewById(R.id.fondo);
        fondo.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new org.upvmaster.comun.DireccionesGestureDetector(Contador.this,
                    new org.upvmaster.comun.DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                        @Override
                        public boolean onArriba(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                            partida.rehacerPunto();
                            vibrador.vibrate(vibrDeshacer, -1);
                            actualizaNumeros();
                            return true;
                        }

                        @Override
                        public boolean onAbajo(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                            partida.deshacerPunto();
                            vibrador.vibrate(vibrDeshacer, -1);
                            actualizaNumeros();
                            return true;
                        }

                        public void onLongPress(MotionEvent evento) {
                            dismissOverlay.show();
                        }
                    });

            @Override
            public boolean onTouch(View v, MotionEvent evento) {
                detector.onTouchEvent(evento);
                return true;
            }
        });
        misPuntos.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new org.upvmaster.comun.DireccionesGestureDetector(Contador.this,
                    new org.upvmaster.comun.DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                        @Override
                        public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                            partida.puntoPara(true);
                            vibrador.vibrate(vibrEntrada, -1);
                            actualizaNumeros();
                            return true;
                        }

                        public void onLongPress(MotionEvent evento) {
                            dismissOverlay.show();
                        }
                    });

            @Override
            public boolean onTouch(View v, MotionEvent evento) {
                detector.onTouchEvent(evento);
                return true;
            }
        });
        susPuntos.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new org.upvmaster.comun.DireccionesGestureDetector(Contador.this,
                    new org.upvmaster.comun.DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                        @Override
                        public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                            partida.puntoPara(false);
                            vibrador.vibrate(vibrEntrada, -1);
                            actualizaNumeros();
                            return true;
                        }

                        public void onLongPress(MotionEvent evento) {
                            dismissOverlay.show();
                        }
                    });

            @Override
            public boolean onTouch(View v, MotionEvent evento) {
                detector.onTouchEvent(evento);
                return true;
            }
        });
    }

    void actualizaNumeros() {
        misPuntos.setText(partida.getMisPuntos());
        susPuntos.setText(partida.getSusPuntos());
        misJuegos.setText(partida.getMisJuegos());
        susJuegos.setText(partida.getSusJuegos());
        misSets.setText(partida.getMisSets());
        susSets.setText(partida.getSusSets());
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        hora.setVisibility(View.VISIBLE);

        misPuntos.setTypeface(fuenteFina);
        misPuntos.getPaint().setAntiAlias(false);
        misJuegos.setTypeface(fuenteFina);
        misJuegos.getPaint().setAntiAlias(false);
        misSets.setTypeface(fuenteFina);
        misSets.getPaint().setAntiAlias(false);
        //Suyos
        susPuntos.setTypeface(fuenteFina);
        susPuntos.getPaint().setAntiAlias(false);
        susJuegos.setTypeface(fuenteFina);
        susJuegos.getPaint().setAntiAlias(false);
        susSets.setTypeface(fuenteFina);
        susSets.getPaint().setAntiAlias(false);
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        hora.setVisibility(View.GONE);

        misPuntos.setTypeface(fuenteNormal);
        misPuntos.getPaint().setAntiAlias(true);
        misJuegos.setTypeface(fuenteNormal);
        misJuegos.getPaint().setAntiAlias(true);
        misSets.setTypeface(fuenteNormal);
        misSets.getPaint().setAntiAlias(true);
        //Suyos
        susPuntos.setTypeface(fuenteNormal);
        susPuntos.getPaint().setAntiAlias(true);
        susJuegos.setTypeface(fuenteNormal);
        susJuegos.getPaint().setAntiAlias(true);
        susSets.setTypeface(fuenteNormal);
        susSets.getPaint().setAntiAlias(true);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        // Actualizar contenido
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        hora.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
        super.onStop();
    }

    private void mandarMensaje(final String path, final String texto) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodos = Wearable.NodeApi.getConnectedNodes(apiClient).await();
                for (Node nodo : nodos.getNodes()) {
                    Wearable.MessageApi.sendMessage(apiClient, nodo.getId(), path, texto.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult resultado) {
                            if (!resultado.getStatus().isSuccess()) {
                                Log.e("sincronizacion", "Error al mandar mensaje. Código:" + resultado.getStatus().getStatusCode());
                            }
                        }
                    });
                }
            }
        }).start();
    }

}
