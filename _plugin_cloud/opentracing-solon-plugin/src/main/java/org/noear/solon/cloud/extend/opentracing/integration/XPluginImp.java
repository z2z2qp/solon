package org.noear.solon.cloud.extend.opentracing.integration;

import org.noear.solon.SolonApp;
import org.noear.solon.cloud.extend.opentracing.OpentracingProps;
import org.noear.solon.cloud.tracing.TracingManager;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.4
 */
public class XPluginImp implements Plugin {

    @Override
    public void start(SolonApp app) {
        if (OpentracingProps.instance.getTraceEnable() == false) {
            return;
        }

        TracingManager.enable(OpentracingProps.instance.getTraceExclude());
    }
}
