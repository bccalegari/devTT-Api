package br.com.devtt.enterprise.abstractions.infrastructure.adapters.gateway;

public interface CacheGateway {
    void put(String key, Object value);
    void put(String key, Object value, Long ttl);
    Object get(String key);
    void delete(String key);
    void deleteAllFrom(String pattern);
}