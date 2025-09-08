package com.freight.contract.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GraphQLConfig {
 
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(localDateTimeScalar())
                .scalar(localDateScalar())
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
    
    @Bean
    public GraphQLScalarType localDateScalar() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("A date without a time-zone in the ISO-8601 calendar system (YYYY-MM-DD)")
                .coercing(new graphql.schema.Coercing<LocalDate, String>() {
                    @Override
                    public String serialize(Object input) {
                        if (input instanceof LocalDate) {
                            return ((LocalDate) input).format(formatter);
                        }
                        throw new graphql.schema.CoercingSerializeException("Expected LocalDate");
                    }

                    @Override
                    public LocalDate parseValue(Object input) {
                        if (input instanceof String) {
                            try {
                                return LocalDate.parse((String) input, formatter);
                            } catch (Exception e) {
                                throw new graphql.schema.CoercingParseValueException("Invalid LocalDate format, expected YYYY-MM-DD");
                            }
                        }
                        throw new graphql.schema.CoercingParseValueException("Expected String");
                    }

                    @Override
                    public LocalDate parseLiteral(Object input) {
                        if (input instanceof graphql.language.StringValue) {
                            try {
                                return LocalDate.parse(((graphql.language.StringValue) input).getValue(), formatter);
                            } catch (Exception e) {
                                throw new graphql.schema.CoercingParseLiteralException("Invalid LocalDate format, expected YYYY-MM-DD");
                            }
                        }
                        throw new graphql.schema.CoercingParseLiteralException("Expected StringValue");
                    }
                })
                .build();
    }
}