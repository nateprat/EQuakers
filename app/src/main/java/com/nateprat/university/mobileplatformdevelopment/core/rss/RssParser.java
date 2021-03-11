package com.nateprat.university.mobileplatformdevelopment.core.rss;

import java.util.List;

public interface RssParser<To> {
    List<To> parse();
}
