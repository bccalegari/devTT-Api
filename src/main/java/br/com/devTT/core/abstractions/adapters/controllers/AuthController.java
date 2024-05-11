package br.com.devTT.core.abstractions.adapters.controllers;

public interface AuthController<R, V> {
    R login (V v);
}