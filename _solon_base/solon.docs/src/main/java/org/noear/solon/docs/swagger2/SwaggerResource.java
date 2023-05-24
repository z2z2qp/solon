package org.noear.solon.docs.swagger2;

import org.noear.solon.Utils;

/**
 * @author noear
 * @since 2.3
 */
public class SwaggerResource {
    private String name;
    private String url;
    private String location;
    private String swaggerVersion;

    public SwaggerResource(String group, String groupName) {
        name = groupName;
        if (Utils.isNotEmpty(group)) {
            url = ("/swagger2/api?group=" + group);
            location = ("/swagger2/api?group=" + group);
            swaggerVersion = ("2.0");
        }
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return location;
    }

    public String getSwaggerVersion() {
        return swaggerVersion;
    }
}