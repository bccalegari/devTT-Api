package br.com.devTT.core.domain.valueobjects.interfaces;

public interface TokenVerifier {
    boolean verify(String secretKey, String token);
}
