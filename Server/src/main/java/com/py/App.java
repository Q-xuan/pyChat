package com.py;

import com.py.server.ChatServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Hello world!
 */
public class App extends AbstractVerticle {



    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new ChatServer());
    }
}
