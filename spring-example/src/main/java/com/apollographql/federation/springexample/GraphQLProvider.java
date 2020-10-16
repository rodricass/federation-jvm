package com.apollographql.federation.springexample;

import com.apollographql.federation.graphqljava.Federation;
import com.coxautodev.graphql.tools.SchemaParser;
import com.graphqljava.tutorial.bookdetails.GraphQLDataFetchers;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.servlet.config.DefaultGraphQLSchemaProvider;
import graphql.servlet.config.GraphQLSchemaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import javax.management.Query;
import java.io.IOException;

@Component
public class GraphQLProvider extends DefaultGraphQLSchemaProvider implements GraphQLSchemaProvider {
    @Autowired
    private static GraphQLDataFetchers graphQLDataFetchers;

    public GraphQLProvider(@Value("classpath:schemas/inventory.graphqls") Resource sdl) throws IOException {
        super(Federation.transform(sdl.getFile(), GraphQLProvider.buildWiring())
                .build());
    }

    private static RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }
}
