package client.bip.cleintdontdeletefuck;

/**
 * Plain (non-JavaFX) entry point used when packaging with jpackage.
 *
 * <p>A main class that extends {@link javafx.application.Application} makes the Java
 * launcher demand JavaFX as <em>named modules</em> ("JavaFX runtime components are
 * missing") when the app is started from the classpath — which is exactly how a
 * jpackage app-image launches. Delegating through this non-Application class avoids
 * that check, so the bundled JavaFX jars on the classpath load normally.</p>
 *
 * <p>Development still uses {@code javafx:run} / {@link ClientApplication}; this class
 * only exists so the packaged {@code .exe} starts.</p>
 */
public final class Launcher {

    public static void main(String[] args) {
        ClientApplication.main(args);
    }

    private Launcher() {
    }
}
