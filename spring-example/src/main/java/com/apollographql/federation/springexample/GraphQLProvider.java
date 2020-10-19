package com.apollographql.federation.springexample;

import com.apollographql.federation.graphqljava.Federation;

import com.apollographql.federation.graphqljava._Entity;
import graphql.schema.idl.RuntimeWiring;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import javax.management.Query;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GraphQLProvider extends DefaultGraphQLSchemaProvider implements GraphQLSchemaProvider {
    @Autowired
    private static GraphQLDataFetchers graphQLDataFetchers;

    public GraphQLProvider(@Value("classpath:schemas/inventory.graphqls") Resource sdl) throws IOException {
        super(Federation.transform(sdl.getFile(), GraphQLProvider.buildWiring())
                .fetchEntities(env -> env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                        .stream()
                        .map(values -> {
                            if ("Client".equals(values.get("__typename"))) {
                                log.info("entro");
                            }
                            return null;
                        })
                        .collect(Collectors.toList()))
                .resolveEntityType(env -> env.getSchema().getObjectType("Client"))
                .build());
    }

    private static RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                ).type("Book", typeWiring -> typeWiring
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher())
                )
                .build();
    }
}
