package io.swagger.solon.api;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.models.Swagger;
import io.swagger.solon.models.SwaggerDocket;
import io.swagger.solon.models.SwaggerResource;
import io.swagger.solon.models.SwaggerBuilder;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Produces;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.handle.*;

/**
 * Swagger api Controller
 *
 * @author noear
 * @since 2.3
 */
public class SwaggerController {
    @Inject
    AopContext aopContext;

    /**
     * swagger 获取分组信息
     */
    @Produces("application/json; charset=utf-8")
    @Mapping("swagger-resources")
    public List<SwaggerResource> resources() {
        List<BeanWrap> list = aopContext.getWrapsOfType(SwaggerDocket.class);

        return list.stream().filter(bw -> Utils.isNotEmpty(bw.name()))
                .map(bw -> new SwaggerResource(bw.name(), ((SwaggerDocket) bw.raw()).groupName()))
                .collect(Collectors.toList());
    }

    /**
     * swagger 获取分组接口数据
     */
    @Produces("application/json; charset=utf-8")
    @Mapping("swagger/api")
    public String api(Context ctx, String group) throws IOException {
        SwaggerDocket docket = aopContext.getBean(group);

        if(docket == null){
            return null;
        }

        if (!BasicAuthUtils.basicAuth(ctx, docket)) {
            BasicAuthUtils.response401(ctx);
            return null;
        }

        if(docket.globalResponseCodes().containsKey(200) == false){
            docket.globalResponseCodes().put(200, "");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Swagger swagger = new SwaggerBuilder(docket).build();

        return mapper.writeValueAsString(swagger);
    }
}