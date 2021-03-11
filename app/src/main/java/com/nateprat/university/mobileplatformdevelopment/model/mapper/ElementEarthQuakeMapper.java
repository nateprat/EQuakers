package com.nateprat.university.mobileplatformdevelopment.model.mapper;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nateprat.university.mobileplatformdevelopment.model.Earthquake;
import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.Location;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ElementEarthQuakeMapper implements Mapper<Element, EarthquakeRecord> {

    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private static final String deliminator = " ; ";

    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";
    private static final String PUB_DATE = "pubDate";
    private static final String CATEGORY = "category";
    private static final String GEO_LAT = "geo:lat";
    private static final String GEO_LONG = "geo:long";

    private static final String dDateTag = "Origin date/time";
    private static final String dLocationTag = "Location";
    private static final String dGeoTag = "Lat/long";
    private static final String dDepthTag = "Depth";
    private static final String dMagnitudeTag = "Magnitude";

    @Override
    public EarthquakeRecord map(Element obj) {
        Log.d(TagUtils.getTag(this), "Mapping object " + obj.getNodeName() + " to an EarthquakeRecord object.");
        EarthquakeRecord record;
        // try to create EarthquakeRecord object
        try {
            String desc = getTagValue(DESCRIPTION, obj);
            String[] descArr = desc.split(deliminator);
            String category = getTagValue(CATEGORY, obj);
            URL url = new URL(getTagValue(LINK, obj));
            Date date = sdf.parse(getTagValue(PUB_DATE, obj));
            String location = getItemFromDescArray(descArr, dLocationTag);
            double depth = Double.parseDouble(removeAlphabeticalCharacters(getItemFromDescArray(descArr, dDepthTag)));
            double magnitude = Double.parseDouble(removeAlphabeticalCharacters(getItemFromDescArray(descArr, dMagnitudeTag)));
            String[] splitLoc = location.split(",");
            String locName = splitLoc[0];
            String locCounty = splitLoc.length == 2 ? splitLoc[1] : null;
            double latitude = Double.parseDouble(getTagValue(GEO_LAT, obj));
            double longitude = Double.parseDouble(getTagValue(GEO_LONG, obj));

            LatLng latLngObj = new LatLng(latitude, longitude);
            Location locationObj = new Location(locName, locCounty, latLngObj);
            Earthquake earthquakeObj = new Earthquake(date, locationObj, depth, magnitude);
            record = new EarthquakeRecord(category, url, earthquakeObj);
        } catch (MalformedURLException | ParseException e) {
            Log.e(TagUtils.getTag(this), e.getMessage());
            record = null;
        }
        return record;
    }

    private String getItemFromDescArray(String[] descArr, String tag) {
        for (String s : descArr) {
            String tagExt = tag + ": ";
            if (s.startsWith(tagExt)) {
                return s.replace(tagExt, "");
            }
        }
        Log.w(TagUtils.getTag(this), "Failed to find tag(" + tag + ") in array [" + Arrays.toString(descArr) + "]");
        return null;
    }

    private String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();

    }

    private String removeAlphabeticalCharacters(String str) {
        return str.replaceAll("[^\\d.]", "");
    }

}
