/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elianshang.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Manages beeps and vibrations.
 */
public final class SoundManager implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, Closeable {

    private static final String TAG = SoundManager.class.getSimpleName();

    private static final float BEEP_VOLUME = 1.0f;
    private static final long VIBRATE_DURATION = 500L;

    private final Context context;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;

    private boolean beepEnabled = true;
    private boolean vibrateEnabled = false;

    private int rawId;

    public SoundManager(Context context, int rawId) {
        this.context = context;
        this.mediaPlayer = null;
        this.rawId = rawId;
        updatePrefs(rawId);
    }

    public boolean isBeepEnabled() {
        return beepEnabled;
    }

    /**
     * Call updatePrefs() after setting this.
     * <p>
     * If the device is in silent mode, it will not beep.
     *
     * @param beepEnabled true to enable beep
     */
    public void setBeepEnabled(boolean beepEnabled) {
        this.beepEnabled = beepEnabled;
    }

    public boolean isVibrateEnabled() {
        return vibrateEnabled;
    }

    /**
     * Call updatePrefs() after setting this.
     *
     * @param vibrateEnabled true to enable vibrate
     */
    public void setVibrateEnabled(boolean vibrateEnabled) {
        this.vibrateEnabled = vibrateEnabled;
    }

    public synchronized void updatePrefs(int rawId) {
        playBeep = shouldBeep(beepEnabled, context);
        if (playBeep && mediaPlayer == null) {

            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                if (audioManager.getMode() == AudioManager.MODE_INVALID) {
                    audioManager.setMode(AudioManager.MODE_NORMAL);

                    int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max / 2, 0);
                }
            }
            mediaPlayer = buildMediaPlayer(context, rawId);
        }
    }

    public synchronized void openSoundControl(Activity activity) {
        if (context instanceof Activity) {
            ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    public synchronized void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrateEnabled) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private static boolean shouldBeep(boolean beep, Context activity) {
        boolean shouldPlayBeep = beep;
        if (shouldPlayBeep) {
            // See if sound settings overrides this
            AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//                shouldPlayBeep = false;
            }
        }
        return shouldPlayBeep;
    }

    private MediaPlayer buildMediaPlayer(Context activity, int rawId) {
        if (rawId < 0) {
            return null;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        try {
            AssetFileDescriptor file = activity.getResources().openRawResourceFd(rawId);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            } finally {
                file.close();
            }
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer.release();
            return null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // When the beep has finished playing, rewind to queue up another one.
        mp.seekTo(0);
    }

    @Override
    public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            // we are finished, so put up an appropriate error toast if required and finish
//            context.finish();
        } else {
            // possibly media player error, so release and recreate
            mp.release();
            mediaPlayer = null;
            updatePrefs(rawId);
        }
        return true;
    }

    @Override
    public synchronized void close() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
