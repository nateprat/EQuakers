package com.nateprat.university.mobileplatformdevelopment.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nateprat.mobileplatformdevelopment.R;
import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;
import com.nateprat.university.mobileplatformdevelopment.service.RedGreenInterpolationService;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.graphics.PorterDuff.Mode.MULTIPLY;

public class MagnitudeCircle {

    private final Context context;
    private final Button button;

    public MagnitudeCircle(Context context, Button button) {
        this.context = context;
        this.button = button;
    }

    public MagnitudeCircle(Button button) {
        this(null, button);
    }

    @SuppressLint("SetTextI18n")
    public void setMagnitude(double magnitude) {
//        Log.d(TagUtils.getTag(this), "Setting magnitude to " + magnitude + " for button id:" + button.getId());
        int point = (int)(magnitude * 10);
        int colour = RedGreenInterpolationService.colourAtPoint(point);
        setButtonColor(button, colour);
        button.setText(Double.toString(magnitude));
        if (context != null) {
            button.setOnClickListener(vibrateOnClick(magnitude));
        }
    }

    public void setMagnitude(String magnitude) {
        this.setMagnitude(Double.parseDouble(magnitude));
    }

    public void setButtonColor(Button button, int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            button.getBackground().setColorFilter(new BlendModeColorFilter(colour, BlendMode.MULTIPLY));
            button.setBackgroundColor(colour);
        } else {
            button.getBackground().setColorFilter(colour, MULTIPLY);
        }
    }

    public View.OnClickListener vibrateOnClick(double magnitude) {
        return v -> {
            ThreadPools.getInstance().submitTask(() -> {
                long duration = (long) (magnitude * 100); // 1.5ML * 100 = 150ms
                Log.d(TagUtils.getTag(this), "Magnitude Button - onClick - Vibrating for " + duration + " ms");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int amplitude = (int)((magnitude / 10) * 255);
                    ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(duration, amplitude));
                } else {
                    ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(duration);
                }
            });
        };
    }

}
