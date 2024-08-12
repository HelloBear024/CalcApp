package com.example.mycalc.UIDesignLogic;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class VibrationHelper {
    private Vibrator vibe;

    public VibrationHelper(Context context) {
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrateOnClick() {
        long[] pattern = {0, 10, 10, 10, 20};
        int[] amplitudes = {0, 70, 70, 60, 30};
        int repeatIdx = -1;

        if (vibe != null && vibe.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibe.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, repeatIdx));
            } else {
                vibe.vibrate(50);
            }
        } else {
            Log.e("VibrationHelper", "Vibrator not available or not initialized");
        }
    }
}
