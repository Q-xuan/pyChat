package com.py.net;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxServer extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        new PermittedOptions();

        SockJSBridgeOptions opts = new SockJSBridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("user:message"))
                .addInboundPermitted(new PermittedOptions().setAddress("user:join"))
                .addInboundPermitted(new PermittedOptions().setAddress("channel:switch"))
                .addInboundPermitted(new PermittedOptions().setAddress("channel:create"))

                .addOutboundPermitted(new PermittedOptions().setAddress("message"))
                .addOutboundPermitted(new PermittedOptions().setAddress("user:joined"))
                .addOutboundPermitted(new PermittedOptions().setAddress("user:left"))
                .addOutboundPermitted(new PermittedOptions().setAddress("channel:created"))
                ;

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        router.route("/ws*").subRouter(sockJSHandler.bridge(opts));

        vertx.createHttpServer().requestHandler(router).listen(8080);

        EventBus eb = vertx.eventBus();

        eb.consumer("user:message", msg -> {

        });
    }

}
