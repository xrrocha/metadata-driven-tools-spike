package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.sql.JDBCType;
import java.util.List;

/**
 * OXStringType - java.lang.String
 *
 * The most common reference type in Java applications.
 * Maps to VARCHAR (preferred), CHAR, CLOB, LONGVARCHAR in JDBC.
 *
 * @author MetaXava Reference Types 2025-10-28
 */
@Entity
@DiscriminatorValue("STRING")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXStringType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.lang.String";
    public static final String PACKAGE_NAME = "java.lang";
    public static final String SIMPLE_NAME = "String";

    @Override
    public String getName() {
        return QUALIFIED_NAME;
    }

    @Override
    public String generateJavaType() {
        return SIMPLE_NAME;
    }

    @Override
    public List<JDBCType> declareCompatibleJdbcTypes() {
        return List.of(JDBCType.VARCHAR, JDBCType.CHAR, JDBCType.CLOB);
    }

    @Override
    public JDBCType declarePreferredJdbcType() {
        return JDBCType.VARCHAR;
    }
}
