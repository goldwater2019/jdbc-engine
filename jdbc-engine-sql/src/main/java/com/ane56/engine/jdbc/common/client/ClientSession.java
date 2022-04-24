package com.ane56.engine.jdbc.common.client;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.US_ASCII;
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
    private String clientInfo;
    private String catalog;
    private String schema;
    private Map<String, String> resourceEstimates;
    private Map<String, String> properties;
    private Map<String, String> preparedStatements;
    private String transactionId;
    private int clientRequestTimeout;
    private Map<String, String> sessionFunctions;

    public static ClientSession stripTransactionId(ClientSession session) {
        return ClientSession.builder()
                .server(session.getServer())
                .user(session.getUser())
                .source(session.getSource())
                .clientInfo(session.getClientInfo())
                .catalog(session.getCatalog())
                .schema(session.getSchema())
                .resourceEstimates(ImmutableMap.copyOf(session.getResourceEstimates()))
                .properties(ImmutableMap.copyOf(session.getProperties()))
                .preparedStatements(ImmutableMap.copyOf(session.getPreparedStatements()))
                .transactionId(null)
                .clientRequestTimeout(session.getClientRequestTimeout())
                .sessionFunctions(session.getSessionFunctions())
                .build();
    }

    public ClientSession(
            URI server,
            String user,
            String source,
            String clientInfo,
            String catalog,
            String schema,
            Map<String, String> resourceEstimates,
            Map<String, String> properties,
            Map<String, String> preparedStatements,
            String transactionId,
            int clientRequestTimeout,
            Map<String, String> sessionFunctions) {
        this.server = requireNonNull(server, "server is null");
        this.user = user;
        this.source = source;
        this.clientInfo = clientInfo;
        this.catalog = catalog;
        this.schema = schema;
        this.transactionId = transactionId;
        this.resourceEstimates = ImmutableMap.copyOf(requireNonNull(resourceEstimates, "resourceEstimates is null"));
        this.properties = ImmutableMap.copyOf(requireNonNull(properties, "properties is null"));
        this.preparedStatements = ImmutableMap.copyOf(requireNonNull(preparedStatements, "preparedStatements is null"));
        this.clientRequestTimeout = clientRequestTimeout;
        this.sessionFunctions = ImmutableMap.copyOf(requireNonNull(sessionFunctions, "sessionFunctions is null"));

        // verify that resource estimates are valid
        CharsetEncoder charsetEncoder = US_ASCII.newEncoder();
        for (Map.Entry<String, String> entry : resourceEstimates.entrySet()) {
            checkArgument(!entry.getKey().isEmpty(), "Resource name is empty");
            checkArgument(entry.getKey().indexOf('=') < 0, "Resource name must not contain '=': %s", entry.getKey());
            checkArgument(charsetEncoder.canEncode(entry.getKey()), "Resource name is not US_ASCII: %s", entry.getKey());
        }

        // verify the properties are valid
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            checkArgument(!entry.getKey().isEmpty(), "Session property name is empty");
            checkArgument(entry.getKey().indexOf('=') < 0, "Session property name must not contain '=': %s", entry.getKey());
            checkArgument(charsetEncoder.canEncode(entry.getKey()), "Session property name is not US_ASCII: %s", entry.getKey());
            checkArgument(charsetEncoder.canEncode(entry.getValue()), "Session property value is not US_ASCII: %s", entry.getValue());
        }
    }


    public boolean isDebug() {
        return false;
    }

}

