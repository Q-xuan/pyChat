package com.py;

import com.py.net.VertxServer;
import io.vertx.core.Launcher;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Launcher.executeCommand("run", VertxServer.class.getName());
    }
}
