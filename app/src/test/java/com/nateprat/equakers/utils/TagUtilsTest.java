package com.nateprat.equakers.utils;

import junit.framework.TestCase;

public class TagUtilsTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGetTag() {
        String expected = "TagUtilsTest";
        assertEquals(expected, TagUtils.getTag(this));
    }
}