package org.metaxava.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// TODO: Skeletal implementation - flesh out conversion logic

/**
 * JPA converter for ValueType<T> ↔ String persistence
 *
 * DESIGN:
 * - Stores FQN of ValueType implementation class
 * - Instantiates via Class.forName() + newInstance()
 * - Requires empty constructor convention
 *
 * STATUS: Skeletal implementation - needs implementation
 */
@Converter(autoApply = false)
public class ValueTypeClassConverter implements AttributeConverter<ValueType<?>, String> {

    @Override
    public String convertToDatabaseColumn(ValueType<?> attribute) {
        // TODO: Implement - return attribute.getClass().getName()
        throw new UnsupportedOperationException("TODO: Implement ValueType → String conversion");
    }

    @Override
    public ValueType<?> convertToEntityAttribute(String dbData) {
        // TODO: Implement - Class.forName(dbData).getDeclaredConstructor().newInstance()
        throw new UnsupportedOperationException("TODO: Implement String → ValueType conversion");
    }
}
