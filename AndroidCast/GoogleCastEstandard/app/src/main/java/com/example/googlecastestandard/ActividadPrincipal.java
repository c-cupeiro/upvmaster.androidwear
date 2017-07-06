package com.example.googlecastestandard;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

public class ActividadPrincipal extends AppCompatActivity {

    private CastSession mCastSession;
    private SessionManager mSessionManager;
    private Button videoButton;
    private Button pausaButton;
    private RemoteMediaClient remoteMediaClient;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CastContext castContext = CastContext.getSharedInstance(this);
        mSessionManager = castContext.getSessionManager();
        pausaButton = (Button) findViewById(R.id.btn_pausa);
        pausaButton.setOnClickListener(btnClickListener);
        videoButton = (Button) findViewById(R.id.btn_video);
        videoButton.setOnClickListener(btnClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(),
                menu, R.id.media_route_menu_item);
        return true;
    }

    private void setSessionStarted(boolean enabled) {
        videoButton.setEnabled(enabled);
        pausaButton.setEnabled(enabled);
    }

    private final SessionManagerListener mSessionManagerListener = new SessionManagerListenerImpl();

    private class SessionManagerListenerImpl implements SessionManagerListener {
        @Override
        public void onSessionStarted(Session session, String sessionId) {
            invalidateOptionsMenu();
            mCastSession = mSessionManager.getCurrentCastSession();
            setSessionStarted(true);
        }

        @Override
        public void onSessionResumed(Session session, boolean wasSuspended) {
            invalidateOptionsMenu();
            setSessionStarted(true);
        }

        @Override
        public void onSessionSuspended(Session session, int error) {
            setSessionStarted(false);
        }

        @Override
        public void onSessionStarting(Session session) {
        }

        @Override
        public void onSessionResuming(Session session, String sessionId) {
        }

        @Override
        public void onSessionStartFailed(Session session, int error) {
        }

        @Override
        public void onSessionResumeFailed(Session session, int error) {
        }

        @Override
        public void onSessionEnding(Session session) {
        }

        @Override
        public void onSessionEnded(Session session, int error) {
            setSessionStarted(false);
            finish();
        }
    }


    private final View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_pausa:
                    if (isPlaying) {
                        pausaButton.setText("Play");
                        remoteMediaClient.pause();
                        isPlaying = false;
                    } else {
                        pausaButton.setText("Pausa");
                        remoteMediaClient.play();
                        isPlaying = true;
                    }
                    break;
                case R.id.btn_video:
                    MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
                    movieMetadata.putString(MediaMetadata.KEY_TITLE, "Big Buck Bunny");
                    movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, "Demo Google Cast UPV");
                    movieMetadata.addImage(new WebImage(Uri
                            .parse("http://bbb3d.renderfarming.net/img/logo.png")));
                    MediaInfo mediaInfo = new MediaInfo
                            .Builder("http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_60fps_normal.mp4")
                            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                            .setContentType("videos/mp4").setMetadata(movieMetadata).build();
                    remoteMediaClient = mCastSession.getRemoteMediaClient();
                    remoteMediaClient.load(mediaInfo, true, 0);
                    break;
            }
        }
    };

}
