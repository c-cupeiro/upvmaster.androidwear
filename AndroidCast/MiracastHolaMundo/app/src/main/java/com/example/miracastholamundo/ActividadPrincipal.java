package com.example.miracastholamundo;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActividadPrincipal extends Activity {

    private DisplayManager mDisplayManager;
    private DisplayListAdapter mDisplayListAdapter;
    private ListView mListView;
    private final SparseArray<RemotePresentation> mActivePresentations = new SparseArray<RemotePresentation>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        mDisplayListAdapter = new DisplayListAdapter(this);
        mListView = (ListView) findViewById(R.id.display_list);
        mListView.setAdapter(mDisplayListAdapter);
    }

    protected void onResume() {
        super.onResume();
        mDisplayListAdapter.updateContents();
        mDisplayManager.registerDisplayListener(mDisplayListener, null);
    }

    private void showPresentation(Display display) {
        RemotePresentation presentation = new RemotePresentation(this, display);
        mActivePresentations.put(display.getDisplayId(), presentation);
        presentation.show();
    }

    private void hidePresentation(Display display) {
        final int displayId = display.getDisplayId();
        RemotePresentation presentation = mActivePresentations.get(displayId);
        if (presentation == null) {
            return;
        }
        presentation.dismiss();
        mActivePresentations.delete(displayId);
    }

    private final class RemotePresentation extends Presentation {
        public RemotePresentation(Context context, Display display) {
            super(context, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.remoto);
        }
    }

    private final class DisplayListAdapter extends ArrayAdapter<Display> {
        final Context mContext;
        private CompoundButton.OnCheckedChangeListener mCheckedRemoteDisplay = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                synchronized (mCheckedRemoteDisplay) {
                    final Display display = (Display) view.getTag();
                    if (isChecked) {
                        showPresentation(display);
                    } else {
                        hidePresentation(display);
                    }
                }
            }
        };

        public DisplayListAdapter(Context context) {
            super(context, R.layout.elemento_lista);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View v;
            if (convertView == null) {
                v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.elemento_lista, null);
            } else {
                v = convertView;
            }
            final Display display = getItem(position);
            TextView tv = (TextView) v.findViewById(R.id.display_id);
            tv.setText(display.getName() + "(ID: " + display.getDisplayId() + ")");
            CheckBox cb = (CheckBox) v.findViewById(R.id.display_cb);
            cb.setTag(display);
            cb.setOnCheckedChangeListener(mCheckedRemoteDisplay);
            return v;
        }

        public void updateContents() {
            clear();
            Display[] displays = mDisplayManager.getDisplays();
            addAll(displays);
        }
    }

    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
            mDisplayListAdapter.updateContents();
        }

        @Override
        public void onDisplayChanged(int displayId) {
            mDisplayListAdapter.updateContents();
        }

        @Override
        public void onDisplayRemoved(int displayId) {
            mDisplayListAdapter.updateContents();
        }
    };
}
