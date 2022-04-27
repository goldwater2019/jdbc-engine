package com.ane56.engine.jdbc.common.client;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 3:27 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ClientSession {
    private URI server;
    private String user;
    private String source;
    private String catalog;
    private String schema;
    private Map<String, String> properties;
    private String transactionId;

    public static ClientSession stripTransactionId(ClientSession session) {
        return ClientSession.builder()
                .server(session.getServer())
                .user(session.getUser())
                .source(session.getSource())
                .catalog(session.getCatalog())
                .schema(session.getSchema())
                .properties(ImmutableMap.copyOf(session.getProperties()))
                .transactionId(null)
                .build();
    }

    public ClientSession(
            URI server,
            String user,
            String source,
            String catalog,
            String schema,
            Map<String, String> properties,
            String transactionId) {
        this.server = requireNonNull(server, "server is null");
        this.user = user;
        this.source = source;
        this.catalog = catalog;
        this.schema = schema;
        this.transactionId = transactionId;
        this.properties = ImmutableMap.copyOf(requireNonNull(properties, "properties is null"));

    }


    public boolean isDebug() {
        return false;
    }

}

