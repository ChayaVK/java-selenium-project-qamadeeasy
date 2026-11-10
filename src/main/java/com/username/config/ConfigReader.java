package com.username.config;

import com.username.constants.FrameworkConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads settings in three layers. A later layer wins:
 * <ol>
 *   <li>config.properties (defaults)</li>
 *   <li>env/&lt;env&gt;.properties when you run with -Denv=qa (only the keys it contains)</li>
 *   <li>-Dkey=value on the command line, for example -Dbrowser=firefox</li>
 * </ol>
 * Tests never hard-code a URL, a browser or a wait: they ask ConfigReader.
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        load(FrameworkConstants.CONFIG_FILE, true);
        String env = System.getProperty("env");
        if (env != null && !env.isBlank()) {
            load(FrameworkConstants.ENV_FOLDER + env.trim() + ".properties", true);
            System.out.println("[ConfigReader] Loaded settings for environment: " + env.trim());
        }
    }

    private ConfigReader() {
    }

    private static void load(String resource, boolean required) {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                if (required) {
                    String message = "Config file not found: src/main/resources/" + resource;
                    System.err.println("[ConfigReader] " + message);
                    throw new IllegalStateException(message);
                }
                return;
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resource, e);
        }
    }

    /** The value for a key; a -Dkey=value system property always wins. */
    public static String get(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        if (value == null) {
            throw new IllegalArgumentException("Missing config key '" + key + "' in config.properties");
        }
        return value.trim();
    }

    public static String get(String key, String defaultValue) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        return value == null ? defaultValue : value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
