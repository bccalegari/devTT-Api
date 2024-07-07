package br.com.devtt.infrastructure.configuration.environment;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentConfig {
    private final Dotenv env;

    private EnvironmentConfig() {
        env = Dotenv.configure().filename(".env").load();
    }

    private static class Holder {
        private static final EnvironmentConfig INSTANCE = new EnvironmentConfig();
    }

    public static EnvironmentConfig getInstance() {
        return Holder.INSTANCE;
    }

    public String get(String key) {
        return env.get(key);
    }
}