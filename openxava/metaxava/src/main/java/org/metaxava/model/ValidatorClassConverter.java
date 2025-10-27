package org.metaxava.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// TODO: Skeletal implementation - flesh out conversion logic

/**
 * JPA converter for Validator<?> ↔ String persistence
 *
 * DESIGN:
 * - Stores FQN of Validator implementation class
 * - Instantiates via Class.forName() + newInstance()
 * - Requires empty constructor convention
 * - Handles Validator.CATCH_ALL as special case (null or empty string)
 *
 * STATUS: Skeletal implementation - needs implementation
 */
@Converter(autoApply = false)
public class ValidatorClassConverter implements AttributeConverter<Validator<?>, String> {

    @Override
    public String convertToDatabaseColumn(Validator<?> attribute) {
        // TODO: Implement - handle CATCH_ALL specially, return FQN otherwise
        throw new UnsupportedOperationException("TODO: Implement Validator<?> → String conversion");
    }

    @Override
    public Validator<?> convertToEntityAttribute(String dbData) {
        // TODO: Implement - return CATCH_ALL for null/empty, instantiate otherwise
        throw new UnsupportedOperationException("TODO: Implement String → Validator<?> conversion");
    }
}
