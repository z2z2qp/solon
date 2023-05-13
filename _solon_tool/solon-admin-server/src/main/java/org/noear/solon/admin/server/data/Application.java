package org.noear.solon.admin.server.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(of = {"name", "baseUrl"})
@EqualsAndHashCode(of = {"name", "baseUrl"})
@NoArgsConstructor
public class Application {

    private String name;

    private String baseUrl;

    private String metadata;

    private Status status = Status.DOWN;

    private long lastHeartbeat;

    public enum Status {
        UP,
        DOWN
    }

}
