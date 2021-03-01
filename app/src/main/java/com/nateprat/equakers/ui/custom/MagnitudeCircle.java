package com.nateprat.equakers.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.nateprat.equakers.service.RedGreenInterpolationService;
import com.nateprat.equakers.utils.TagUtils;

import okhttp3.internal.Version;

public class MagnitudeCircle {

    private final Button button;

    public MagnitudeCircle(Button button) {
        this.button = button;
    }

    @SuppressLint("SetTextI18n")
    public void setMagnitude(double magnitude) {
        Log.d(TagUtils.getTag(this), "Setting magnitude to " + magnitude + " for button id:" + button.getId());
        int point = (int)(magnitude * 10);
        int colour = RedGreenInterpolationService.colourAtPoint(point);
        setButtonColor(button, colour);
        button.setText(Double.toString(magnitude));
    }

    public void setMagnitude(String magnitude) {
        this.setMagnitude(Double.parseDouble(magnitude));
    }

    public void setButtonColor(Button button, int colour) {
        if (Build.VERSION.SDK_INT >= 29) {
            button.getBackground().setColorFilter(new BlendModeColorFilter(colour, BlendMode.MULTIPLY));
        } else {
            button.getBackground().setColorFilter(colour, PorterDuff.Mode.MULTIPLY);
        }
    }

}
