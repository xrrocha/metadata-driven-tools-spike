package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * OXBigIntegerType - java.math.BigInteger
 *
 * Arbitrary-precision integer arithmetic.
 * Maps to NUMERIC/DECIMAL/BIGINT JDBC types.
 *
 * @author MetaXava Reference Types 2025-10-28
 */
@Entity
@DiscriminatorValue("BIG_INTEGER")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public class OXBigIntegerType extends OXBasicReferenceType {

    public static final String QUALIFIED_NAME = "java.math.BigInteger";
    public static final String PACKAGE_NAME = "java.math";
    public static final String SIMPLE_NAME = "BigInteger";

    @Override
    public String getName() {
        return QUALIFIED_NAME;
    }

    @Override
    public String generateJavaType() {
        return SIMPLE_NAME;
    }
}
