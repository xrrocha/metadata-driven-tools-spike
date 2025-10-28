package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.sql.JDBCType;
import java.util.List;

/**
 * OXDateType - java.util.Date
 *
 * Legacy date type predating Java 8 time API.
 * Ambiguous without @Temporal annotation (could be DATE, TIME, or TIMESTAMP).
 * Maps to TIMESTAMP (preferred), DATE, TIME in JDBC.
 *
 * Note: java.sql.{Date,Time,Timestamp} all extend this class in Java,
 * but we model them as separate types since they have distinct JPA semantics.
 *
 * @author MetaXava Temporal Types 2025-10-28
 */
@Entity
@DiscriminatorValue("DATE")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXDateType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.util.Date";
    public static final String PACKAGE_NAME = "java.util";
    public static final String SIMPLE_NAME = "Date";

    @Override
    public String getName() {
        return QUALIFIED_NAME;
    }

    @Override
    public String generateJavaType() {
        return SIMPLE_NAME;
    }

    @Override
    public List<String> generateJPAAnnotations() {
        // java.util.Date is ambiguous without @Temporal
        // Default to TIMESTAMP (most common usage)
        return List.of("@Temporal(TemporalType.TIMESTAMP)");
    }

    @Override
    public List<JDBCType> declareCompatibleJdbcTypes() {
        return List.of(JDBCType.TIMESTAMP, JDBCType.DATE, JDBCType.TIME);
    }

    @Override
    public JDBCType declarePreferredJdbcType() {
        return JDBCType.TIMESTAMP;
    }
}
