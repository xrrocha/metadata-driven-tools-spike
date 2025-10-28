package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.sql.JDBCType;
import java.util.List;

/**
 * OXBigDecimalType - java.math.BigDecimal
 *
 * Exact decimal arithmetic with arbitrary precision.
 * Essential for financial calculations.
 * Maps to NUMERIC/DECIMAL JDBC types.
 *
 * @author MetaXava Reference Types 2025-10-28
 */
@Entity
@DiscriminatorValue("BIG_DECIMAL")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXBigDecimalType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.math.BigDecimal";
    public static final String PACKAGE_NAME = "java.math";
    public static final String SIMPLE_NAME = "BigDecimal";

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
        return List.of(JDBCType.NUMERIC, JDBCType.DECIMAL);
    }

    @Override
    public JDBCType declarePreferredJdbcType() {
        return JDBCType.NUMERIC;
    }
}
