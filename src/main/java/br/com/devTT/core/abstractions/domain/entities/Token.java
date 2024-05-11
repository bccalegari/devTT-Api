package br.com.devTT.core.abstractions.domain.entities;

public interface Token {
    String getToken();
    public boolean isExpired();
}
