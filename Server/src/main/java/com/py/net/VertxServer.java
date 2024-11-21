package com.py.net;

import com.py.eventBus.AbsEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.py.kit.ClassKit.scanPackage;

@Slf4j
public class VertxServer extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        SockJSBridgeOptions opts = new SockJSBridgeOptions();
        List<AbsEvent> events = getEvents("com.py.eventBus");
        for (AbsEvent event : events) {
            opts.addInboundPermitted(new PermittedOptions().setAddress(event.inbound()));
            opts.addOutboundPermitted(new PermittedOptions().setAddress(event.outbound()));
        }
        router.route()
                .handler(cros())
                .subRouter(sockJSHandler.bridge(opts));
        vertx.createHttpServer()
                //跨域
                .requestHandler(router)
                .listen(8080)
                .onSuccess(server -> log.info("HTTP server started on port {}", server.actualPort()));
    }

    private static CorsHandler cros() {
        // 配置 CORS 中间件
        return CorsHandler.create()
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.PUT)
                .allowedMethod(io.vertx.core.http.HttpMethod.DELETE)
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowCredentials(true);
    }

    public List<AbsEvent> getEvents(String packageName) {
        List<AbsEvent> events = new ArrayList<>();
        Set<Class<?>> classes = scanPackage(packageName);
        for (Class<?> clazz : classes) {
            if (AbsEvent.class.isAssignableFrom(clazz) && clazz != AbsEvent.class) {
                try {
                    events.add((AbsEvent) clazz.getDeclaredConstructor(EventBus.class).newInstance(vertx.eventBus()));
                } catch (Exception e) {
                    log.error("Failed to create instance of class {}", clazz.getName(), e);
                }
            }
        }
        return events;
    }

}
