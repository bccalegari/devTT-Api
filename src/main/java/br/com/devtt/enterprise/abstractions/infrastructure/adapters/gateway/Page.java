package br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway;

import java.util.List;

public interface Page<T> {
    int getPage();
    int getSize();
    int getCurrentPage();
    long getTotalElements();
    long getTotalPages();
    List<T> getContent();
}