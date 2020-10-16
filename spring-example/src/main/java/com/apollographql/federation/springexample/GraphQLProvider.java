package com.apollographql.federation.springexample;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.graphqljava.tutorial.bookdetails.GraphQLDataFetchers;
import graphql.schema.idl.RuntimeWiring;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider extends DefaultGraphQLSchemaProvider implements GraphQLSchemaProvider {
    @Autowired
    private static GraphQLDataFetchers graphQLDataFetchers;

    public GraphQLProvider(@Value("classpath:schemas/inventory.graphqls") Resource sdl) throws IOException {
        super(Federation.transform(sdl.getFile(), GraphQLProvider.buildWiring())
                .fetchEntities(env -> env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                        .stream()
                        .map(values -> {
                            System.out.println("values" + values.toString());
                            if ("Notification".equals(values.get("__typename"))) {
                                final Object id = values.get("id");
                                if (id instanceof String) {
                                    return 1;
                                }
                            }
                            return 1;
                        })
                        .collect(Collectors.toList()))
                .resolveEntityType(env -> {
                    System.out.println("env" + env.toString());
                    final Object src = env.getObject();
                    if (src instanceof Case) {
                        return env.getSchema().getObjectType("Case");
                    }
                    return null;
                })
                .build());
    }

    private static RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("getNotificationById", graphQLDataFetchers.getNotificationById()))
                .build();
    }

    @NotNull
    private static Integer lookupCaseIdByNotificationId(@NotNull Integer id) {
            return 1;
    }
}
