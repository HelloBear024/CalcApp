package com.example.mycalc.UIDesignLogic;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ButtonUtility {

    private final Vibrator vibe;

    public ButtonUtility(Context context) {
        this.vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setButtonListenerWithResize(Button button, final String number, final Runnable onClick, final float pressedTextSize, final float releasedTextSize) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    button.setTextSize(pressedTextSize);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    button.setTextSize(releasedTextSize);
                    break;
            }
            return false;
        });

        button.setOnClickListener(v -> {
            onClick.run();
            vibrateOnClick();
        });
    }

    public void setImageTouchListener(final ImageView imageView, final Runnable onClick) {
        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    imageView.setScaleX(0.9f);
                    imageView.setScaleY(0.9f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    imageView.setScaleX(1.0f);
                    imageView.setScaleY(1.0f);
                    break;
            }
            return false;
        });

        imageView.setOnClickListener(v -> {
            onClick.run();
            vibrateOnClick();
        });
    }

    private void vibrateOnClick() {
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
            Log.e("ButtonUtility", "Vibrator not available or not initialized");
        }
    }
}

