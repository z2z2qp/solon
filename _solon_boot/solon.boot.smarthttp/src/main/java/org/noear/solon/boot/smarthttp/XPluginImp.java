package org.noear.solon.boot.smarthttp;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.smarthttp.http.SmHttpContextHandler;
import org.noear.solon.boot.smarthttp.http.FormContentFilter;
import org.noear.solon.core.*;
import org.noear.solon.core.util.LogUtil;

public final class XPluginImp implements Plugin {
    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }


    public static String solon_boot_ver() {
        return "smart http 1.1/" + Solon.version();
    }

    private SmHttpServer _server;

    @Override
    public void start(AopContext context) {
        if (Solon.app().enableHttp() == false) {
            return;
        }

        context.lifecycle(-99, () -> {
            start0(Solon.app());
        });
    }

    private void start0(SolonApp app) throws Throwable {
        //初始化属性
        ServerProps.init();

        HttpServerProps props = new HttpServerProps();
        final String _host = props.getHost();
        final int _port = props.getPort();
        final String _name = props.getName();

        long time_start = System.currentTimeMillis();

        SmHttpContextHandler _handler = new SmHttpContextHandler();

        if (props.isIoBound()) {
            //如果是io密集型的，加二段线程池
            _handler.setExecutor(props.getBioExecutor("smarthttp-"));
        }

        final String _wrapHost = props.getWrapHost();
        final int _wrapPort = props.getWrapPort();
        _signal = new SignalSim(_name, _wrapHost, _wrapPort, "http", SignalType.HTTP);

        _server = new SmHttpServer();
        _server.setEnableWebSocket(app.enableWebSocket());
        _server.setCoreThreads(props.getCoreThreads());
        _server.setHandler(_handler);
        _server.start(_host, _port);

        app.signalAdd(_signal);

        app.before(-9, new FormContentFilter());

        long time_end = System.currentTimeMillis();

        String connectorInfo = "solon.connector:main: smarthttp: Started ServerConnector@{HTTP/1.1,[http/1.1]";
        if (app.enableWebSocket()) {
            LogUtil.global().info(connectorInfo + "[WebSocket]}{0.0.0.0:" + _port + "}");
        }

        LogUtil.global().info(connectorInfo + "}{http://localhost:" + _port + "}");

        LogUtil.global().info("Server:main: smarthttp: Started (" + solon_boot_ver() + ") @" + (time_end - time_start) + "ms");
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;

            LogUtil.global().info("Server:main: smarthttp: Has Stopped (" + solon_boot_ver() + ")");
        }
    }
}