package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

/**
 * OXSqlTimeType - java.sql.Time
 *
 * Time-only type (no date component).
 * Extends java.util.Date in Java but has distinct JPA semantics.
 * Maps to TIME (preferred), TIMESTAMP in JDBC.
 *
 * @author MetaXava Temporal Types 2025-10-28
 */
@Entity
@DiscriminatorValue("SQL_TIME")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXSqlTimeType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.sql.Time";
    public static final String PACKAGE_NAME = "java.sql";
    public static final String SIMPLE_NAME = "Time";

    @Override
    public String getName() {
        return QUALIFIED_NAME;
    }

    @Override
    public String generateJavaType() {
        // Must use FQN to avoid potential conflicts
        return QUALIFIED_NAME;
    }

    @Override
    public List<String> generateJPAAnnotations() {
        return List.of("@Temporal(TemporalType.TIME)");
    }
}
