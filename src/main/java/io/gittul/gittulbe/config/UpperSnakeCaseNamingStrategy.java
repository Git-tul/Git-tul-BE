package io.gittul.gittulbe.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

/**
 * UPPER_SNAKE_CASE
 * or
 * SCREAMING_SNAKE_CASE
 */
@Component
public class UpperSnakeCaseNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return toUpperSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return toUpperSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return toUpperSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return toUpperSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return toUpperSnakeCase(identifier);
    }

    private Identifier toUpperSnakeCase(Identifier identifier) {
        if (identifier == null) {
            return null;
        }
        String snakeCase = identifier.getText().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
        return Identifier.toIdentifier(snakeCase, identifier.isQuoted());
    }
}
