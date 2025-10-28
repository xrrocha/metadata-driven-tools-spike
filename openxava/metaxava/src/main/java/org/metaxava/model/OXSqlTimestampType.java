package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

/**
 * OXSqlTimestampType - java.sql.Timestamp
 *
 * Full date + time + nanoseconds type.
 * Extends java.util.Date in Java but has distinct JPA semantics.
 * Maps to TIMESTAMP (preferred), DATE, TIME in JDBC.
 *
 * @author MetaXava Temporal Types 2025-10-28
 */
@Entity
@DiscriminatorValue("SQL_TIMESTAMP")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXSqlTimestampType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.sql.Timestamp";
    public static final String PACKAGE_NAME = "java.sql";
    public static final String SIMPLE_NAME = "Timestamp";

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
        return List.of("@Temporal(TemporalType.TIMESTAMP)");
    }
}
