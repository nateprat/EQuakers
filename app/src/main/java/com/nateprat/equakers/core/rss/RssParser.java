package com.nateprat.equakers.core.rss;

import com.nateprat.equakers.model.mapper.Mapper;

import java.util.List;

public interface RssParser<To> {
    List<To> parse();
}
