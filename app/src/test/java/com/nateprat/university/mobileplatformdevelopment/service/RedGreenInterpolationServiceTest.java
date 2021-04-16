package com.nateprat.university.mobileplatformdevelopment.service;

import android.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RedGreenInterpolationServiceTest {

    @Test
    public void colourAtPoint() {
        assertEquals(0, RedGreenInterpolationService.colourAtPoint(100));
    }

    @Test
    public void colourAtPointOutwith() {
        assertEquals(Color.MAGENTA, RedGreenInterpolationService.colourAtPoint(10000));
    }

}