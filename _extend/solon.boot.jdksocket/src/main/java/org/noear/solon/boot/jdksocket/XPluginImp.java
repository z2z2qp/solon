package org.noear.solon.boot.jdksocket;

import org.noear.solon.Solon;
import org.noear.solon.core.Plugin;
import org.noear.solon.extend.xsocket.MessageSessionFactory;

public class XPluginImp implements Plugin {
    SocketServer _server;

    public static String solon_boot_ver(){
        return "jdk tpc socket/" + Solon.cfg().version();
    }

    @Override
    public void start(Solon app) {
        //注册会话工厂
        MessageSessionFactory.setInstance(new _SessionFactoryImpl());

        if (app.enableSocket() == false) {
            return;
        }

        long time_start = System.currentTimeMillis();

        System.out.println("solon.Server:main: java.net.ServerSocket(jdksocket)");

        int _port = app.props().getInt("server.socket.port", 0);
        if (_port < 1) {
            _port = 20000 + app.port();
        }

        try {
            _server = new SocketServer();
            _server.start(_port);

            long time_end = System.currentTimeMillis();

            System.out.println("solon.Connector:main: jdksocket: Started ServerConnector@{[Socket]}{0.0.0.0:" + _port + "}");
            System.out.println("solon.Server:main: jdksocket: Started @" + (time_end - time_start) + "ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() throws Throwable {
        if(_server == null){
            return;
        }

        _server.stop();
        _server = null;
        System.out.println("solon.Server:main: jdksocket: Has Stopped " + solon_boot_ver());
    }
}
