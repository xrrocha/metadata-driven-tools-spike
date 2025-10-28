package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

/**
 * OXSqlDateType - java.sql.Date
 *
 * Date-only type (no time component).
 * Extends java.util.Date in Java but has distinct JPA semantics.
 * Maps to DATE (preferred), TIMESTAMP in JDBC.
 *
 * @author MetaXava Temporal Types 2025-10-28
 */
@Entity
@DiscriminatorValue("SQL_DATE")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXSqlDateType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.sql.Date";
    public static final String PACKAGE_NAME = "java.sql";
    public static final String SIMPLE_NAME = "Date";

    @Override
    public String getName() {
        return QUALIFIED_NAME;
    }

    @Override
    public String generateJavaType() {
        // Must use FQN to avoid conflict with java.util.Date
        return QUALIFIED_NAME;
    }

    @Override
    public List<String> generateJPAAnnotations() {
        return List.of("@Temporal(TemporalType.DATE)");
    }
}
