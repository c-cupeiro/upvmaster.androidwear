package org.upvmaster.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {
    private static final String WEAR_ARRANCAR_ACTIVIDAD = "/arrancar_actividad";
    private GoogleApiClient apiClient;
    // Elementos a mostrar en la lista
    String[] elementos = {"Partida", "Terminar partida", "Historial", "Notificación", "Pasos", "Pulsaciones", "Terminar partida"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        WearableRecyclerView lista = (WearableRecyclerView) findViewById(R.id.lista);
        Adaptador adaptador = new Adaptador(this, elementos);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tag = (Integer) v.getTag();
                Log.i("ClickWear", "Pulsado tag -> " + tag);
                switch (tag) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, Contador.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, Confirmacion.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, Historial.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, Pasos.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, SwipeDismiss.class));
                        break;
                }
            }
        });
        lista.setAdapter(adaptador);
        lista.setCenterEdgeItems(true);
        lista.setLayoutManager(new MyChildLayoutManager(this));
        lista.setCircularScrollingGestureEnabled(true);
        lista.setScrollDegreesPerScreen(180);
        lista.setBezelWidth(0.5f);
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
}
