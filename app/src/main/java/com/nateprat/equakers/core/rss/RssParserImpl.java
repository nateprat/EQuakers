package com.nateprat.equakers.core.rss;

import android.util.Log;

import com.nateprat.equakers.model.mapper.Mapper;
import com.nateprat.equakers.utils.TagUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RssParserImpl<Item> implements RssParser<Item> {

    private final URL url;
    private final Mapper<Element, Item> mapper;

    public RssParserImpl(URL url, Mapper<Element, Item> mapper) {
        this.url = url;
        this.mapper = mapper;
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
