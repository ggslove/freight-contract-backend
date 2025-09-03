package com.freight.contract.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GraphQLConfig {
    
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(localDateTimeScalar())
                .scalar(ExtendedScalars.GraphQLBigDecimal);
    }
    
    @Bean
    public GraphQLScalarType localDateTimeScalar() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("A date-time without a time-zone in the ISO-8601 calendar system")
                .coercing(new graphql.schema.Coercing<LocalDateTime, String>() {
                    @Override
                    public String serialize(Object input) {
                        if (input instanceof LocalDateTime) {
                            return ((LocalDateTime) input).format(formatter);
                        }
                        throw new graphql.schema.CoercingSerializeException("Expected LocalDateTime");
                    }

                    @Override
                    public LocalDateTime parseValue(Object input) {
                        if (input instanceof String) {
                            try {
                                return LocalDateTime.parse((String) input, formatter);
                            } catch (Exception e) {
                                throw new graphql.schema.CoercingParseValueException("Invalid LocalDateTime format");
                            }
                        }
                        throw new graphql.schema.CoercingParseValueException("Expected String");
                    }

                    @Override
                    public LocalDateTime parseLiteral(Object input) {
                        if (input instanceof graphql.language.StringValue) {
                            try {
                                return LocalDateTime.parse(((graphql.language.StringValue) input).getValue(), formatter);
                            } catch (Exception e) {
                                throw new graphql.schema.CoercingParseLiteralException("Invalid LocalDateTime format");
                            }
                        }
                        throw new graphql.schema.CoercingParseLiteralException("Expected StringValue");
                    }
                })
                .build();
    }
}