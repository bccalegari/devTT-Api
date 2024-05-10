package br.com.devTT.core.domain.valueobjects.interfaces;

public interface Token {
    String getToken();
    public boolean isExpired();
    public boolean isValid(TokenVerifier verifier);
}
