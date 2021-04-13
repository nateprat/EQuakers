package com.nateprat.university.mobileplatformdevelopment.core.rss;

import android.util.Log;

import com.nateprat.university.mobileplatformdevelopment.model.EarthquakeRecord;
import com.nateprat.university.mobileplatformdevelopment.model.builder.EarthquakeRecordBuilder;
import com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper;
import com.nateprat.university.mobileplatformdevelopment.model.mapper.Mapper;
import com.nateprat.university.mobileplatformdevelopment.utils.TagUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.CATEGORY;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.DESCRIPTION;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.GEO_LAT;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.GEO_LONG;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.LINK;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.PUB_DATE;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.dDepthTag;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.dLocationTag;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.dMagnitudeTag;
import static com.nateprat.university.mobileplatformdevelopment.model.mapper.ElementEarthQuakeMapper.sdf;

public class RssParserImpl<Item> implements RssParser<Item> {

    private final URL url;
    private final Mapper<Element, Item> mapper;

    public RssParserImpl(URL url, Mapper<Element, Item> mapper) {
        this.url = url;
        this.mapper = mapper;
    }

    public List<EarthquakeRecord> parseWeirdMethod() {
        List<EarthquakeRecord> rssObjList = new CopyOnWriteArrayList<>();
        try {
            Log.i(TagUtils.getTag(this), "Opening connection to " + url.getPath());
            InputStream inStream = url.openStream();
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = xmlPullParserFactory.newPullParser();
            xpp.setInput(inStream, null);
            EarthquakeRecordBuilder builder = new EarthquakeRecordBuilder();
            String text = "";
            int eventType = xpp.getEventType();
            boolean withinItem = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            builder = new EarthquakeRecordBuilder();
                            withinItem = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            rssObjList.add(builder.build());
                            withinItem = false;
                        } else if (withinItem && tagName.equalsIgnoreCase(DESCRIPTION)) {
                            String desc = text;
                            String[] descArr = desc.split(" ; ");
                            String location = getItemFromDescArray(descArr, dLocationTag);
                            double depth = Double.parseDouble(removeAlphabeticalCharacters(getItemFromDescArray(descArr, dDepthTag)));
                            double magnitude = Double.parseDouble(removeAlphabeticalCharacters(getItemFromDescArray(descArr, dMagnitudeTag)));
                            String[] splitLoc = location.split(",");
                            String locName = splitLoc[0];
                            String locCounty = splitLoc.length == 2 ? splitLoc[1] : null;
                            builder.setLocationName(locName)
                                    .setLocationCounty(locCounty)
                                    .setDepth(depth)
                                    .setMagnitude(magnitude);
                        } else if (withinItem && tagName.equalsIgnoreCase(LINK)) {
                            builder.setUrl(text);
                        } else if (withinItem && tagName.equalsIgnoreCase(CATEGORY)) {
                            builder.setCategory(text);
                        } else if (withinItem && tagName.equalsIgnoreCase(PUB_DATE)) {
                            builder.setDate(sdf.parse(text));
                        } else if (withinItem && tagName.equalsIgnoreCase(GEO_LAT)) {
                            builder.setLatitude(Double.parseDouble(text));
                        } else if (withinItem && tagName.equalsIgnoreCase(GEO_LONG)) {
                            builder.setLongitude(Double.parseDouble(text));
                    }
                        break;
                    default:
                        break;
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException | IOException | ParseException e) {
            Log.e(TagUtils.getTag(this), e.getMessage());
        }
        return rssObjList;
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

    private String removeAlphabeticalCharacters(String str) {
        return str.replaceAll("[^\\d.]", "");
    }

    @Override
    public List<Item> parse() {
        List<Item> rssObjList = new CopyOnWriteArrayList<>();
        try {
            Log.i(TagUtils.getTag(this), "Opening connection to " + url.getPath());
            InputStream inStream = url.openStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(inStream);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) n;
                    Item item = (Item) mapper.map(e);
                    if (item != null) {
                        rssObjList.add(item);
                    } else {
                        Log.d(TagUtils.getTag(this), "Failed to add item: " + e.getTagName());
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            Log.e(TagUtils.getTag(this), e.getMessage(), e);
        }
        return rssObjList;
    }

}
