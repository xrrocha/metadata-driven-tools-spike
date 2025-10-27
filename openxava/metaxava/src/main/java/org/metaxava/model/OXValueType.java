package org.metaxava.model;

import javax.persistence.*;

import lombok.*;

/**
 * OXValueType - Abstract base for all value types in MetaXava
 * <p>
 * DESIGN STATUS: FIRST CUT - UNDER ITERATION
 * <p>
 * Value types are simple, unstructured types that:
 * 1. Map to single database columns (JPA @Basic @Column)
 * 2. Can be parsed from and formatted to strings
 * 3. Are NOT composed of entity relationships
 * 4. Are NOT navigable structures (unlike entities/embeddables)
 * <p>
 * This abstraction unifies:
 * - Java primitives (int, boolean, char, etc.)
 * - Wrapper types (Integer, Boolean, etc.)
 * - Built-in classes (String, BigDecimal, LocalDate)
 * - User-defined value types (Money, Email, ProductCode)
 * <p>
 * JPA COMPLIANCE:
 * User-defined value types map to JPA via @Converter + AttributeConverter
 * (See JPA 3.2 spec Chapter 11.1.10)
 * <p>
 * OPEN QUESTIONS (see docs/oxvaluetype-design.md):
 * 1. Should complex types (Money with amount+currency) be @Embeddable instead?
 * 2. Where should parse/format logic live? (DB? Registry? Code gen?)
 * 3. Should enums extend OXValueType or be siblings?
 * 4. Keep generics <T> for design clarity despite runtime erasure?
 * 5. Split OXNativeValueType vs OXUserValueType at class level?
 *
 * @author MetaXava Architecture Session 2025-10-27
 */
@MappedSuperclass
@Getter
@Setter
public abstract class OXValueType<T> extends OXType {

    @ManyToOne
    private OXDBType databaseType;

    /**
     * ValueType implementation for parse/format behavior
     *
     * DESIGN DECISION:
     * - Stored as FQN string, instantiated via Class.forName() + newInstance()
     * - Implementations MUST have empty constructor (framework convention)
     * - Handles runtime parse/format via minimal runtime JAR (ValueType interface)
     * - MetaXava ships ValueType<T> interface + generic JPA converter
     * - Generated OpenXava apps depend on this minimal runtime (~5-10KB JAR)
     */
    @Column(name = "value_type_class", length = 255, nullable = false)
    @Convert(converter = ValueTypeClassConverter.class)
    private ValueType<T> valueType;
}
