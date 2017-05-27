package org.upvmaster.padelwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.SwipeDismissFrameLayout;

/**
 * Created by Carlos on 27/05/2017.
 */

public class SwipeDismiss extends Activity {

    Activity actividad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_dismiss);
        actividad = this;
        SwipeDismissFrameLayout root = (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root);
        root.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                actividad.finish();
            }
        });
    }
}
