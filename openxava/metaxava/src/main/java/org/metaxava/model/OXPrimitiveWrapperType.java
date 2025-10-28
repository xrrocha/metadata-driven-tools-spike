package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * OXPrimitiveWrapperType - Wrapper classes for Java primitives
 *
 * Examples: Integer, Boolean, Long, Short, Byte, Float, Double, Character
 *
 * Characteristics:
 * - Reference types (java.lang.*)
 * - Map to same JDBC types as their primitive counterparts
 * - Nullable (unlike primitives)
 * - Bidirectional relationship with primitive types
 *
 * @author MetaXava Reference Types 2025-10-28
 */
@Entity
@DiscriminatorValue("WRAPPER")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXPrimitiveWrapperType extends OXBasicReferenceType {

    // Constants for all 8 wrapper types
    public static final String PACKAGE_NAME = "java.lang";

    public static final String BYTE_SIMPLE_NAME = "Byte";
    public static final String SHORT_SIMPLE_NAME = "Short";
    public static final String INTEGER_SIMPLE_NAME = "Integer";
    public static final String LONG_SIMPLE_NAME = "Long";
    public static final String FLOAT_SIMPLE_NAME = "Float";
    public static final String DOUBLE_SIMPLE_NAME = "Double";
    public static final String CHARACTER_SIMPLE_NAME = "Character";
    public static final String BOOLEAN_SIMPLE_NAME = "Boolean";

    public static final String BYTE_QUALIFIED_NAME = PACKAGE_NAME + "." + BYTE_SIMPLE_NAME;
    public static final String SHORT_QUALIFIED_NAME = PACKAGE_NAME + "." + SHORT_SIMPLE_NAME;
    public static final String INTEGER_QUALIFIED_NAME = PACKAGE_NAME + "." + INTEGER_SIMPLE_NAME;
    public static final String LONG_QUALIFIED_NAME = PACKAGE_NAME + "." + LONG_SIMPLE_NAME;
    public static final String FLOAT_QUALIFIED_NAME = PACKAGE_NAME + "." + FLOAT_SIMPLE_NAME;
    public static final String DOUBLE_QUALIFIED_NAME = PACKAGE_NAME + "." + DOUBLE_SIMPLE_NAME;
    public static final String CHARACTER_QUALIFIED_NAME = PACKAGE_NAME + "." + CHARACTER_SIMPLE_NAME;
    public static final String BOOLEAN_QUALIFIED_NAME = PACKAGE_NAME + "." + BOOLEAN_SIMPLE_NAME;

    @Override
    public String getName() {
        return getQualifiedName();
    }

    @Override
    public String generateJavaType() {
        return getSimpleName();
    }
}
