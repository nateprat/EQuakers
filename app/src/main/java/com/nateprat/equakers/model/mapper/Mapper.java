package com.nateprat.equakers.model.mapper;

@FunctionalInterface
public interface Mapper<From, To> {
    To map(From obj);
}
