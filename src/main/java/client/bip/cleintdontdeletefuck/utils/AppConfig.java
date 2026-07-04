package client.bip.cleintdontdeletefuck.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralised, externalised configuration for the client.
 *
 * <p>Values are resolved once at startup with the following precedence
 * (first non-blank wins):</p>
 * <ol>
 *     <li>JVM system property, e.g. {@code -Dbip.api.base-url=...}</li>
 *     <li>Environment variable, e.g. {@code BIP_API_BASE_URL}</li>
 *     <li>{@code config.properties} bundled on the classpath</li>
 *     <li>Hard-coded fallback (localhost)</li>
 * </ol>
 *
 * This replaces the values that used to be hard-coded in {@code MainController}.
 */
public final class AppConfig {

    private static final Properties FILE = load();

    /** Base URL of the bipApi server, e.g. {@code http://localhost:28242/bipapi/}. */
    public static final String API_BASE_URL =
            resolve("bip.api.base-url", "BIP_API_BASE_URL", "http://localhost:28242/bipapi/");

    /** Default path pre-filled in the XLSX dialog. Empty means "no default". */
    public static final String XLSX_DEFAULT_PATH =
            resolve("bip.xlsx.default-path", "BIP_XLSX_DEFAULT_PATH", "");

    private AppConfig() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = AppConfig.class.getResourceAsStream("/config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            System.err.println("[AppConfig] could not read config.properties, using defaults: " + e.getMessage());
        }
        return props;
    }

    private static String resolve(String propertyKey, String envKey, String fallback) {
        String fromSystem = System.getProperty(propertyKey);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem;
        }
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        String fromFile = FILE.getProperty(propertyKey);
        if (fromFile != null && !fromFile.isBlank()) {
            return fromFile;
        }
        return fallback;
    }
}
