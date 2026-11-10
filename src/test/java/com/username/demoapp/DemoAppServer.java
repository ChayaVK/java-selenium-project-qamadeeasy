package com.username.demoapp;

import com.sun.net.httpserver.HttpServer;
import com.username.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

/**
 * Serves the Practice Shop (src/test/resources/demo-app) on http://localhost:8080
 * using the web server built into Java, so the tests run with no internet.
 *
 * It starts only when base.url points to localhost and demo.app.autostart=true.
 * To test your own application, set base.url to its address: nothing starts then.
 */
public final class DemoAppServer {

    private static final Logger LOG = LogManager.getLogger(DemoAppServer.class);
    private static final Map<String, String> TYPES = Map.of(
            "html", "text/html; charset=utf-8", "css", "text/css", "js", "application/javascript");
    private static HttpServer server;

    private DemoAppServer() {
    }

    public static synchronized void startIfNeeded() {
        if (server != null || !ConfigReader.getBoolean("demo.app.autostart")) {
            return;
        }
        String host = URI.create(ConfigReader.get("base.url")).getHost();
        if (!"localhost".equals(host) && !"127.0.0.1".equals(host) && !"host.docker.internal".equals(host)) {
            return;
        }
        int port = ConfigReader.getInt("demo.app.port");
        // On a Docker Grid the browser runs inside a container, so it must reach this computer:
        // then listen on all network interfaces; otherwise only on this computer.
        String bindAddress = ConfigReader.getBoolean("remote") ? "0.0.0.0" : "127.0.0.1";
        try {
            server = HttpServer.create(new InetSocketAddress(bindAddress, port), 0);
        } catch (IOException e) {
            // Something already runs on the port (for example the app started by hand): use it.
            LOG.warn("Port {} is in use; using whatever is already running there", port);
            return;
        }
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }
            try (InputStream in = DemoAppServer.class.getClassLoader().getResourceAsStream("demo-app" + path);
                 OutputStream out = exchange.getResponseBody()) {
                if (in == null) {
                    byte[] notFound = "Not found".getBytes();
                    exchange.sendResponseHeaders(404, notFound.length);
                    out.write(notFound);
                    return;
                }
                byte[] body = in.readAllBytes();
                String ext = path.substring(path.lastIndexOf('.') + 1);
                exchange.getResponseHeaders().set("Content-Type", TYPES.getOrDefault(ext, "application/octet-stream"));
                exchange.sendResponseHeaders(200, body.length);
                out.write(body);
            }
        });
        server.start();
        LOG.info("Practice Shop running at http://localhost:{}", port);
    }

    public static synchronized void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
            LOG.info("Practice Shop stopped");
        }
    }

    /** Run this class on its own to explore the Practice Shop in your browser. */
    public static void main(String[] args) {
        startIfNeeded();
        System.out.println("Open http://localhost:" + ConfigReader.getInt("demo.app.port") + " (press Ctrl+C to stop)");
    }
}
