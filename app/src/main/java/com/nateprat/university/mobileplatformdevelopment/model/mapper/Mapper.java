package com.nateprat.university.mobileplatformdevelopment.model.mapper;

@FunctionalInterface
public interface Mapper<From, To> {
    To map(From obj);
}
