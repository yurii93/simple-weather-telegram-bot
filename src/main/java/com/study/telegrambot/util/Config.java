package com.study.telegrambot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private static final Config INSTANCE = new Config();
    private final Properties props;

    private Config() {
        System.out.println("ConfigLoad");
        ClassLoader classLoader = Config.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE)) {
            Properties props = new Properties();
            props.load(inputStream);
            this.props = props;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load settings from config file: " + CONFIG_FILE);
        }
    }

    public static String get(String configProperty) {
        System.out.println("ConfigLoad get property: " + configProperty);
        String prop = INSTANCE.props.getProperty(configProperty);
        if (Objects.isNull(prop)) throw new IllegalArgumentException("Invalid prop name!");
        return INSTANCE.props.getProperty(configProperty);
    }
}
